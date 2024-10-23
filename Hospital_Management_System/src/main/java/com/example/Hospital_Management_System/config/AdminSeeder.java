package com.example.Hospital_Management_System.config;

import com.example.Hospital_Management_System.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {
    @Autowired
    private UserServices userServices;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Adminseeder is running");
        userServices.seedSuperAdmin();
    }
}
