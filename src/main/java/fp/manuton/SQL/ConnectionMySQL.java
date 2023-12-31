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

    public ConnectionMySQL(String host, int port, String database, String username, String password){
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;

        try {
            synchronized(this) {
                if(connection != null && !connection.isClosed())
                    return;

                Class.forName("com.mysql.jdbc.Driver");
                this.connection = DriverManager.getConnection("jdbc:mysql://"+this.host+":"+this.port+"/"+this.database,this.username,this.password);


            }
        }catch(SQLException e) {
            e.printStackTrace();
        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
    }

}
