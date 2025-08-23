package com.example.demo.Exception;

import org.springframework.http.HttpStatus;

public class BatchServiceException extends RuntimeException {

	private String messege;
	private HttpStatus httpStatus;
	
	public BatchServiceException(String messege, HttpStatus httpStatus  ) {
		this.httpStatus= httpStatus;
		this.messege=messege;	
	}
	
	public String getmessege () {
		return messege;
	}
	
	public HttpStatus getHttpStatus () {
		return httpStatus  ;
	}
		
}
