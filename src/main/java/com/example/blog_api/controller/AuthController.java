package com.example.blog_api.controller;

import com.example.blog_api.entity.Role;
import com.example.blog_api.entity.User;
import com.example.blog_api.payload.JwtAuthResponse;
import com.example.blog_api.payload.LoginDto;
import com.example.blog_api.payload.SignUpDto;
import com.example.blog_api.repository.RoleRepository;
import com.example.blog_api.repository.UserRepository;
import com.example.blog_api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("signin")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
    @GetMapping("signin")
    public String signIn(){
        return "show sign in form";
    }

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto){
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("User exist by username: " +signUpDto.getUsername(),HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("User exist by email: "+signUpDto.getEmail(),HttpStatus.BAD_REQUEST);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()) );

        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(List.of(roles));

        userRepository.save(user);
        return new ResponseEntity<>("User sign up successfully",HttpStatus.OK);
    }
}
