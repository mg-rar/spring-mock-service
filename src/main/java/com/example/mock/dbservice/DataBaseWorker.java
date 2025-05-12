package com.example.mock.dbservice;

import com.example.mock.model.User;

import java.sql.*;
import java.util.NoSuchElementException;

public class DataBaseWorker {

    private static final String URL = "jdbc:postgresql://localhost:5432/users_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "pwd";

    public User selectUserByLogin(String login) throws SQLException, NoSuchElementException {
        String query = "SELECT users.login, users.password, users.date, user_emails.email " +
                "FROM users " +
                "JOIN user_emails ON users.login = user_emails.login " +
                "WHERE users.login = ?";

        User user = new User();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (!resultSet.next()) {
                    throw new NoSuchElementException("User with login '" + login + "' not found");
                }

                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setDate(resultSet.getDate("date"));
                user.setEmail(resultSet.getString("email"));

            }

        } catch (SQLException e) {
            throw new SQLException("Error while fetching user with login '" + login + "': " + e.getMessage(), e);
        }

        return user;
    }

    public int insertUser(User user) throws SQLException {
        final String sql = "INSERT INTO users (login, password, date) VALUES (?, ?, ?);\n" +
                "INSERT INTO user_emails (login, email) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            connection.setAutoCommit(false);

            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.setDate(3, user.getDate());
            statement.setString(4, user.getLogin());
            statement.setString(5, user.getEmail());

            int rows = statement.executeUpdate();

            connection.commit();
            return rows;

        } catch (SQLException e) {
            throw new SQLException("Error while inserting user: " + e.getMessage(), e);
        }
    }
}