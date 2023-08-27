package com.example.pettimer;

import java.sql.*;

public class MyDataBase {

    //static final String DB_USERNAME = "postgres";
    //static final String DB_PASSWORD = "kbndbyjd";
    //static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=PetTimer";

    public Connection connectToDB(String dbname, String user, String pass) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbname, user, pass);
            if (conn != null) {
                System.out.println("Connection Established");
            } else {
                System.out.println("Connection Failed");
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }
    public boolean tryToLogin(String login, String password, Connection cn) throws SQLException {
        Statement statement = cn.createStatement();
        String SQL = "SELECT * FROM users";

        ResultSet result = statement.executeQuery(SQL);
        boolean loggedIn = false;
        while(result.next())
        {
            if(login.equals(result.getString("login"))
                    && password.equals(result.getString("password")))
            {
                System.out.println("logged");
                loggedIn = true;
                break;
            }
        }
        if(!loggedIn)
        {
            System.out.println("wrong log/pass");
            return false;
        }
        else
            return true;
    }
}
