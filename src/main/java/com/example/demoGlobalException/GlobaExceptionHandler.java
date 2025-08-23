package com.example.demoGlobalException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.demo.Exception.UserServiceException;

@ControllerAdvice
public class GlobaExceptionHandler {

@ExceptionHandler
	public ResponseEntity HandleException (UserServiceException e) {
		return new ResponseEntity (e.getMessage() , HttpStatus.BAD_REQUEST);
		
	}
	
	
	
	
	
	
	
}
