package com.swiss.bank.user.service.services;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.LoginRequest;
import com.swiss.bank.user.service.models.RegisterUserRequest;
import com.swiss.bank.user.service.models.RegisterUserResponse;
import com.swiss.bank.user.service.models.UserUpdateRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface UserService {

	public Flux<User> findAllUsers();

    public Mono<User> findUserByUsername(String username);

    public Mono<LoginRequest> encodePasswordForLoginRequest(LoginRequest loginRequest);

    public Mono<User> validateLoginRequest(LoginRequest loginRequest);
    
    public Mono<RegisterUserResponse> registerUser(RegisterUserRequest registerUserRequest);

	public Mono<User> saveUserWithRoles(UserUpdateRequest user);
	
	public Flux<Role> findRoleByUsername(String username);

	public Mono<Role> findRoleByRoleName(String roleName);

	public Flux<SimpleGrantedAuthority> findAuthoritiesForUser(User user);

	public Mono<List<SimpleGrantedAuthority>> findAuthoritiesForUserName(String username);

}
