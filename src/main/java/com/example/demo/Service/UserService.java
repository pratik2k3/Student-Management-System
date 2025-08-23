package com.example.demo.Service;

import com.example.demo.Dto.UserProfileUpdateDTO;
import com.example.demo.Dto.UserSignupDTO;
import com.example.demo.Entity.User;

public interface UserService {

	String signup(UserSignupDTO dto);
    String updateProfile(UserProfileUpdateDTO dto);
    public void deleteUserById(Long id);
	User getUserById(Long id);
	User getUserByUsername(String username);
	void deleteUserByUsername(String username);
	
    
    
	
}
