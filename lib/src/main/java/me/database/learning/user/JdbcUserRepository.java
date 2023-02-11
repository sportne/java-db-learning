package me.database.learning.user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

/**
 * JdbcUserRepository is a concrete implementation of the UserRepository
 * interface that uses a JDBC DataSource to interact with a database.
 */
public class JdbcUserRepository implements UserRepository {

   public static String TABLE_NAME = "users";

   private final DataSource dataSource;

   /**
    * Creates a new instance of JdbcUserRepository.
    *
    * @param dataSource the JDBC DataSource to use for interacting with the
    *                   database.
    */
   public JdbcUserRepository(DataSource dataSource) {
      this.dataSource = dataSource;
   }

   @Override
   public Optional<User> findById(long id) {
      // The query "SELECT * FROM users WHERE id = ?" is a prepared statement that
      // retrieves all columns of the "users" table where the "id" column is equal to
      // the given id. The "?" is a placeholder that will be filled with the actual id
      // value at runtime.
      try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection
                  .prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE id = ?")) {
         // Setting the first parameter of the prepared statement to the id provided as
         // argument to the method
         statement.setLong(1, id);

         try (ResultSet resultSet = statement.executeQuery()) {
            // If there's a result, create a User object with the values from the result set
            if (resultSet.next()) {
               String name = resultSet.getString("name");
               String email = resultSet.getString("email");
               Timestamp createdAt = resultSet.getTimestamp("created_at");
               User user = new User(id, name, email, createdAt);
               return Optional.of(user);
            } else {
               // If there's no result, return an empty Optional
               return Optional.empty();
            }
         }
      } catch (SQLException e) {
         // Handle the exception
      }
      // If an exception occurs, return an empty Optional
      return Optional.empty();
   }

   @Override
   public List<User> findAll() {
      List<User> users = new ArrayList<>();
      try (Connection connection = dataSource.getConnection();
            // Create a Statement object to execute a SQL query
            Statement statement = connection.createStatement();
            // Execute a SQL SELECT statement to retrieve all data from the "users" table
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + TABLE_NAME)) {
         // Iterate through the result set and create User objects for each row of data
         while (resultSet.next()) {
            long id = resultSet.getLong("id");
            String name = resultSet.getString("name");
            String email = resultSet.getString("email");
            Timestamp createdAt = resultSet.getTimestamp("created_at");
            User user = new User(id, name, email, createdAt);
            users.add(user);
         }
      } catch (SQLException e) {
         // Handle the exception
      }
      return users;
   }

   @Override
   public User save(User user) {
      // The prepared statement string is used to insert data into the "users" table.
      // It contains placeholders for the user's name, email, and created date which
      // will be filled in using setString and setDate methods. The "?" characters in
      // the string represent these placeholders.
      try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                  "INSERT INTO " + TABLE_NAME + " (name, email, created_at) VALUES (?, ?, ?)",
                  Statement.RETURN_GENERATED_KEYS)) {
         // Set the values for the three columns in the users table: name, email, and
         // created_at
         statement.setString(1, user.getName());
         statement.setString(2, user.getEmail());
         statement.setTimestamp(3, new Timestamp(user.getCreatedAt().getTime()));

         // Execute the statement and get the number of affected rows
         int affectedRows = statement.executeUpdate();

         // If the number of affected rows is 0, throw an exception
         if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
         }

         // Get the generated keys from the statement, which will contain the ID of the
         // inserted row
         try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

            // If the generated keys result set contains a row, get the ID and create a new
            // User object with it
            if (generatedKeys.next()) {
               long id = generatedKeys.getLong(1);
               User savedUser = new User(id, user.getName(), user.getEmail(), user.getCreatedAt());
               return savedUser;
            } else {
               // If the generated keys result set doesn't contain a row, throw an exception
               throw new SQLException("Creating user failed, no ID obtained.");
            }
         }
      } catch (SQLException e) {
         // Handle the exception
         System.out.println(e.getMessage());
      }
      return user;
   }

   @Override
   public void update(User user) {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection
                  .prepareStatement("UPDATE users SET name = ?, email = ? WHERE id = ?")) {
         // Set the values for the first two parameters of the UPDATE statement
         statement.setString(1, user.getName());
         statement.setString(2, user.getEmail());
         // Set the value for the third parameter (id) of the UPDATE statement
         statement.setLong(3, user.getId());
         // Execute the UPDATE statement and retrieve the number of affected rows
         int affectedRows = statement.executeUpdate();
         // If no rows were affected, throw an exception
         if (affectedRows == 0) {
            throw new SQLException("Updating user failed, no rows affected.");
         }
      } catch (SQLException e) {
         // Handle the exception
      }
   }

   @Override
   public boolean deleteById(long id) {
      try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection
                  .prepareStatement("DELETE FROM " + TABLE_NAME + " WHERE id = ?")) {
         // Set the first (and only) parameter in the prepared statement to the id of the
         // user to be deleted
         statement.setLong(1, id);
         // Execute the delete statement and get the number of affected rows
         int affectedRows = statement.executeUpdate();
         // If no rows were affected, it means the delete failed
         return affectedRows != 0;
      } catch (SQLException e) {
         return false;
      }
   }

}
