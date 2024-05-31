package com.swiss.bank.user.service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.swiss.bank.user.service.exceptions.DuplicateUsernameException;
import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.models.ExceptionResponse;
import com.swiss.bank.user.service.util.SwissConstants;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@RestController
@RestControllerAdvice
@Slf4j
public class ExceptionController {

	@ExceptionHandler({WebExchangeBindException.class})
	public ResponseEntity<ExceptionResponse> handleGenericException(WebExchangeBindException exception) {
		String message = exception.getFieldErrors()
				.stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage())
				.reduce("", (error1, error2) -> error1 + "\n" + error2);
		log.atWarn().log("Field validation failed: {}", message);
		return new ResponseEntity<>(ExceptionResponse.builder().message(message).build(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({InvalidUsernamePasswordException.class})
	public ResponseEntity<ExceptionResponse> handleInvalidUsernamePasswordException(InvalidUsernamePasswordException exception) {
		log.atInfo().log("Invalid credentials for user: {}", exception.getLocalizedMessage());
		exception.printStackTrace();
		return new ResponseEntity<>(ExceptionResponse.builder().message(exception.getLocalizedMessage()).build(), HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler({NullPointerException.class})
	public ResponseEntity<ExceptionResponse> handleNullPointerException(NullPointerException exception) {
		log.atError().log(SwissConstants.ERROR_SINGLE_PARAM, exception.getMessage());
		exception.printStackTrace();
		return new ResponseEntity<>(ExceptionResponse.builder().message(exception.getLocalizedMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler({AuthenticationCredentialsNotFoundException.class, ExpiredJwtException.class, DuplicateUsernameException.class})
	public ResponseEntity<ExceptionResponse> handleErrorsAndSendMessage(Exception exception) {
		log.atError().log(SwissConstants.ERROR_SINGLE_PARAM, exception.getMessage());
		exception.printStackTrace();
		return new ResponseEntity<>(ExceptionResponse.builder().message(exception.getLocalizedMessage()).build(), HttpStatus.BAD_REQUEST);
	}
}
