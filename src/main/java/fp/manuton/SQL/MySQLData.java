package fp.manuton.SQL;

import fp.manuton.FarmingPlus;
import fp.manuton.rewardsCounter.RewardRecord;
import fp.manuton.rewardsCounter.RewardsCounter;
import org.bukkit.Bukkit;

import java.sql.*;
import java.sql.Date;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MySQLData {

    private static final String REWARDS_TABLE = "farmingplus_rewards_table";
    // Thread-safe DateTimeFormatter (Java 8+) replaces non-thread-safe SimpleDateFormat
    private static final DateTimeFormatter ISO_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static boolean isDatabaseConnected(Connection connection) {
        if (connection == null)
            return false;
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT 1");
            return true;
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[FarmingPlus] Error checking database connection: " + e.getMessage());
            return false;
        }
    }

    public static boolean existsUUID(Connection connection, String uuid) {
        String query = "SELECT COUNT(*) FROM " + REWARDS_TABLE + " WHERE uuid = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[FarmingPlus] Error checking if UUID exists in database: " + e.getMessage());
        }
        return false;
    }

    public static void saveRewardsCounterToDatabase(Connection connection, Map<UUID, RewardRecord> rewardsMap) {
        if (rewardsMap == null || rewardsMap.isEmpty()) {
            return;
        }

        createTableIfNotExists(connection);
        String query = "INSERT INTO " + REWARDS_TABLE + " (uuid, Date, rewardName) VALUES (?, ?, ?)";

        try {
            // Deshabilitar auto-commit para usar transacción
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                for (Map.Entry<UUID, RewardRecord> entry : rewardsMap.entrySet()) {
                    // Use thread-safe DateTimeFormatter to convert Date to ISO format
                    String isoDate = entry.getValue().getDate()
                        .toInstant()
                        .atZone(ZoneId.systemDefault())
                        .format(ISO_FORMATTER);
                    statement.setString(1, entry.getKey().toString());
                    statement.setString(2, isoDate);
                    statement.setString(3, entry.getValue().getRewardName());
                    statement.addBatch(); // Agregar al batch en lugar de ejecutar uno por uno
                }

                // Ejecutar todos los inserts en una sola operación (MUCHO más eficiente)
                int[] results = statement.executeBatch();

                // Commit de la transacción
                connection.commit();
                Bukkit.getLogger().info("[FarmingPlus] Successfully saved " + results.length + " reward records to database.");
            }
        } catch (SQLException e) {
            try {
                // Rollback en caso de error
                connection.rollback();
                Bukkit.getLogger().severe("[FarmingPlus] Error saving rewards counter to database: " + e.getMessage());
            } catch (SQLException rollbackException) {
                Bukkit.getLogger().severe("[FarmingPlus] Error rolling back transaction: " + rollbackException.getMessage());
            }
        } finally {
            try {
                // Restaurar auto-commit
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Bukkit.getLogger().severe("[FarmingPlus] Error restoring auto-commit: " + e.getMessage());
            }
        }
    }

    public static void saveRewardCounterToDatabase(Connection connection, UUID uuid, RewardRecord rewardRecord) {
        createTableIfNotExists(connection);
        String query = "INSERT INTO " + REWARDS_TABLE + " (uuid, Date, rewardName) VALUES (?, ?, ?)";
        // Use thread-safe DateTimeFormatter to convert Date to ISO format
        String isoDate = rewardRecord.getDate()
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .format(ISO_FORMATTER);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            statement.setString(2, isoDate);
            statement.setString(3, rewardRecord.getRewardName());
            statement.executeUpdate();
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[FarmingPlus] Error saving reward counter to database: " + e.getMessage());
        }
    }

    public static Map<UUID, RewardsCounter> loadRewardsFromDatabase(Connection connection) {
        // IMPORTANT: Create table first if it doesn't exist
        createTableIfNotExists(connection);

        String query = "SELECT uuid, Date, rewardName FROM " + REWARDS_TABLE + " ORDER BY Date DESC";
        Map<UUID, RewardsCounter> rewardsMap = new HashMap<>();

        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(query)) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                    Date date = new Date(resultSet.getTimestamp("Date").getTime());
                    String rewardName = resultSet.getString("rewardName");

                    RewardRecord rewardRecord = new RewardRecord(date, rewardName);

                    if (!rewardsMap.containsKey(uuid)) {
                        rewardsMap.put(uuid, new RewardsCounter(new ArrayList<>()));
                    }

                    rewardsMap.get(uuid).getRecord().add(rewardRecord);
                }
                Bukkit.getLogger().info("[FarmingPlus] Loaded " + rewardsMap.size() + " players' rewards from database.");
            }
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[FarmingPlus] Error loading rewards from database: " + e.getMessage());
        }

        return rewardsMap;
    }

    public static void deleteRewardsForUUID(Connection connection, UUID uuid) {
        String query = "DELETE FROM " + REWARDS_TABLE + " WHERE uuid = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, uuid.toString());
            int deletedRows = statement.executeUpdate();
            Bukkit.getLogger().info("[FarmingPlus] Deleted " + deletedRows + " reward records for UUID: " + uuid);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[FarmingPlus] Error deleting rewards for UUID: " + e.getMessage());
        }
    }

    public static void createTableIfNotExists(Connection connection) {
        String query = "CREATE TABLE IF NOT EXISTS " + REWARDS_TABLE + " (" +
                       "uuid VARCHAR(36), " +
                       "`Date` TIMESTAMP, " +  // Backticks needed, 'Date' is a reserved keyword in MySQL
                       "rewardName VARCHAR(255))";

        try (Statement statement = connection.createStatement()) {
            statement.execute(query);

            // Crear índice en UUID para búsquedas ultra-rápidas
            String indexQuery = "CREATE INDEX IF NOT EXISTS idx_uuid ON " + REWARDS_TABLE + " (uuid)";
            statement.execute(indexQuery);
        } catch (SQLException e) {
            Bukkit.getLogger().severe("[FarmingPlus] Error creating rewards table: " + e.getMessage());
        }
    }

}
