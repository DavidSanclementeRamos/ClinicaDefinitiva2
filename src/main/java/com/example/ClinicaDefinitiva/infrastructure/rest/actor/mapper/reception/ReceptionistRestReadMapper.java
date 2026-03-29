package com.example.ClinicaDefinitiva.infrastructure.rest.actor.mapper.reception;

import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.actor.dto.receptionist.ReadReceptionistDto;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.PageReceptionistResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.actor.dto.reception.ReadReceptionistResponse;
import org.springframework.stereotype.Component;

@Component
public class ReceptionistRestReadMapper {

    // De DTO de aplicación → DTO REST completo (detalle) - CORREGIDO
    public ReadReceptionistResponse toRest(ReadReceptionistDto dto) {
        if (dto == null) return null;

        return new ReadReceptionistResponse(
                dto.receptionist(),        
                dto.sector(),
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.age(),
                dto.phoneNumber(),
                dto.dateOfBirth(),
                dto.bloodType(),
                dto.documentEPS(),
                dto.user(),                 // ← Corregido (antes estaba comentado y duplicado)
                dto.lastUpdate(),
                dto.street(),
                dto.city(),
                dto.state(),
                dto.country(),
                dto.postalCode()
        );
    }

    // De DTO de aplicación → DTO REST simplificado (listados/paginación)
    public PageReceptionistResponse toPageRest(PageReceptionistDto dto) {
        if (dto == null) return null;

        return new PageReceptionistResponse(
                dto.receptionist(),         
                dto.sector(),
                
                dto.dni(),
                dto.first(),
                dto.lastName(),
                dto.phoneNumber()
        );
    }
}
