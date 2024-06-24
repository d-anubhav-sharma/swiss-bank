package com.swiss.bank.user.service.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.UserAccessResponse;
import com.swiss.bank.user.service.services.RolePrivilegeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/admin/access/users")
public class AdminAccessController {

	RolePrivilegeService rolePrivilegeService;
	
	public AdminAccessController(RolePrivilegeService rolePrivilegeService) {
		this.rolePrivilegeService = rolePrivilegeService;
	}
	
	
	@GetMapping("/all")
	public Flux<UserAccessResponse> findAllUsers(){
		return rolePrivilegeService.findAllUsers();
	}
	
	@PostMapping("/addRole/{username}/{roleName}")
	public Mono<User> addRoleToUser(@PathVariable String username, @PathVariable String roleName){
		return rolePrivilegeService.addRoleToUser(username, roleName);
	}
	
	@PostMapping("/addAllRoles/{username}")
	public Mono<User> addAllRolesToUser(@PathVariable String username){
		return rolePrivilegeService.addAllRolesToUser(username);
	}

	@DeleteMapping("/removeRole/{username}/{roleName}")
	public Mono<User> removeRoleFromUser(@PathVariable String username, @PathVariable String roleName){
		return rolePrivilegeService.removeRoleFromUser(username, roleName);
	}

	@DeleteMapping("/removeAllRoles/{username}")
	public Mono<User> removeAllRoles(@PathVariable String username){
		return rolePrivilegeService.removeAllRoles(username);
	}
}
