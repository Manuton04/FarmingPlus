package fp.manuton.SQL;

import fp.manuton.FarmingPlus;
import fp.manuton.rewardsCounter.RewardRecord;
import fp.manuton.rewardsCounter.RewardsCounter;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

public class MySQLData {

    public static boolean isDatabaseConnected(Connection connection) {
        if (connection == null)
            return false;
        try {
            Statement statement = connection.createStatement();
            statement.executeQuery("SELECT 1");
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public static boolean existsUUID(Connection connection, String uuid) {
        String query = "SELECT * FROM rewards_counter_table WHERE uuid = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
        }
        return false;
    }

    public static void saveRewardsCounterToDatabase(Connection connection, Map<UUID, RewardRecord> rewardsMap) {
        createTableIfNotExists(connection);
        String query = "INSERT INTO rewards_table (uuid, Date, rewardName) VALUES (?, ?, ?)";
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Map.Entry<UUID, RewardRecord> entry : rewardsMap.entrySet()) {
            String isoDate = isoFormat.format(entry.getValue().getDate());

            try {
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, entry.getKey().toString());
                statement.setString(2, isoDate);
                statement.setString(3, entry.getValue().getRewardName());
                statement.executeUpdate();
            } catch (SQLException e) {

            }
        }
    }

    public static void saveRewardCounterToDatabase(Connection connection, UUID uuid, RewardRecord rewardRecord) {
        createTableIfNotExists(connection);
        String query = "INSERT INTO rewards_table (uuid, Date, rewardName) VALUES (?, ?, ?)";
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String isoDate = isoFormat.format(rewardRecord.getDate());

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            statement.setString(2, isoDate);
            statement.setString(3, rewardRecord.getRewardName());
            statement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public static Map<UUID, RewardsCounter> loadRewardsFromDatabase(Connection connection) {
        String query = "SELECT * FROM rewards_table";
        Map<UUID, RewardsCounter> rewardsMap = new HashMap<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

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
        } catch (SQLException e) {

        }

        return rewardsMap;
    }

    public static void deleteRewardsForUUID(Connection connection, UUID uuid) {
        String query = "DELETE FROM rewards_table WHERE uuid = ?";
    
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
        } catch (SQLException e) {

        }
    }

    public static void createTableIfNotExists(Connection connection) {
        String query = "CREATE TABLE IF NOT EXISTS rewards_table (" +
                       "uuid VARCHAR(36), " +
                       "Date TIMESTAMP, " +
                       "rewardName VARCHAR(255))";
    
        try {
            Statement statement = connection.createStatement();
            statement.execute(query);
        } catch (SQLException e) {

        }
    }

}
