package org.mateh.meowSMP.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLiteManager {

    private Connection connection;

    public SQLiteManager(String dbPath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS token_data (" +
                "uuid TEXT NOT NULL, " +
                "token_key TEXT NOT NULL, " +
                "level INTEGER DEFAULT 1, " +
                "kills INTEGER DEFAULT 0, " +
                "PRIMARY KEY (uuid, token_key)" +
                ");";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveTokenData(String uuid, String tokenKey, int level, int kills) {
        String sql = "INSERT OR REPLACE INTO token_data (uuid, token_key, level, kills) VALUES (?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.setString(2, tokenKey);
            stmt.setInt(3, level);
            stmt.setInt(4, kills);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TokenData loadTokenData(String uuid, String tokenKey) {
        String sql = "SELECT level, kills FROM token_data WHERE uuid = ? AND token_key = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.setString(2, tokenKey);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int level = rs.getInt("level");
                    int kills = rs.getInt("kills");
                    return new TokenData(level, kills);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new TokenData(1, 0);
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TokenData {
        private final int level;
        private final int kills;

        public TokenData(int level, int kills) {
            this.level = level;
            this.kills = kills;
        }

        public int getLevel() {
            return level;
        }

        public int getKills() {
            return kills;
        }
    }
}
