package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Dto.UserProfileUpdateDTO;
import com.example.demo.Dto.UserSignupDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserServiceException;
import com.example.demo.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {
	 @Autowired
	    private UserService userService;

	    @PostMapping("/signup")
	    public String signup(@Valid @RequestBody UserSignupDTO dto) {
	        return userService.signup(dto);
	    }

	    @PutMapping("/update-profile")
	    public String updateProfile(@RequestBody UserProfileUpdateDTO dto) {
	        return userService.updateProfile(dto);
	    }
	
	    @DeleteMapping("/id/{id}")
	    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
	        try {
	            userService.deleteUserById(id);
	            return new ResponseEntity<>("User deleted successfully with ID: " + id, HttpStatus.OK);
	        } catch (UserServiceException e) {
	            return new ResponseEntity<>(e.getMessage(), e.getHttpsStatus());
	        }
	    }
	    
	    @DeleteMapping("/username/{username}")
	    public ResponseEntity<?> deleteUserByUsername(@PathVariable String username) {
	        try {
	            userService.deleteUserByUsername(username);
	            return new ResponseEntity<>("User deleted successfully with username: " + username, HttpStatus.OK);
	        } catch (UserServiceException e) {
	            return new ResponseEntity<>(e.getMessage(), e.getHttpsStatus());
	        }
	    }
	    
	   
	    @GetMapping("/id/{id}")
	    public ResponseEntity<?> getUserById(@PathVariable Long id) {
	        try {
	            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
	        } catch (UserServiceException e) {
	            // Return error message and 404 status instead of 500
	            return new ResponseEntity<>(e.getMessage(), e.getHttpsStatus());
	        }
	    }
	    
	    
	    
	    @GetMapping("/username/{username}")
	    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
	        try {
	            return new ResponseEntity<>(userService.getUserByUsername(username), HttpStatus.OK);
	        } catch (UserServiceException e) {
	            return new ResponseEntity<>(e.getMessage(), e.getHttpsStatus());
	        }  
	    }
	    
	
	
}
