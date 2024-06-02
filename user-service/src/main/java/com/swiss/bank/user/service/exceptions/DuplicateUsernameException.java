package com.swiss.bank.user.service.exceptions;

public class DuplicateUsernameException extends Exception{
	private static final long serialVersionUID = -5346045417567400234L;
	
	public DuplicateUsernameException(String message) {
		super(message);
	}

}
