package com.example.ClinicaDefinitiva.infrastructure.rest.authentication.mapper;

import com.example.ClinicaDefinitiva.application.authentication.dto.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.authentication.dto.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.CreateUserIdentityRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.authentication.dto.UpdateUserIdentityRequest;
import org.springframework.stereotype.Component;

@Component
public class UserIdentityRestWriteMapper {

    // De DTO REST de creación → DTO de aplicación
    public CreateUserIdentityDto toServiceCreate(CreateUserIdentityRequest request) {
        return new CreateUserIdentityDto(
                request.email(),
                request.password(),
                request.name()
        );
    }

    // De DTO REST de update → DTO de aplicación
    public UpdateUserIdentityDto toServiceUpdate(UpdateUserIdentityRequest request) {
        return new UpdateUserIdentityDto(
                request.email(),
                request.name(),
                request.password()

        );
    }


}


