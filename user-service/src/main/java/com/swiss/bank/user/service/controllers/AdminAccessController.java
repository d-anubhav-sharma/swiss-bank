package com.swiss.bank.user.service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.Privilege;
import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.RolePrivilegeResponse;
import com.swiss.bank.user.service.models.UserAccessResponse;
import com.swiss.bank.user.service.services.RolePrivilegeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/admin/access")
public class AdminAccessController {

	RolePrivilegeService rolePrivilegeService;
	
	public AdminAccessController(RolePrivilegeService rolePrivilegeService) {
		this.rolePrivilegeService = rolePrivilegeService;
	}
	
	@GetMapping("/roles/all")
	public Flux<RolePrivilegeResponse> findAllRoles(){
		return rolePrivilegeService.findAllRoles();
	}
	
	@GetMapping("/privileges/all")
	public Flux<Privilege> findAllPrivileges(){
		return rolePrivilegeService.findAllPrivileges();
	}
	
	@GetMapping("/users/all")
	public Flux<UserAccessResponse> findAllUsers(){
		return rolePrivilegeService.findAllUsers();
	}
	

	@PostMapping("/users/addRole/{username}/{roleName}")
	public Mono<User> addRoleToUser(@PathVariable String username, @PathVariable String roleName){
		return rolePrivilegeService.addRoleToUser(username, roleName);
	}
	
	@PostMapping("/roles/create/{roleName}")
	public Mono<Role> createRole(@PathVariable String roleName){
		return rolePrivilegeService.createRole(roleName);
	}
	
	@PostMapping("/roles/addPrivilege/{roleName}/{privilegeName}")
	public Mono<Role> addPrivilegeToRole(@PathVariable String roleName, @PathVariable String privilegeName){
		return rolePrivilegeService.addPrivilegeToRole(roleName, privilegeName);
	}

	@PostMapping("/privileges/create/{privilegeName}")
	public Mono<Privilege> createPrivilege(@PathVariable String privilegeName){
		return rolePrivilegeService.createPrivilege(privilegeName);
	}
}
