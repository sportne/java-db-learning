package me.database.learning.user;

import java.sql.Timestamp;
import java.time.Instant;

public class User {
   private long id;
   private String name;
   private String email;
   private Timestamp createdAt;

   public User(String name, String email) {
      this(0, name, email);
   }

   public User(long id, String name, String email) {
      this(id, name, email, new Timestamp(Instant.now().getEpochSecond() * 1000));
   }

   public User(long id, String name, String email, Timestamp createdAt) {
      this.id = id;
      this.name = name;
      this.email = email;
      this.createdAt = createdAt;
   }

   public long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public String getEmail() {
      return email;
   }

   public Timestamp getCreatedAt() {
      return createdAt;
   }
}
