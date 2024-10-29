package com.example.Hospital_Management_System.Controller;

import com.example.Hospital_Management_System.Response.AuthResponse;
import com.example.Hospital_Management_System.Response.ErrorResponse;
import com.example.Hospital_Management_System.config.JWSService;
import com.example.Hospital_Management_System.request.AuthRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWSService jwsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        UserDetails userDetails;

        try {
            // Attempt to load the user by username
            userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            // Return a specific message if the user does not exist
            ErrorResponse errorResponse = new ErrorResponse("User not found", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }

        try {
            // Attempt to authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // Handle invalid password
            ErrorResponse errorResponse = new ErrorResponse("Incorrect password", HttpStatus.UNAUTHORIZED.value());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            // Handle other potential exceptions
            ErrorResponse errorResponse = new ErrorResponse("Authentication failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        // If authentication is successful
        final String jwt = jwsService.generateToken(userDetails);
        final String status = determineStatus(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt, status));
    }


    private String determineStatus(UserDetails userDetails) {
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            String role = authority.getAuthority();
            switch (role) {
                case "ROLE_SUPER_ADMIN":
                    return "Super Admin";
                case "ROLE_ADMIN":
                    return "Admin";
                case "ROLE_DOCTOR":
                    return "Doctor";
                case "ROLE_NURSE":
                    return "Nurse";
                case "ROLE_PATIENT":
                    return "Patient";
            }
        }
        return "User"; // Default status
    }
}
