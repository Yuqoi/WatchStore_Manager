package org.yuqoi.managerapp.utils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/jewelery_store";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection getConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            if (conn.isValid(5)){
                System.out.println("dzialadsdsds");
                return conn;
            }else {
                conn.close();
                return null;
            }
        } catch (ClassNotFoundException | SQLException e) {

            // TODO: do something when we didnt connect to database

            throw new RuntimeException(e);

        }
    }

}