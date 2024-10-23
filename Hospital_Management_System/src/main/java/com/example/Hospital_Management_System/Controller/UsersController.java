package com.example.Hospital_Management_System.Controller;

import com.example.Hospital_Management_System.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/superadmin")
public class UsersController {
    @Autowired
    private UserServices userServices;

    @PostMapping("/seed")
    public ResponseEntity<String> seedUser() {
        userServices.seedSuperAdmin();
        return ResponseEntity.ok("User seeded successfully");
    }
}
