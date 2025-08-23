package com.example.demo.Dto;

import java.time.LocalDate;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class UserProfileUpdateDTO {

	 private Long userId;
	
	    private String username;

	    private String firstName;
	    private String lastName;
	    private String address;

	    private String email;
	    private String phoneNo;
	    private String password;

	    private String role; // admin / student
	    private LocalDate createDate; // signup date

	    private String education;
	    private Integer passoutYear;
	    private String status; // Active / Inactive

	    private String parentContactNo;
	    private Integer age;
	    private String gender;
	    private LocalDate dob;

	    private String aadharCard;
	    private String uanNo; // optional
}
