package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JPA Repository 사용 가능하다.
// JpaRepository 상속하여 @Repository 사용하지 않아도 IOC 가능하다.
public interface UserRepository extends JpaRepository<User, Integer> {
    // findBy 규칙
    // SELECT * FROM USER WHERE username = ?
    public User findByUsername(String username);
}
