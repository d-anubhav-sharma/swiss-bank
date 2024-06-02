package com.swiss.bank.user.service.exceptions;

public class InvalidUsernamePasswordException extends RuntimeException {
	private static final long serialVersionUID = 100000000001L;
	
	public InvalidUsernamePasswordException(String message) {
		super(message);
	}
	
	public InvalidUsernamePasswordException() {
		super("");
	}
}
