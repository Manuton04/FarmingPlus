package fp.manuton.SQL;

import fp.manuton.FarmingPlus;
import fp.manuton.utils.MessageUtils;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionMySQL {

    private Connection connection;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private boolean connectionValid;

    public ConnectionMySQL(String host, int port, String database, String username, String password){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.connectionValid = false;

        connect();
    }

    private void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                this.username,
                this.password
            );
            this.connectionValid = true;
        }catch(SQLException e) {
            this.connectionValid = false;
            Bukkit.getLogger().severe("[FarmingPlus] SQL error while connecting to database: " + e.getMessage());
            e.printStackTrace();
        }catch(ClassNotFoundException e) {
            this.connectionValid = false;
            Bukkit.getLogger().severe("[FarmingPlus] MySQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connectionValid && connection != null;
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                this.connectionValid = false;
            } catch (SQLException e) {
                this.connectionValid = false;
                Bukkit.getLogger().severe("[FarmingPlus] Error closing database connection: " + e.getMessage());
            }
        }
    }

}
