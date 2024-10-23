package com.example.Hospital_Management_System.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.Hospital_Management_System.model.Users;
import com.example.Hospital_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void seedSuperAdmin() {
        try {
            System.out.println("Attempting to seed super admin...");
            long userCount = userRepository.count();
            System.out.println("Current user count: " + userCount);

            if (userCount == 0) {
                Users superAdmin = Users.builder()
                        .email("superadmin@gmail.com")
                        .password(passwordEncoder.encode("superAdmin@"))
                        .build();

                Users savedUser = userRepository.save(superAdmin);
                System.out.println("Super admin created with ID: " + savedUser.getId());
            } else {
                System.out.println("Super admin was not created - users already exist");
            }
        } catch (Exception e) {
            System.err.println("Error seeding super admin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
