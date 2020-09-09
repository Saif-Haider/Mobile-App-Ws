package com.haider.app.ws.exceptions;

// Custom class to throw exception

public class UserServiceException extends RuntimeException {

	private static final long serialVersionUID = 7332452572123036052L;

	public UserServiceException(String message) {
		super(message);

	}

}
