package com.example.demo.Dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import lombok.Data;
@Data
public class UserSignupDTO {

	 @NotBlank(message = "Username is required")
	  
	    private String username;

	    @NotBlank(message = "Password is required")
	    private String password;

	    @Email(message = "Invalid email format")
	    @NotBlank(message = "Email is mandatory")
	    private String email;

	    private String mobileNo;
	
	
	
}
