package com.haider.app.ws.exceptions;

// Custom class to throw exception
// As it's custom so now we can return Custom Json to client  
// unlike a normal exception 

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 7332452572123036052L;

	public UserServiceException(String message) {
		super(message);

	}

}
