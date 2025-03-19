package org.mateh.meowSMP.data;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SQLiteManager {

    private Connection connection;

    public SQLiteManager(String dbPath) {
        try {
            File dbFile = new File(dbPath);
            File parent = dbFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            createTables();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        // Tabla para tokens (anterior)
        String sql1 = "CREATE TABLE IF NOT EXISTS token_data (" +
                "uuid TEXT NOT NULL, " +
                "token_key TEXT NOT NULL, " +
                "level INTEGER DEFAULT 1, " +
                "kills INTEGER DEFAULT 0, " +
                "PRIMARY KEY (uuid, token_key)" +
                ");";
        // Nueva tabla para el token activo (único por jugador)
        String sql2 = "CREATE TABLE IF NOT EXISTS active_tokens (" +
                "uuid TEXT PRIMARY KEY, " +
                "token_key TEXT NOT NULL, " +
                "level INTEGER DEFAULT 1, " +
                "kills INTEGER DEFAULT 0, " +
                "cooldown_end INTEGER DEFAULT 0" +
                ");";
        try (PreparedStatement stmt1 = connection.prepareStatement(sql1);
             PreparedStatement stmt2 = connection.prepareStatement(sql2)) {
            stmt1.execute();
            stmt2.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Métodos para token_data (ya existentes)
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

    // Nuevos métodos para tokens activos:
    public void setActiveToken(String uuid, String tokenKey, int level, int kills, long cooldownEnd) {
        String sql = "INSERT OR REPLACE INTO active_tokens (uuid, token_key, level, kills, cooldown_end) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.setString(2, tokenKey);
            stmt.setInt(3, level);
            stmt.setInt(4, kills);
            stmt.setLong(5, cooldownEnd);
            stmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ActiveTokenData loadActiveToken(String uuid) {
        String sql = "SELECT token_key, level, kills, cooldown_end FROM active_tokens WHERE uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String tokenKey = rs.getString("token_key");
                    int level = rs.getInt("level");
                    int kills = rs.getInt("kills");
                    long cooldownEnd = rs.getLong("cooldown_end");
                    return new ActiveTokenData(tokenKey, level, kills, cooldownEnd);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void removeActiveToken(String uuid) {
        String sql = "DELETE FROM active_tokens WHERE uuid = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        }
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
}
