package me.okay.minetwitch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;


public class Database {
    
    private final String CONNECT_URL;
    private final Logger logger;
    private Connection conn;

    public Database(Minetwitch plugin) {
        CONNECT_URL = "jdbc:sqlite:" + plugin.getDataFolder() + "/storage.db";

        logger = plugin.getLogger();

        try {
            // Create a connection to the database
            conn = DriverManager.getConnection(CONNECT_URL);

            logger.info("Connected to database.");
            
            conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS "LinkedAccounts" (
                    "minecraftUuid" TEXT NOT NULL,
                    "twitchId" TEXT NOT NULL
                );
            """).execute();

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        }

    }

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

    public boolean isClosed() {
        try {
            return conn.isClosed();
        }
        catch (SQLException e) {
            return true;
        }
    }
}