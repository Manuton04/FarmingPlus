package fp.manuton.SQL;

import fp.manuton.FarmingPlus;
import org.bukkit.Bukkit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for MySQL connection validation
 * NOTE: These tests require a running MySQL instance with proper configuration
 */
public class MySQLConnectionTest {

    private ConnectionMySQL connectionMySQL;
    private Connection connection;

    @BeforeEach
    void setUp() {
        // Only run tests if MySQL is enabled in config
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) {
            Bukkit.getLogger().warning("[MySQLTest] MySQL is not enabled in config. Skipping tests.");
            return;
        }

        connectionMySQL = new ConnectionMySQL(
                FarmingPlus.getPlugin().getMainConfigManager().getMySQLHost(),
                FarmingPlus.getPlugin().getMainConfigManager().getMySQLPort(),
                FarmingPlus.getPlugin().getMainConfigManager().getMySQLDatabase(),
                FarmingPlus.getPlugin().getMainConfigManager().getMySQLUsername(),
                FarmingPlus.getPlugin().getMainConfigManager().getMySQLPassword()
        );

        connection = connectionMySQL.getConnection();
    }

    @Test
    @DisplayName("Test 1: Connection should not be null")
    void testConnectionNotNull() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        assertNotNull(connection, "Connection should not be null after initialization");
        Bukkit.getLogger().info("[MySQLTest] ✓ Connection is not null");
    }

    @Test
    @DisplayName("Test 2: Connection should be marked as connected")
    void testIsConnected() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        assertTrue(connectionMySQL.isConnected(), "Connection should be marked as connected");
        Bukkit.getLogger().info("[MySQLTest] ✓ Connection is marked as connected");
    }

    @Test
    @DisplayName("Test 3: Connection should be able to execute simple query")
    void testCanExecuteQuery() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        try {
            var stmt = connection.createStatement();
            var rs = stmt.executeQuery("SELECT 1");
            assertTrue(rs.next(), "Query should return at least one row");
            assertEquals(1, rs.getInt(1), "Query result should be 1");
            Bukkit.getLogger().info("[MySQLTest] ✓ Connection can execute simple queries");
        } catch (SQLException e) {
            Bukkit.getLogger().log(Level.SEVERE, "[MySQLTest] ✗ Failed to execute query: " + e.getMessage(), e);
            fail("Should be able to execute simple query");
        }
    }

    @Test
    @DisplayName("Test 4: MySQLData.isDatabaseConnected should validate connection")
    void testIsDatabaseConnected() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        assertTrue(
                MySQLData.isDatabaseConnected(connection),
                "MySQLData.isDatabaseConnected should return true for valid connection"
        );
        Bukkit.getLogger().info("[MySQLTest] ✓ MySQLData.isDatabaseConnected validates connection correctly");
    }

    @Test
    @DisplayName("Test 5: Table should be created if not exists")
    void testCreateTableIfNotExists() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        assertDoesNotThrow(() -> {
            MySQLData.createTableIfNotExists(connection);
            // Verify table exists by querying it
            var stmt = connection.createStatement();
            var rs = stmt.executeQuery("SHOW TABLES LIKE 'farmingplus_rewards_table'");
            assertTrue(rs.next(), "Table farmingplus_rewards_table should exist");
        }, "Should create table without errors");

        Bukkit.getLogger().info("[MySQLTest] ✓ Table creation works correctly");
    }

    @Test
    @DisplayName("Test 6: Should be able to insert and query test data")
    void testInsertAndQuery() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        UUID testUUID = UUID.randomUUID();
        String testReward = "test_reward";

        // Clean up any existing test data
        assertDoesNotThrow(() -> {
            MySQLData.deleteRewardsForUUID(connection, testUUID);
        }, "Should be able to delete test data");

        // Insert test data
        assertDoesNotThrow(() -> {
            var record = new fp.manuton.rewardsCounter.RewardRecord(
                    new java.util.Date(),
                    testReward
            );
            MySQLData.saveRewardCounterToDatabase(connection, testUUID, record);
        }, "Should be able to insert test data");

        // Verify UUID exists
        assertTrue(
                MySQLData.existsUUID(connection, testUUID.toString()),
                "UUID should exist after inserting test data"
        );

        // Clean up
        assertDoesNotThrow(() -> {
            MySQLData.deleteRewardsForUUID(connection, testUUID);
        }, "Should be able to clean up test data");

        Bukkit.getLogger().info("[MySQLTest] ✓ Insert and query operations work correctly");
    }

    @Test
    @DisplayName("Test 7: Connection should be closable without errors")
    void testCloseConnection() {
        if (!FarmingPlus.getPlugin().getMainConfigManager().isMySQLEnabled()) return;

        assertDoesNotThrow(() -> {
            connectionMySQL.close();
            assertFalse(
                    connectionMySQL.isConnected(),
                    "Connection should not be marked as connected after close"
            );
        }, "Should be able to close connection without errors");

        Bukkit.getLogger().info("[MySQLTest] ✓ Connection closes correctly");
    }
}
