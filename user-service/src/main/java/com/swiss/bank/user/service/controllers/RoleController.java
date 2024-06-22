package com.swiss.bank.user.service.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.Role;

import reactor.core.publisher.Flux;

@RestController
@RestControllerAdvice
@RequestMapping("/role")
public class RoleController {

	@GetMapping("/getRoles")
	public ResponseEntity<Flux<Role>> findAllRoles(){
		return ResponseEntity.ok().build();	
	}
	
}
