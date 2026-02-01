package com.example.ClinicaDefinitiva.infrastructure.rest.controller.auth;


import com.example.ClinicaDefinitiva.application.dto.user.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.portsInput.userIdentity.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.login.LoginRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.login.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserIdentityUseCase useCase;
    private final JwtProvider jwtProvider;

    public AuthController(UserIdentityUseCase useCase, JwtProvider jwtProvider) {
        this.useCase = useCase;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Caso de uso valida credenciales con el agregado
        ReadUserIdentityDto user = useCase.authenticate(request.email(), request.password());

        // Generar token JWT
        String token = jwtProvider.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}

