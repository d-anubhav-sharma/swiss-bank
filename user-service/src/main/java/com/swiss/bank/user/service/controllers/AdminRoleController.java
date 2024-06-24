package com.swiss.bank.user.service.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.models.RolePrivilegeResponse;
import com.swiss.bank.user.service.services.RolePrivilegeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RestControllerAdvice
@RequestMapping("/admin/access/roles")
public class AdminRoleController {

	RolePrivilegeService rolePrivilegeService;

	public AdminRoleController(RolePrivilegeService rolePrivilegeService) {
		this.rolePrivilegeService = rolePrivilegeService;
	}

	@GetMapping("/all")
	public Flux<RolePrivilegeResponse> findAllRoles() {
		return rolePrivilegeService.findAllRoles();
	}

	@PostMapping("/create/{roleName}")
	public Mono<Role> createRole(@PathVariable String roleName) {
		return rolePrivilegeService.createRole(roleName);
	}

	@PostMapping("/addPrivilege/{roleName}/{privilegeName}")
	public Mono<Role> addPrivilegeToRole(@PathVariable String roleName, @PathVariable String privilegeName) {
		return rolePrivilegeService.addPrivilegeToRole(roleName, privilegeName);
	}

	@PostMapping("/addAllPrivileges/{roleName}")
	public Mono<Role> addAllPrivilegesToRole(@PathVariable String roleName) {
		return rolePrivilegeService.addAllPrivilegesToRole(roleName);
	}

	@DeleteMapping("/removePrivilege/{roleName}/{privilegeName}")
	public Mono<Role> removePrivilegeFromRole(@PathVariable String roleName, @PathVariable String privilegeName) {
		return rolePrivilegeService.removePrivilegeFromRole(roleName, privilegeName);
	}

	@DeleteMapping("/removeAllPriviliges/{roleName}")
	public Mono<Role> removeAllPriviligesFromRole(@PathVariable String roleName) {
		return rolePrivilegeService.removeAllPriviliges(roleName);
	}
}
