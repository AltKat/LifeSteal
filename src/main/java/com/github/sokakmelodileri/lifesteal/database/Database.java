package com.github.sokakmelodileri.lifesteal.database;

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
                health INTEGER NOT NULL DEFAULT 0,
                isbanned TEXT NOT NULL DEFAULT "false")
        """);
        }
    }

    public void closeConnection() throws SQLException {
        if(connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    public void addPlayer(String uuid, String username) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO players (uuid, username, health) VALUES (?, ?, ?)")){
            preparedStatement.setString(1, uuid);
            preparedStatement.setString(2, username);
            preparedStatement.setInt(3, 5);
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }


    public boolean playerExists(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM players WHERE uuid = ?")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public void updateHealth(Player player, int healths) {
        if(!playerExists(player)){
            addPlayer(player.getUniqueId().toString(), player.getName());
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET health = ? WHERE uuid = ?")){

            preparedStatement.setInt(1, healths);
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void updateHealthbyUsername(String name, int healths) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET health = ? WHERE username = ?")) {
            preparedStatement.setInt(1, healths);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        public int getPlayerHealths(Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT health FROM players WHERE uuid = ?")){
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getInt("health");
            }else{
                return 0;
            }
        }catch (SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    public boolean checkBanned(String nick){
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT isbanned FROM players WHERE username = ?")){
            preparedStatement.setString(1, nick);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                if(resultSet.getString("isbanned").equalsIgnoreCase("true")){
                    return true;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    public void setBanned (Player player) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET isbanned = ? WHERE uuid = ?")) {
            preparedStatement.setString(1, "true");
            preparedStatement.setString(2, player.getUniqueId().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void setUnBanned(String nick){
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE players SET isbanned = ? WHERE username = ?")) {
            preparedStatement.setString(1, "false");
            preparedStatement.setString(2, nick);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    }
