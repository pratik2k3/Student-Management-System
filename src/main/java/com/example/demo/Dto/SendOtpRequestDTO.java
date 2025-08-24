package com.example.demo.Dto;

import com.example.demo.utility.OtpPurpose;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SendOtpRequestDTO {
	 @Email(message = "Invalid email format")
	    @NotBlank(message = "Email is mandatory")
	    private String email;

	    @NotBlank(message = "Purpose is mandatory")
	    private OtpPurpose purpose;
}
