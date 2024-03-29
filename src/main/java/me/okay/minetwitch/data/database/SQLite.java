package me.okay.minetwitch.data.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import me.okay.minetwitch.MinetwitchPlugin;

public class SQLite implements Database {
    private final Logger logger;
    private Connection conn;

    public SQLite(MinetwitchPlugin plugin) {
        logger = plugin.getLogger();

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + "/storage.db");

            logger.info("Connected to database.");

            conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS `LinkedAccounts` (
                    `minecraftUuid` TEXT UNIQUE NOT NULL,
                    `twitchId` INTEGER UNIQUE NOT NULL
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
    public void setLinkedAccount(UUID minecraftUuid, int twitchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("REPLACE INTO LinkedAccounts (minecraftUuid, twitchId) VALUES (?, ?);");
            statement.setString(1, minecraftUuid.toString());
            statement.setInt(2, twitchId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeLinkedAccount(UUID minecraftUuid) {
        try {
            PreparedStatement statement = conn
                    .prepareStatement("DELETE FROM LinkedAccounts WHERE minecraftUuid = ?;");
            statement.setString(1, minecraftUuid.toString());
            statement.execute();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeLinkedAccount(int twitchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM LinkedAccounts WHERE twitchId = ?;");
            statement.setInt(1, twitchId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Integer> getTwitchId(UUID minecraftUuid) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT twitchId FROM LinkedAccounts WHERE minecraftUuid = ?;");
            statement.setString(1, minecraftUuid.toString());
            ResultSet resultSet = statement.executeQuery();

            return Optional.ofNullable(resultSet.next() ? resultSet.getInt("twitchId") : null);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UUID> getMinecraftUuid(int twitchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT minecraftUuid FROM LinkedAccounts WHERE twitchId = ?;");
            statement.setInt(1, twitchId);
            ResultSet resultSet = statement.executeQuery();

            return Optional.ofNullable(resultSet.next() ? UUID.fromString(resultSet.getString("minecraftUuid")) : null);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}