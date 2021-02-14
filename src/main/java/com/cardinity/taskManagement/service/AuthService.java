package com.cardinity.taskManagement.service;

import com.cardinity.taskManagement.entity.Role;
import com.cardinity.taskManagement.entity.RoleName;
import com.cardinity.taskManagement.entity.User;
import com.cardinity.taskManagement.exception.AppException;
import com.cardinity.taskManagement.exception.BadRequestException;
import com.cardinity.taskManagement.exception.ResourceNotFoundException;
import com.cardinity.taskManagement.model.ApiResponse;
import com.cardinity.taskManagement.model.LoginRequest;
import com.cardinity.taskManagement.model.SignUpRequest;
import com.cardinity.taskManagement.repository.RoleRepository;
import com.cardinity.taskManagement.repository.UserRepository;
import com.cardinity.taskManagement.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider tokenProvider;

    @Transactional
    public User save(SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new BadRequestException("Username is already taken!");
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email Address already in use!");
        }
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() ->
                new ResourceNotFoundException("Role", "role", RoleName.ROLE_USER));
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

    public String authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @PostConstruct
    @Transactional
    public void postConstruct() {
         if(!userRepository.existsByUsername("admin")){
             User user = new User("Admin", "admin", "admin@gmail.com", "sec123");
             user.setPassword(passwordEncoder.encode(user.getPassword()));
             Role userRole = roleRepository.findByName(RoleName.ROLE_ADMIN).orElseThrow(() -> new AppException("User Role not set."));
             user.setRoles(Collections.singleton(userRole));
             userRepository.save(user);
         }

    }
}
