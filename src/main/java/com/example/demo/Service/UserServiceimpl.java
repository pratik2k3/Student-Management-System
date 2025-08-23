package com.example.demo.Service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.demo.Dto.UserProfileUpdateDTO;
import com.example.demo.Dto.UserSignupDTO;
import com.example.demo.Entity.User;
import com.example.demo.Exception.UserServiceException;
import com.example.demo.Repository.UserRepository;
@Service
public class UserServiceimpl implements UserService  {
	@Autowired
    private UserRepository userRepository;
	@Override
	public String signup(UserSignupDTO dto) {
	    if (userRepository.existsByEmail(dto.getEmail())) {
	        return "Email already exists!";
	    }
	    if (userRepository.existsByUsername(dto.getUsername())) {
	        return "Username already exists!";
	    }

	    User user = new User();
	    user.setUsername(dto.getUsername());
	    user.setPassword(dto.getPassword());
	    user.setEmail(dto.getEmail());
	    user.setMobileNo(dto.getMobileNo());
	    user.setCreateDate(LocalDate.now());

	    userRepository.save(user); // ✅ FIXED
	    return "Signup successful!";
	}

	@Override
	public String updateProfile(UserProfileUpdateDTO dto) {
	    Optional<User> optionalUser = userRepository.findById(dto.getUserId());
	    if (optionalUser.isEmpty()) {
	        return "User not found!";
	    }

	    User user = optionalUser.get();

	    // ✅ Check if username belongs to someone else
	    Optional<User> existingUserWithSameUsername = userRepository.findByUsername(dto.getUsername());
	    if (existingUserWithSameUsername.isPresent() && !existingUserWithSameUsername.get().getUserId().equals(dto.getUserId())) {
	        return "Username already exists!";
	    }

	    // Now safe to update
	    user.setUsername(dto.getUsername());
	    user.setFirstName(dto.getFirstName());
	    user.setLastName(dto.getLastName());
	    user.setAddress(dto.getAddress());
	    user.setEmail(dto.getEmail());
	    user.setPhoneNo(dto.getPhoneNo());
	    user.setPassword(dto.getPassword());
	    user.setRole(dto.getRole());
	    user.setCreateDate(dto.getCreateDate());
	    user.setEducation(dto.getEducation());
	    user.setPassoutYear(dto.getPassoutYear());
	    user.setStatus(dto.getStatus());
	    user.setParentContactNo(dto.getParentContactNo());
	    user.setAge(dto.getAge());
	    user.setGender(dto.getGender());
	    user.setDob(dto.getDob());
	    user.setAadharCard(dto.getAadharCard());
	    user.setUanNo(dto.getUanNo());

	    userRepository.save(user);
	    return "Profile updated successfully!";
	}

	
	public void deleteUserById(Long id) throws UserServiceException {
	    Optional<User> user = userRepository.findById(id); // check if exists
	    if (!user.isPresent()) {
	        throw new UserServiceException("User not found with ID: " + id, HttpStatus.NOT_FOUND);
	    }
	    userRepository.deleteById(id); // delete if exists
	}
	
	public void deleteUserByUsername(String username) throws UserServiceException {
	    Optional<User> user = userRepository.findByUsername(username); // check if exists
	    if (!user.isPresent()) {
	        throw new UserServiceException("User not found with username: " + username,HttpStatus.NOT_FOUND);
	    }
	    userRepository.delete(user.get()); // delete if exists
	}
	
	

	public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new UserServiceException("User not found with ID: " + id , HttpStatus.NOT_FOUND);
        }
        return user.get();
    }

	public User getUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UserServiceException("User not found with username: " + username  ,  HttpStatus.NOT_FOUND);
        }
        return user.get();
    }
	
}
