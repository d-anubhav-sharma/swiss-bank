package com.swiss.bank.user.service.services;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.Privilege;
import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.RolePrivilegeResponse;
import com.swiss.bank.user.service.models.UserAccessResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface RolePrivilegeService {

	Mono<Role> createRole(String roleName);
	
	Mono<Privilege> createPrivilege(String privilegeName);

	Mono<Role> removePrivilegeFromRole(String roleName, String privilegeName);
	
	Mono<Role> addPrivilegeToRole(String roleName, String privilegeName);

	Flux<RolePrivilegeResponse> findAllRoles();

	Flux<UserAccessResponse> findAllUsers();

	Flux<Privilege> findAllPrivileges();

	Mono<User> addRoleToUser(String username, String roleName);
	
}
