package com.typebaazi;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
@Table(name="typebaazi_users")
public class AppUser {
 @Id public String id;
 @Column(nullable=false, unique=true, length=254) public String email;
 @Column(nullable=false, length=20) public String username;
 @Column(nullable=false) public String passwordHash;
 public Instant createdAt;
 public AppUser() {}
}
