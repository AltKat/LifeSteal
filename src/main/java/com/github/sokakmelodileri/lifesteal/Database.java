package com.github.sokakmelodileri.lifesteal;

import org.bukkit.entity.Player;

import java.sql.*;

public class Database {
    private final Connection connection;


    public Database(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        try (Statement statement = connection.createStatement()){
            statement.execute("""
                CREATE TABLE IF NOT EXISTS players (
                uuid TEXT PRIMARY KEY,
                username TEXT NOT NULL, 
                health INTEGER NOT NULL DEFAULT 0)
        """);
        }
    }

    public void closeConnection() throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    public void addPlayer(String uuid, String username) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid, username, health) VALUES (?, ?, ?)")){
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, 5);
            preparedStatement.executeUpdate();
        }
    }

    public boolean playerExists(Player player) throws SQLException{
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void updateHealth(Player player, int healths) throws SQLException{
        if(!playerExists(player)){
            addPlayer(player.getUniqueId().toString(), player.getName());
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET health = ? WHERE uuid = ?")){

            preparedStatement.setInt(1, healths);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }
    }

    public int getPlayerHealths(Player player) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT health FROM players WHERE uuid = ?")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("health");
            }else{
                return 0;
            }
        }
    }

}
