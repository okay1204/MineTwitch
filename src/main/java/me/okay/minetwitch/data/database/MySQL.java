package me.okay.minetwitch.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Logger;

import me.okay.minetwitch.MinetwitchPlugin;

public class MySQL implements Database {

    private final Logger logger;
    private Connection conn;

    public MySQL(MinetwitchPlugin plugin) {
        logger = plugin.getLogger();

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:mysql://"
                    + plugin.getConfig().getString("mysql.host") + ":"
                    + plugin.getConfig().getString("mysql.port") + "/"
                    + plugin.getConfig().getString("mysql.database"),
                    plugin.getConfig().getString("mysql.username"),
                    plugin.getConfig().getString("mysql.password"));

            logger.info("Connected to database.");
        }
        catch (SQLException e) {
            logger.severe("FAILED TO CONNECT TO DATABASE. Please check your config.yml and reload.");
            return;
        }

        try {
            conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS `LinkedAccounts` (
                    `minecraftUuid` BINARY(16) UNIQUE NOT NULL,
                    `twitchId` INT UNSIGNED UNIQUE NOT NULL
                );
            """).execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void safeDisconnect() {
        if (conn != null) {
            try {
                conn.close();
                logger.info("Disconnected from database");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isClosed() {
        try {
            return conn.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    @Override
    public void setLinkedAccount(UUID minecraftUuid, String twitchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("REPLACE INTO LinkedAccounts (minecraftUuid, twitchId) VALUES (UUID_TO_BIN(?), ?);");
            statement.setString(1, minecraftUuid.toString());
            statement.setString(2, twitchId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeLinkedAccount(UUID minecraftUuid) {
        try {
            PreparedStatement statement = conn
                    .prepareStatement("DELETE FROM LinkedAccounts WHERE minecraftUuid = UUID_TO_BIN(?);");
            statement.setString(1, minecraftUuid.toString());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeLinkedAccount(String twitchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM LinkedAccounts WHERE twitchId = ?;");
            statement.setString(1, twitchId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getTwitchId(UUID minecraftUuid) {
        try {
            PreparedStatement statement = conn
                    .prepareStatement("SELECT twitchId FROM LinkedAccounts WHERE minecraftUuid = UUID_TO_BIN(?);");
            statement.setString(1, minecraftUuid.toString());
            statement.execute();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("twitchId");
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID getMinecraftUuid(String twitchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT BIN_TO_UUID(minecraftUuid) FROM LinkedAccounts WHERE twitchId = ?;");
            statement.setString(1, twitchId);
            statement.execute();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return UUID.fromString(resultSet.getString("minecraftUuid"));
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
