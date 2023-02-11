package me.database.learning.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JdbcUserRepositoryTest {

   private JdbcUserRepository jdbcUserRepository;

   private DataSource dataSource;

   @Before
   public void setup() throws SQLException {

      BasicDataSource basicSource = new BasicDataSource();
      basicSource.setDriverClassName("org.h2.Driver");
      basicSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
      basicSource.setUsername("tester");
      basicSource.setPassword("");
      dataSource = basicSource;

      jdbcUserRepository = new JdbcUserRepository(basicSource);

      createTable();
   }

   @After
   public void tearDown() throws SQLException {
      try (Connection conn = dataSource.getConnection();
            Statement statement = conn.createStatement()) {
         statement.executeUpdate("DROP TABLE " + JdbcUserRepository.TABLE_NAME);
      }
   }

   private void createTable() throws SQLException {
      // Get a connection to the database from the DataSource
      try (Connection connection = dataSource.getConnection();
            // Create a Statement object to execute the SQL command
            Statement statement = connection.createStatement()) {

         // Create a StringBuilder to construct the SQL command to create the table
         StringBuilder statementBuilder = new StringBuilder();

         // Start constructing the SQL command
         statementBuilder.append("CREATE TABLE ");
         // Append the name of the table to create
         statementBuilder.append(JdbcUserRepository.TABLE_NAME);
         // Define the columns of the table
         /*
          * the id column is being defined as an IDENTITY column with the following
          * attributes:
          * 
          * IDENTITY: Indicates that this column is an auto-incrementing column, which
          * means that the database will automatically generate a unique value for this
          * column for each new record inserted into the table.
          * 
          * NOT NULL: Specifies that the value in this column must not be NULL. In other
          * words, a value must be present for this column for each record in the table.
          * 
          * PRIMARY KEY: Designates this column as the primary key for the table, which
          * is a unique identifier for each record in the table. The primary key can be
          * used to ensure the integrity of the data in the table and to enforce
          * relationships with other tables.
          */
         statementBuilder.append("(id IDENTITY NOT NULL PRIMARY KEY,");
         statementBuilder.append(" name VARCHAR(255) NOT NULL,");
         statementBuilder.append(" email VARCHAR(255) NOT NULL,");
         statementBuilder.append(" created_at TIMESTAMP NOT NULL");
         // End the definition of the table
         statementBuilder.append(");");

         // Execute the SQL command to create the table
         statement.executeUpdate(statementBuilder.toString());
      }
   }

   @Test
   public void testFindById_UserExists_ShouldReturnUser() throws SQLException {
      // Arrange
      long id = 1;
      String name = "John Doe";
      String email = "john.doe@example.com";
      Timestamp createdAt = new Timestamp(Instant.now().getEpochSecond() * 1000);

      User user = new User(id, name, email, createdAt);

      User updatedUser = jdbcUserRepository.save(user);

      Optional<User> actualUser = jdbcUserRepository.findById(updatedUser.getId());
      // Assert
      assertTrue(actualUser.isPresent());
      assertEquals(updatedUser.getId(), actualUser.get().getId());
      assertEquals(name, actualUser.get().getName());
      assertEquals(email, actualUser.get().getEmail());
      assertEquals(createdAt, actualUser.get().getCreatedAt());
   }

   @Test
   public void testGetConnectionFailure() throws SQLException {
      DataSource dataSource = Mockito.mock(DataSource.class);
      // Mocking the data source to throw an exception when getConnection is called
      when(dataSource.getConnection()).thenThrow(new SQLException("Error obtaining connection"));
      assertFalse(new JdbcUserRepository(dataSource).deleteById(1L));
   }

   @Test
   public void testSave() throws SQLException {

      // Create a test User
      User testUser = new User(1l, "John Doe", "john.doe@example.com",
            new Timestamp(Instant.now().getEpochSecond() * 1000));

      // Call the save method
      User savedUser = jdbcUserRepository.save(testUser);

      // verify a new user was actually returned
      assertNotSame(testUser, savedUser);

      // Verify that the saved user's ID is equal to the test ID
      assertEquals(1l, savedUser.getId());

      // Verify that the saved user's name, email, and createdAt are equal to the test
      // user's values
      assertEquals("John Doe", savedUser.getName());
      assertEquals("john.doe@example.com", savedUser.getEmail());
      assertEquals(testUser.getCreatedAt(), savedUser.getCreatedAt());
   }

}