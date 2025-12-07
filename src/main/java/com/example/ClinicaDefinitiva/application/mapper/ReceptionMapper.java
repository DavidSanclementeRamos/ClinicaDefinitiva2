package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.receptionist.ReadReceptionDto;
import com.example.ClinicaDefinitiva.domain.actor.model.Receptionist;
import org.springframework.stereotype.Component;

@Component
public final class ReceptionMapper {
    public ReadReceptionDto toReadReceptionDto(Receptionist r) {
        if (r == null) return null;
        ReadReceptionDto dto = new ReadReceptionDto();
        dto.setReceptionId(r.getId() != null ? r.getId().toString() : null);
        dto.setPerson(r.getPerson());
        dto.setSector(r.getSector());
        dto.setUserId(String.valueOf(r.getUser() != null ? r.getUser().getId() : null));
        // si tu UserIdentity.getId() no devuelve String, ajusta
        dto.setLastUpdate(null); // si Receptionist expone lastUpdate, mapearlo; en tu ejemplo no está en campos explícitos
        return dto;
    }


}
