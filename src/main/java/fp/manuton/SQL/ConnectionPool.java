package fp.manuton.SQL;

import fp.manuton.FarmingPlus;
import org.bukkit.Bukkit;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.HikariPoolMXBean;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Improved database connection management using HikariCP connection pool.
 * This provides:
 * - Thread-safe connection handling
 * - Automatic connection reuse
 * - Connection health checking
 * - Performance optimization
 *
 * Thread Safety: This implementation is thread-safe.
 */
public class ConnectionPool {

    private HikariDataSource dataSource;  // Made non-final to allow null initialization
    private boolean initialized;

    public ConnectionPool(String host, int port, String database, String username, String password) {
        HikariConfig config = new HikariConfig();

        // Build JDBC URL with connection parameters
        String jdbcUrl = String.format(
            "jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
            host, port, database
        );

        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        // Connection pool settings optimized for Minecraft servers
        // Formula: (CPU cores × 2) + 1, but using a reasonable default
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(600000); // 10 minutes
        config.setConnectionTimeout(30000); // 30 seconds
        config.setPoolName("FarmingPlus-Pool");

        // MySQL-specific optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("useLocalSessionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");

        try {
            // initializationFailTimeout = -1 prevents HikariCP from throwing on startup
            // if the DB is unreachable. Instead, it creates the pool and fails on first getConnection().
            config.setInitializationFailTimeout(-1);

            this.dataSource = new HikariDataSource(config);

            // Test connection manually to verify credentials/host
            try (Connection testConn = dataSource.getConnection()) {
                this.initialized = true;
                Bukkit.getLogger().info("[FarmingPlus] Database connection pool initialized successfully.");
                Bukkit.getLogger().info("[FarmingPlus] Pool size: min=" + config.getMinimumIdle() + ", max=" + config.getMaximumPoolSize());
            } catch (SQLException e) {
                this.initialized = false;
                Bukkit.getLogger().severe("[FarmingPlus] Database connection test failed: " + e.getMessage());
                Bukkit.getLogger().severe("[FarmingPlus] Check your MySQL credentials and host in config.yml.");
                // Close the pool since we can't connect
                dataSource.close();
                this.dataSource = null;
            }
        } catch (Exception e) {
            this.initialized = false;
            this.dataSource = null;
            Bukkit.getLogger().severe("[FarmingPlus] Failed to initialize database connection pool: " + e.getMessage());
        }
    }

    /**
     * Get a connection from the pool.
     * IMPORTANT: Always close this connection when done to return it to the pool!
     *
     * @return Connection from pool
     * @throws SQLException if connection cannot be obtained
     */
    public Connection getConnection() throws SQLException {
        if (!initialized) {
            throw new SQLException("Connection pool not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Check if the pool is initialized and can provide connections.
     * This is a basic check - for health checking, use validateConnection()
     *
     * @return true if pool is initialized
     */
    public boolean isInitialized() {
        return initialized && dataSource != null && !dataSource.isClosed();
    }

    /**
     * Validate the connection pool health by attempting a query.
     *
     * @return true if connection pool is healthy
     */
    public boolean validateConnection() {
        if (!initialized) {
            return false;
        }

        try (Connection conn = getConnection()) {
            return MySQLData.isDatabaseConnected(conn);
        } catch (SQLException e) {
            Bukkit.getLogger().warning("[FarmingPlus] Connection validation failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Close the connection pool and release all resources.
     * Should be called when plugin is disabled.
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            try {
                dataSource.close();
                initialized = false;
                Bukkit.getLogger().info("[FarmingPlus] Database connection pool closed.");
            } catch (Exception e) {
                Bukkit.getLogger().severe("[FarmingPlus] Error closing connection pool: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Get statistics about the connection pool for debugging.
     *
     * @return String with pool statistics
     */
    public String getPoolStatistics() {
        if (!initialized || dataSource == null) {
            return "Pool not initialized";
        }

        HikariPoolMXBean pool = dataSource.getHikariPoolMXBean();
        return String.format(
            "Pool Statistics - Active: %d/%d | Idle: %d | Waiting: %d | Total Connections: %d",
            pool.getActiveConnections(),
            pool.getTotalConnections(),
            pool.getIdleConnections(),
            pool.getThreadsAwaitingConnection(),
            pool.getTotalConnections()
        );
    }
}
