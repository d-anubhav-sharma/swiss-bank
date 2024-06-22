package com.swiss.bank.user.service;

import java.util.List;
import java.util.function.Function;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.swiss.bank.user.service.entities.Privilege;
import com.swiss.bank.user.service.entities.Role;
import com.swiss.bank.user.service.repositories.PrivilegeRepository;
import com.swiss.bank.user.service.repositories.RoleRepository;
import com.swiss.bank.user.service.repositories.UserRepository;

import reactor.core.publisher.Mono;

@SpringBootApplication
@EnableMongoRepositories({"com.swiss.bank.user.service.repositories"})
public class UserServiceApplication implements ApplicationRunner{
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PrivilegeRepository privilegeRepository;
	
	public UserServiceApplication(
			UserRepository userRepository,
			RoleRepository roleRepository,
			PrivilegeRepository privilegeRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		/**
		 * anubhav_sharma should always exists and should always be staff && admin
		 */
		Privilege staffPrivilege = Privilege
				.builder()
				.privilegeName("STAFF")
				.build();

		Privilege adminPrivilege = Privilege
				.builder()
				.privilegeName("ADMIN")
				.build();
		
		Mono<List<String>> staffAdminPrivileges = privilegeRepository
				.saveAll(List.of(staffPrivilege, adminPrivilege))
				.map(Privilege::getId)
				.collectList();
		
		Mono<Role> adminRole = staffAdminPrivileges.map(privs -> Role
				.builder()
				.roleName("ADMIN")
				.privilegeIds(privs)
				.build())
				.flatMap(role -> roleRepository.save(role));
		
		userRepository
				.findUserByUsername("anubhav_sharma")
				.zipWith(adminRole, (usr, role) -> {
					usr.getRoleIds().add(role.getId());
					return userRepository.save(usr);
				})
				.flatMap(Function.identity())
				.subscribe();
		
		
	}

}
