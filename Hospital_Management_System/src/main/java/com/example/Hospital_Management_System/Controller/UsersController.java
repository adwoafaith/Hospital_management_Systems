package com.example.Hospital_Management_System.Controller;

import com.example.Hospital_Management_System.model.Role;
import com.example.Hospital_Management_System.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UsersController {

    @Autowired
    private UserServices userServices;

    @PostMapping("add/superadmin/seed")
    public ResponseEntity<String> seedUser() {
        userServices.seedSuperAdmin();
        return ResponseEntity.ok("User seeded successfully");
    }

    @PostMapping("add/addAdmin")
    public ResponseEntity<String> addAdmin(@RequestParam String email) {
        String result = userServices.addAdmin(email);
        return ResponseEntity.ok(result);
    }


    @PostMapping("add/addUser")
    public ResponseEntity<String> addUser(@RequestParam String email, @RequestParam Role role) {
        String result = userServices.addUser(email, role);
        return ResponseEntity.ok(result);
    }
}