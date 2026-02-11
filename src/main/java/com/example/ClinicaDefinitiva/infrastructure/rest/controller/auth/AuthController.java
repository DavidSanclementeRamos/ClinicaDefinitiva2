package com.example.ClinicaDefinitiva.infrastructure.rest.controller.auth;


import com.example.ClinicaDefinitiva.application.dto.authentication.ReadUserIdentityDto;
import com.example.ClinicaDefinitiva.application.portsInput.authentication.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.login.LoginRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.login.LoginResponse;
import com.example.ClinicaDefinitiva.infrastructure.security.JwtProvider;
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
        //ReadUserIdentityDto user = useCase.authenticate(request.email(), request.password());

        // Generar token JWT
       // String token = jwtProvider.generateToken(user);

        return null;//ResponseEntity.ok(new LoginResponse(token));
    }
}

