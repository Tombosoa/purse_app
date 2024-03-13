package com.example.PurseApp;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.*;
    public class DataBaseConnection {
        private final String userName;
        private final String password;
        private final String databaseName;
        private Connection connection;


        public DataBaseConnection(String userName, String password, String databaseName) {
            this.userName = userName;
            this.password = password;
            this.databaseName = databaseName;
            createConnection();
        }

        private void createConnection() {
            try{
                this.connection = DriverManager.getConnection(
                        "jdbc:postgresql://localhost/" + this.databaseName,
                        this.userName,
                        this.password
                );
            }catch (SQLException e){
                e.printStackTrace();
            }
        }

        public Connection getConnection() {
            return connection;
        }
        public Statement prepareStatement(String query) {
            return prepareStatement(query);
        }
    }

