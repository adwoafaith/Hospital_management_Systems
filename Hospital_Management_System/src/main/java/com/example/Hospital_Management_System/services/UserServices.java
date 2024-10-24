package com.example.Hospital_Management_System.services;

import com.example.Hospital_Management_System.model.Role;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.Hospital_Management_System.model.Users;
import com.example.Hospital_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired
    private UserRepository userRepository;
    private String email = "superadmin@gmail.com";
    private String password = "superAdmin@";


    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

    public void seedSuperAdmin() {
        try {
            System.out.println("Attempting to seed super admin...");
            long userCount = userRepository.count();
            System.out.println("Current user count: " + userCount);

            //checking if a super admin already exist
            if (userCount == 0) {
                Users superAdmin = Users.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .role(Role.SUPER_ADMIN)
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

    public String addAdmin(String adminEmail){

        if (userRepository.existsByEmail(adminEmail)){
            return "User with email" + " " + adminEmail + " " + "already exists";
        }
        String defaultPassword = generateRandomPassword();
        Users admin = Users.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode(defaultPassword))
                .role(Role.ADMIN)
                .build();
        userRepository.save(admin);
        try {
            emailService.sendEmail(adminEmail, "Admin Account Created", adminEmail, adminEmail, defaultPassword);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to admin: " + e.getMessage());
        }
        return "Admin added successfully";
    }

    public String addUser(String userEmail, Role role){
        if (userRepository.existsByEmail(userEmail)){
            return "User with email "+ userEmail + " already exists";
        }
        String defaultUserPassword = generateRandomPassword();

        Users user = Users.builder()
                .email(userEmail)
                .password(passwordEncoder.encode(defaultUserPassword))
                .role(role)
                .build();

        userRepository.save(user);

        // Send email with login credentials
        try {
            emailService.sendEmail(userEmail, "User Account Created", userEmail, userEmail, defaultUserPassword);
        } catch (MessagingException e) {
            System.err.println("Failed to send email to user: " + e.getMessage());
        }
        return "User added successfully";
    }

    private  String generateRandomPassword(){
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
