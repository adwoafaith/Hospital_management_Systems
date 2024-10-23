package com.example.Hospital_Management_System.repository;

import com.example.Hospital_Management_System.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByEmail(String email);
}
