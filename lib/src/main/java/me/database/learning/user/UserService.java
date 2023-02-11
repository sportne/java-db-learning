package me.database.learning.user;

import java.util.List;
import java.util.Optional;

/**
 * The `UserService` class provides a higher-level interface for managing `User`
 * objects by delegating calls to a `UserRepository` instance.
 */
public class UserService {
   private final UserRepository userRepository;

   /**
    * Constructs a new `UserService` instance.
    *
    * @param userRepository the `UserRepository` instance to delegate calls to
    */
   public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
   }

   /**
    * Deletes a `User` object.
    *
    * @param user the `User` object to delete
    */
   public void delete(User user) {
      userRepository.deleteById(user.getId());
   }

   /**
    * Returns a list of all `User` objects.
    *
    * @return a list of all `User` objects
    */
   public List<User> findAll() {
      return userRepository.findAll();
   }

   /**
    * Finds a `User` by their ID.
    *
    * @param id the ID of the user to find
    * @return an `Optional` containing the found `User`, or an empty `Optional` if
    *         no such user exists
    */
   public Optional<User> findUserById(long id) {
      return userRepository.findById(id);
   }

   /**
    * Saves a new `User` object.
    *
    * @param user the `User` object to save
    * @return the saved `User` object
    */
   public User save(User user) {
      return userRepository.save(user);
   }

   /**
    * Updates an existing `User` object.
    *
    * @param user the `User` object to update
    */
   public void update(User user) {
      userRepository.update(user);
   }

}
