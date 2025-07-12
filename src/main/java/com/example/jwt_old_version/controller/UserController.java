package com.example.jwt_old_version.controller;

import com.example.jwt_old_version.dto.AuthRequest;
import com.example.jwt_old_version.dto.AuthResponse;
import com.example.jwt_old_version.entity.User;
import com.example.jwt_old_version.repository.UserRepository;
import com.example.jwt_old_version.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        Optional<User> user1 = userService.register(user);
        if(!user1.isEmpty()){
//          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists");
            return ResponseEntity.ok(user1.get());
        }
        return ResponseEntity.ok(user1.get());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody AuthRequest authRequest){
        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );
            UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
            String token = "jwt-token-generated";
            return ResponseEntity.ok(new AuthResponse(token));
        }catch (BadCredentialsException e){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Bad credientials"));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> viewUser(@PathVariable Long userId, Authentication authentication){
        // Get currently logged-in username
        String loggedInUsername = authentication.getName();
        User loggedInUserDetails = userRepository.findByUsername(loggedInUsername).get();
        if(Objects.equals(loggedInUserDetails.getId(), userRepository.findById(userId).get().getId()) && Objects.equals(loggedInUserDetails.getRole(), "USER")){
            return ResponseEntity.ok(userRepository.findById(userId).get());
        } else if (Objects.equals(loggedInUserDetails.getRole(), "ADMIN")) {
            return ResponseEntity.ok(userRepository.findById(userId));
        }
        else return ResponseEntity.status(403).body("Forbidden");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId,@RequestBody User updateUser, Authentication authentication){
        // Get currently logged-in username
        String loggedInUsername = authentication.getName();
        User loggedInUserDetails = userRepository.findByUsername(loggedInUsername).get();

        if(Objects.equals(loggedInUserDetails.getId(), userRepository.findById(userId).get().getId()) && Objects.equals(loggedInUserDetails.getRole(), "USER")){
            User userToUpdate = userRepository.findById(userId).get();
            userToUpdate.setUsername(updateUser.getUsername());
            userToUpdate.setPassword(updateUser.getPassword());
            return ResponseEntity.ok(userRepository.save(userToUpdate));

        } else if (Objects.equals(loggedInUserDetails.getRole(), "ADMIN")) {
            User userToUpdate = userRepository.findById(userId).get();
            userToUpdate.setUsername(updateUser.getUsername());
            userToUpdate.setPassword(updateUser.getPassword());
            userToUpdate.setRole(updateUser.getRole());
            return ResponseEntity.ok(userRepository.save(userToUpdate));
        }
        else return ResponseEntity.status(403).body("Forbidden");
    }
}
