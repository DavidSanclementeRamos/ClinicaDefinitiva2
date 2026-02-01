package com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.CreateDentistDto;
import com.example.ClinicaDefinitiva.application.dto.user.CreateUserIdentityDto;
import com.example.ClinicaDefinitiva.application.dto.user.UpdateUserIdentityDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityCreateRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityUpdateRequest;

public class UserRestToServiceMapper {

    // De DTO REST de creación → DTO de aplicación
    public CreateUserIdentityDto toCreate(UserIdentityCreateRequest request) {
        return new CreateUserIdentityDto(
                request.email(),
                request.password(),
                request.name()
        );
    }

    // De DTO REST de update → DTO de aplicación
    public UpdateUserIdentityDto toUpdate(UserIdentityUpdateRequest request) {
        return new UpdateUserIdentityDto(
                request.email(),
                request.name(),
                request.verified(),
                request.status(),
                request.password(),
                request.version()

        );
    }


}


