package com.sreekar.batchfetch.dao;


import com.sreekar.batchfetch.Util.ReadPropertiesFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnection {

    private volatile static Connection con;
    private static MySqlConnection mySqlConnection;


    private MySqlConnection() throws IOException, SQLException {
        ReadPropertiesFile readPropertiesFile = ReadPropertiesFile.getInstance();
        con = DriverManager.getConnection(readPropertiesFile.getProperty("DB_URL"),
                  readPropertiesFile.getProperty("DB_USERNAME"),
                  readPropertiesFile.getProperty("DB_PASSWORD"));
    }


    public static MySqlConnection getInstance() throws IOException, SQLException {
        synchronized (MySqlConnection.class){
            if(mySqlConnection==null){
                mySqlConnection = new MySqlConnection();
            }
        }
        return mySqlConnection;
    }

    public Connection getConnection(){
        return con;
    }
}
