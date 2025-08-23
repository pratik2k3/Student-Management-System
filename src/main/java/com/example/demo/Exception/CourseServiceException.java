package com.example.demo.Exception;

import org.springframework.http.HttpStatus;

public class CourseServiceException extends RuntimeException {
	private String messege;
	private HttpStatus httpStatus;
	
	
	public CourseServiceException (String messege, HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
		this.messege= messege;
	}
	
	
	public String getmessege() {
		return messege;
		
	}
	
	public HttpStatus gethttpStatus () {
		return httpStatus;
	}
		
}
