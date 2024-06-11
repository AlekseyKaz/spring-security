package com.dhabits.ss.demo;

import com.dhabits.ss.demo.data.entity.ResourceObjectEntity;
import com.dhabits.ss.demo.data.entity.UserActionsEntity;
import com.dhabits.ss.demo.data.entity.UserEntity;
import com.dhabits.ss.demo.data.repository.ResourceObjectRepository;
import com.dhabits.ss.demo.data.repository.UserActionsRepository;
import com.dhabits.ss.demo.data.repository.UserRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public ApplicationRunner ensureAdminCreated(PasswordEncoder passwordEncoder, ResourceObjectRepository objectRepository, UserRepository userRepository, UserActionsRepository userActionsRepository) {
		return new ApplicationRunner() {
			@Override
			public void run(ApplicationArguments args) {
				Optional<UserEntity> admin = userRepository.findByLogin("superuser@gmail.com");
				if (admin.isPresent()) {
					return;
				}
				UserEntity entity = new UserEntity();
				entity.setLogin("admin");
				entity.setOpenPassword("test");
				entity.setPassword(passwordEncoder.encode("test"));

				userRepository.save(entity);
				UserActionsEntity actions = new UserActionsEntity();
				actions.setUser(entity);
				actions.setRegular(true);
				entity.setActions(actions);
				userActionsRepository.save(actions);
				objectRepository.findById(1).orElseGet(() -> {
					ResourceObjectEntity resourceObject = new ResourceObjectEntity(1, "1", "/path");
					return objectRepository.save(resourceObject);
				});
			}
		};
	}
}
