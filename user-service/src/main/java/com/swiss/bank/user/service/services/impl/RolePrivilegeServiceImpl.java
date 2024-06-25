package com.swiss.bank.user.service.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.swiss.bank.user.service.entities.PathPrivilegeMapper;
import com.swiss.bank.user.service.entities.Privilege;
import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.entities.User;
import com.swiss.bank.user.service.models.RolePrivilegeResponse;
import com.swiss.bank.user.service.models.UserAccessResponse;
import com.swiss.bank.user.service.repositories.PathPrivilegeMapperRepository;
import com.swiss.bank.user.service.repositories.PrivilegeRepository;
import com.swiss.bank.user.service.repositories.RoleRepository;
import com.swiss.bank.user.service.services.RolePrivilegeService;
import com.swiss.bank.user.service.services.UserService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RolePrivilegeServiceImpl implements RolePrivilegeService{
	
	private RoleRepository roleRepository;
	private PrivilegeRepository privilegeRepository;
	private UserService userService;
	private PathPrivilegeMapperRepository pathPrivilegeMapperRepository;
	
	public RolePrivilegeServiceImpl(
			RoleRepository roleRepository,
			PrivilegeRepository privilegeRepository,
			UserService userService,
			PathPrivilegeMapperRepository pathPrivilegeMapperRepository) {
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
		this.userService = userService;
		this.pathPrivilegeMapperRepository = pathPrivilegeMapperRepository;
	}

	@Override
	public Mono<Role> createRole(String roleName) {
		return roleRepository
			.findRoleByRoleName(roleName)
			.switchIfEmpty(roleRepository.save(Role
					.builder()
					.roleName(roleName)
					.privilegeIds(new ArrayList<>())
					.build()))
			.doOnNext(role -> Mono.error(new IllegalArgumentException("Role already exists")));
	}

	@Override
	public Mono<Privilege> createPrivilege(String privilegeName) {
		return privilegeRepository
				.findPrivilegeByPrivilegeName(privilegeName)
				.switchIfEmpty(privilegeRepository.save(Privilege.builder().privilegeName(privilegeName).build()))
				.doOnNext(privilege -> Mono.error(new IllegalArgumentException("Privilege already exists")));
	}

	@Override
	public Mono<Role> removePrivilegeFromRole(String roleName, String privilegeName) {
		
		Mono<Role> roleMono = roleRepository
				.findRoleByRoleName(roleName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Role doesn't exists")));
		Mono<Privilege> privilegeMono = privilegeRepository
				.findPrivilegeByPrivilegeName(privilegeName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Privilege doesn't exists")));
		return roleMono.zipWith(privilegeMono, (role, privilege) -> {
			role.getPrivilegeIds().remove(privilege.getId());
			return roleRepository.save(role);
		})
		.flatMap(Function.identity());
	}

	@Override
	public Mono<Role> addPrivilegeToRole(String roleName, String privilegeName) {

		Mono<Role> roleMono = roleRepository
				.findRoleByRoleName(roleName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Role doesn't exists")));
		Mono<Privilege> privilegeMono = privilegeRepository
				.findPrivilegeByPrivilegeName(privilegeName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Privilege doesn't exists")));
		return roleMono.zipWith(privilegeMono, (role, privilege) -> {
			role.getPrivilegeIds().add(privilege.getId());
			return roleRepository.save(role);
		})
		.flatMap(Function.identity());
	}

	@Override
	public Flux<RolePrivilegeResponse> findAllRoles() {
		return roleRepository.findAll().map(role -> 
			 privilegeRepository
					.findAllById(role.getPrivilegeIds())
					.map(Privilege::getPrivilegeName)
					.collectList()
					.map(privilegeNames -> RolePrivilegeResponse
							.builder()
							.roleName(role.getRoleName())
							.privilegeNames(privilegeNames)
							.build())
		)
		.flatMap(Function.identity());
	}

	@Override
	public Flux<UserAccessResponse> findAllUsers() {
		return userService.findAllUsers().map(user -> {
			Mono<List<RolePrivilegeResponse>> userRolesMono = roleRepository
					.findAllById(user.getRoleIds())
					.flatMap(role -> privilegeRepository
						.findAllById(role.getPrivilegeIds())
						.map(Privilege::getPrivilegeName)
						.collectList()
						.map(privList -> RolePrivilegeResponse
								.builder()
								.roleName(role.getRoleName())
								.privilegeNames(privList)
								.build())
					)
					.collectList();
			return userRolesMono.map(roles -> UserAccessResponse
					.builder()
					.username(user.getUsername())
					.roles(roles)
					.build());
		})
		.flatMap(Function.identity());
	}

	@Override
	public Flux<Privilege> findAllPrivileges() {
		return privilegeRepository.findAll();
	}

	@Override
	public Mono<User> addRoleToUser(String username, String roleName) {

		Mono<User> userMono = userService
				.findUserByUsername(username)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("User doesn't exists")));
		Mono<Role> roleMono = roleRepository
				.findRoleByRoleName(roleName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Role doesn't exists")));
		return userMono.zipWith(roleMono, (user, role) -> {
			user.getRoleIds().add(role.getId());
			return userService.updateUser(user);
		})
		.flatMap(Function.identity());
	}

	@Override
	public Mono<Role> removeAllPriviliges(String roleName) {
		return roleRepository
				.findRoleByRoleName(roleName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Role doesn't exists")))
		 		.map(role -> {
					role.setPrivilegeIds(new ArrayList<>());
					return roleRepository.save(role);
				})
		 		.flatMap(Function.identity());
	}

	@Override
	public Mono<Role> addAllPrivilegesToRole(String roleName) {
		return roleRepository
			.findRoleByRoleName(roleName)
			.zipWith(privilegeRepository
					.findAll()
					.map(Privilege::getId)
					.collectList(), (role, privilegeIds) -> {
				role.setPrivilegeIds(privilegeIds);
				return roleRepository.save(role);
			})
			.flatMap(Function.identity());
	}

	@Override
	public Mono<User> addAllRolesToUser(String username) {
		return userService
				.findUserByUsername(username)
				.zipWith(roleRepository.findAll().map(Role::getId).collectList(), (user, roles) -> {
					user.setRoleIds(roles);
					return user;
				})
				.flatMap(user -> userService.updateUser(user));
	}

	@Override
	public Mono<User> removeRoleFromUser(String username, String roleName) {
		Mono<User> userMono = userService
				.findUserByUsername(username)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("User doesn't exists")));
		Mono<Role> roleMono = roleRepository
				.findRoleByRoleName(roleName)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("Role doesn't exists")));
		return userMono.zipWith(roleMono, (user, role) -> {
			user.getRoleIds().remove(role.getId());
			return userService.updateUser(user);
		})
		.flatMap(Function.identity());
	}

	@Override
	public Mono<User> removeAllRoles(String username) {
		return userService
				.findUserByUsername(username)
				.switchIfEmpty(Mono.error(new IllegalArgumentException("User doesn't exists")))
				.map(user -> {
					user.setRoleIds(new ArrayList<>());
					return userService.updateUser(user);
				})
		.flatMap(Function.identity());
	}

	@Override
	public Flux<PathPrivilegeMapper> findAllPathPrivilegeMapper() {
		Flux<PathPrivilegeMapper> pathPrivilegeMapperFlux = pathPrivilegeMapperRepository.findAll();
		return privilegeRepository.findAll().map(privilege -> 
			 pathPrivilegeMapperFlux
				.filter(mapper -> mapper.getPrivilegeName()!=null && mapper.getPrivilegeName().equals(privilege.getPrivilegeName()))
				.defaultIfEmpty(new PathPrivilegeMapper())
				.map(mapper -> PathPrivilegeMapper
							.builder()
							.privilegeName(privilege.getPrivilegeName())
							.category(mapper.getCategory())
							.method(mapper.getMethod())
							.urlPattern(mapper.getUrlPattern())
							.build()
				)
			
		)
		.flatMap(Function.identity());
	}

	@Override
	public Mono<PathPrivilegeMapper> updatePathPrivilegeMapper(PathPrivilegeMapper pathPrivilegeMapper) {
		return pathPrivilegeMapperRepository
			.findByPrivilegeName(pathPrivilegeMapper.getPrivilegeName())
			.defaultIfEmpty(new PathPrivilegeMapper())
			.map(mapper -> {
				mapper.setCategory(pathPrivilegeMapper.getCategory());
				mapper.setEnabled(true);
				mapper.setMethod(pathPrivilegeMapper.getMethod());
				mapper.setUrlPattern(pathPrivilegeMapper.getUrlPattern());
				mapper.setPrivilegeName(pathPrivilegeMapper.getPrivilegeName());
				return pathPrivilegeMapperRepository.save(mapper);
			})
			.flatMap(Function.identity());
	}

}
