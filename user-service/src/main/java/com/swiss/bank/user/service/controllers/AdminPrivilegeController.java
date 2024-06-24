package com.swiss.bank.user.service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.Privilege;
import com.swiss.bank.user.service.services.RolePrivilegeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/admin/access/privileges")
public class AdminPrivilegeController {

	RolePrivilegeService rolePrivilegeService;
	
	public AdminPrivilegeController(RolePrivilegeService rolePrivilegeService) {
		this.rolePrivilegeService = rolePrivilegeService;
	}
	
	@GetMapping("/all")
	public Flux<Privilege> findAllPrivileges(){
		return rolePrivilegeService.findAllPrivileges();
	}

	@PostMapping("/create/{privilegeName}")
	public Mono<Privilege> createPrivilege(@PathVariable String privilegeName){
		return rolePrivilegeService.createPrivilege(privilegeName);
	}
}
