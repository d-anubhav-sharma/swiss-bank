package com.swiss.bank.user.service.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swiss.bank.common.utils.DataUtil;
import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.models.LoginRequest;
import com.swiss.bank.user.service.models.RegisterUserRequest;
import com.swiss.bank.user.service.models.RegisterUserResponse;
import com.swiss.bank.user.service.models.UserUpdateRequest;
import com.swiss.bank.user.service.repositories.PrivilegeRepository;
import com.swiss.bank.user.service.repositories.RoleRepository;
import com.swiss.bank.user.service.repositories.UserRepository;
import com.swiss.bank.user.service.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	PasswordEncoder encoder;
	RoleRepository roleRepository;
	UserRepository userRepository;
	PrivilegeRepository privilegeRepository;

	public UserServiceImpl(
			PasswordEncoder passwordEncoder, 
			UserRepository userRepository, 
			RoleRepository roleRepository,
			PrivilegeRepository privilegeRepository) {
		
		this.encoder = passwordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	public Flux<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Mono<User> findUserByUsername(String username) {
		return userRepository.findUserByUsername(username);
	}

	@Override
	public Mono<LoginRequest> encodePasswordForLoginRequest(LoginRequest request) {
		return Mono.just(LoginRequest.builder().username(request.getUsername()).password(encoder.encode(request.getPassword())).build());
	}

	@Override
	public Mono<User> validateLoginRequest(LoginRequest loginRequest) {
		return userRepository.findUserByUsername(loginRequest.getUsername())
				.switchIfEmpty(Mono.error(new InvalidUsernamePasswordException("Invalida username/password exception")))
				.filter(user -> encoder.matches(loginRequest.getPassword(), user.getPassword()))
				.switchIfEmpty(Mono.error(new InvalidUsernamePasswordException("invalid username/password: " + loginRequest.getUsername())));
	}

	@Override
	public Mono<RegisterUserResponse> registerUser(RegisterUserRequest registerUserRequest) {
		return userRepository.save(User.builder()
				.username(registerUserRequest.getUsername())
				.email(registerUserRequest.getEmail())
				.roleIds(new ArrayList<>())
				.password(encoder.encode(registerUserRequest.getPassword()))
				.build()).map(user -> RegisterUserResponse.builder().username(user.getUsername()).build());
	}

	@Override
	public Mono<User> saveUserWithRoles(UserUpdateRequest updateRequest) {
		Mono<User> userMono = userRepository
				.findById(DataUtil.getOrDefault(updateRequest.getId(), ""))
				.switchIfEmpty(userRepository.findUserByUsername(updateRequest.getUsername()))
				.defaultIfEmpty(new User());

		Mono<List<String>> roleIds = Flux
				.fromIterable(updateRequest.getRoles())
				.flatMap(role -> roleRepository.findRoleByRoleName(role.getRoleName()))
				.map(Role::getId)
				.collectList();

		return userMono.zipWith(roleIds, (user, roleId) -> {
			user.setRoleIds(roleId);
			return user;
		}).flatMap(user -> userRepository.save(user));
	}

	@Override
	public Flux<Role> findRoleByUsername(String username) {
		return userRepository
				.findUserByUsername(username)
				.flatMapMany(user -> roleRepository.findAllById(user.getRoleIds()));
	}

	@Override
	public Mono<Role> findRoleByRoleName(String roleName){
		return roleRepository.findRoleByRoleName(roleName);
	}
	
	@Override
	public Flux<SimpleGrantedAuthority> findAuthoritiesForUser(User user){
		return roleRepository
				.findAllById(user.getRoleIds()).map(Role::getPrivilegeIds)
				.flatMap(privIds -> privilegeRepository.findAllById(privIds))
				.map(priv -> new SimpleGrantedAuthority(priv.getPrivilegeName()));
				
	}
	

	@Override
	public Mono<List<SimpleGrantedAuthority>> findAuthoritiesForUserName(String username){
		return findUserByUsername(username)
				.flatMapMany(user -> roleRepository.findAllById(user.getRoleIds()))
				.map(Role::getPrivilegeIds)
				.flatMap(privIds -> privilegeRepository.findAllById(privIds))
				.map(priv -> new SimpleGrantedAuthority(priv.getPrivilegeName()))
				.collectList();
				
	}
}
