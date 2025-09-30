package com.example.ClinicaDefinitiva;
import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.config.RolesFactory;
import com.example.ClinicaDefinitiva.persistence.entity.RolesEntity;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.RolesEntityRepository;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class ClinicaDefinitivaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClinicaDefinitivaApplication.class, args);

		Logger logger = LoggerFactory.getLogger(ClinicaDefinitivaApplication.class);
		logger.info("Logger funciona correctamente");
		System.out.print(" holo desde Spring");
	}

	@Profile("!test")
	@Bean
	public CommandLineRunner dataLoader(RolesEntityRepository roleRepo,
										UsuarioRepository userRepo,
										PasswordEncoder encoder) {
		return args -> {
			// 1. Garantiza todos los roles en BD
			for (Roles r : Roles.values()) {
				roleRepo.findByRoleEnum(r)
						.orElseGet(() -> roleRepo.save(RolesFactory.createRoleEntity(r)));
			}

			// 2. Crea usuario admin si no existe
			if (userRepo.count() == 0) {
				RolesEntity admin = roleRepo.findByRoleEnum(Roles.ADMINISTRADOR).orElseThrow();
				Usuario u = new Usuario();
				u.setNombreUsuario("santiago");
				u.setContrasena(encoder.encode("tuClaveSegura"));
				u.setEnabled(true);
				u.setAccountNoExpired(true);
				u.setAccountNoLocked(true);
				u.setCredentialNoExpired(true);
				u.setRoles(Set.of(admin));
				userRepo.save(u);
			}


		};

// http://localhost/phpmyadmin/index.php?route=/sql&pos=0&db=prueva&tab

//C:/Users/David/OneDrive/Documents/proyectosJava/ClinicaDefinitiva
	}
}