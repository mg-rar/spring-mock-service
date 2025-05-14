package com.example.mock.dbservice;

import com.example.mock.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.NoSuchElementException;

@Service
public class DataBaseWorker {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    public User selectUserByLogin(String login) throws SQLException, NoSuchElementException {
        String query = "SELECT users.login, users.password, users.date, user_emails.email " +
                "FROM users " +
                "JOIN user_emails ON users.login = user_emails.login " +
                "WHERE users.login = ?";
        ResultSet resultSet = null;

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, login);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new NoSuchElementException("User with login '" + login + "' not found");
            }

            return new User(
                    resultSet.getString("login"),
                    resultSet.getString("password"),
                    resultSet.getString("email"),
                    resultSet.getDate("date")
            );

        } catch (SQLException e) {
            throw new SQLException("Error while fetching user with login '" + login + "': " + e.getMessage(), e);
        } finally {
            if (resultSet != null)
                resultSet.close();
        }
    }

    public int insertUser(User user) throws SQLException {
        final String sql = "INSERT INTO users (login, password, date) VALUES (?, ?, ?);\n" +
                "INSERT INTO user_emails (login, email) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
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