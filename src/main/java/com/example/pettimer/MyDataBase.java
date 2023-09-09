package com.example.pettimer;

import java.sql.*;
import java.util.HashMap;

public class MyDataBase {

    //static final String DB_USERNAME = "postgres";
    //static final String DB_PASSWORD = "kbndbyjd";
    //static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres?currentSchema=PetTimer";

    private String UserId;
    private String UserName;

    private HashMap<String, String> userActivities;

    private Connection connection;

    public MyDataBase(String dbname, String user, String pass){
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
        connection = conn;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public Connection getConnection() {
        return connection;
    }

    public HashMap<String, String> getUserActivities() {
        return userActivities;
    }

    public boolean tryToLogin(String login, String password, Connection cn) throws SQLException {
        Statement statement = cn.createStatement();
        String SQL = "SELECT * FROM users";

        ResultSet result = statement.executeQuery(SQL);
        boolean loggedIn = true;
        while(result.next())
        {
            if(login.equals(result.getString("login"))
                    && password.equals(result.getString("password")))
            {
                System.out.println("logged");
                loggedIn = false;

                UserId = result.getString("id");
                UserName = result.getString("login");

                break;
            }
        }
        if(loggedIn)
        {
            System.out.println("wrong login or password");
            return false;
        }
        else
            return true;
    }

    public void tryToCreateAcc(String log, String pass, Connection cn)  throws SQLException {
        Statement statement = cn.createStatement();
        String SQL = "SELECT * FROM users";

        ResultSet result = statement.executeQuery(SQL);
        boolean created = true;
        while(result.next())
        {
            if(log.equals(result.getString("login")))
            {
                System.out.println("Such login already exists");
                created = false;
                break;
            }
        }
        if(created)
        {
            System.out.println("User was created");
            statement = cn.createStatement();
            SQL = "INSERT INTO users  (login, password) values ('" + log + "', '" + pass + "')";

            statement.executeQuery(SQL);
        }
    }

    public HashMap<String, String> getAllUserActivities()throws SQLException{

        HashMap<String, String> activityNames = new HashMap<>();

        Statement statement = connection.createStatement();
        String SQL = "SELECT a.id, a.name FROM activities as a " +
                "inner join users as u ON u.id = a.user_id " +
                "where u.id = " + UserId + ";";

        ResultSet result = statement.executeQuery(SQL);

        while(result.next())
        {
            activityNames.put(result.getString("id"), result.getString("name"));
        }
        return activityNames;
    }

    public void endActivity(String log, String pass, Connection cn)  throws SQLException{

    }

    public String getActNameByid(String id) throws SQLException {
        Statement statement = connection.createStatement();
        String SQL = "SELECT a.id, a.name FROM activities as a " +
                "inner join users as u ON u.id = a.user_id " +
                "where u.id = " + UserId + ";";

        ResultSet result = statement.executeQuery(SQL);

        return result.getString("name");
    }

    public void updateSumTimeOfActivity(String id, int sec) throws SQLException{
        try {
            Statement statement = connection.createStatement();
            String SQL = "update activities set totalseconds = totalseconds + " + sec + " where id = " + id;

            statement.executeQuery(SQL);
        }
        catch (Exception e){

        }

    }
}
