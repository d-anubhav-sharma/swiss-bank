package com.swiss.bank.user.service.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.exceptions.InvalidUsernamePasswordException;
import com.swiss.bank.user.service.models.LoginRequest;
import com.swiss.bank.user.service.models.RegisterUserRequest;
import com.swiss.bank.user.service.models.RegisterUserResponse;
import com.swiss.bank.user.service.models.UserUpdateRequest;
import com.swiss.bank.user.service.repositories.RoleRepository;
import com.swiss.bank.user.service.repositories.UserRepository;
import com.swiss.bank.user.service.services.UserService;
import com.swiss.bank.user.service.util.DataUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserServiceImpl implements UserService {

	PasswordEncoder encoder;
	RoleRepository roleRepository;
	UserRepository userRepository;

	public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
		this.encoder = passwordEncoder;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	@Override
	public Flux<User> findAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public Mono<User> findUserByUsername(String username) {
		Mono<User> userMono = userRepository.findUserByUsername(username);
		Mono<List<Role>> rolesMono = userMono.flatMap(user -> roleRepository.findRoleByUsername(username).collectList());
		return userMono.zipWith(rolesMono, (user, roles) -> {
			user.setRoles(roles);
			return user;
		});
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
				.roles(new ArrayList<>())
				.password(encoder.encode(registerUserRequest.getPassword()))
				.build()).map(user -> RegisterUserResponse.builder().username(user.getUsername()).build());
	}

	@Override
	public Mono<User> saveUserWithRoles(UserUpdateRequest updateRequest) {
		Mono<User> userMono = userRepository.findById(DataUtil.getOrDefault(updateRequest.getId(), ""))
				.switchIfEmpty(userRepository.findUserByUsername(updateRequest.getUsername()))
				.defaultIfEmpty(new User());

		Mono<List<Role>> rolesMono = Flux.fromIterable(updateRequest.getRoles()).flatMap(role -> roleRepository.save(role)).collectList();

		return userMono.zipWith(rolesMono, (user, roles) -> {
			user.setRoles(roles);
			return user;
		}).flatMap(user -> userRepository.save(user));
	}

}
