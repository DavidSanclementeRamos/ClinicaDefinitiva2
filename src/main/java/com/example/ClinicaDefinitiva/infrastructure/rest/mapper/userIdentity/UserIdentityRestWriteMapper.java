package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity;

import com.example.ClinicaDefinitiva.application.dto.authentication.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.authentication.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.CreateUserIdentityRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UpdateUserIdentityRequest;

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
                request.verified(),
                request.password(),
                request.version()

        );
    }


}


