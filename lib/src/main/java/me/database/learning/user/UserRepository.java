package me.database.learning.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

   /**
    * Finds a user with the given ID.
    *
    * @param id the ID of the user to find.
    * @return an Optional containing the found user, or an empty Optional if no
    *         user was found.
    */
   Optional<User> findById(long id);

   /**
    * Finds all users in the database.
    *
    * @return a List of all found users.
    */
   List<User> findAll();

   /**
    * Saves a new user to the database.
    *
    * @param user the user to save.
    * @return the saved user, with an updated ID if the user was inserted into the
    *         database.
    */
   User save(User user);

   /**
    * Updates an existing user in the database.
    *
    * @param user the user to update.
    */
   void update(User user);

   /**
    * Deletes a user from the database.
    *
    * @param id the ID of the user to delete.
    * @return true if the operation resulted in a change and false if not
    */
   boolean deleteById(long id);
}
