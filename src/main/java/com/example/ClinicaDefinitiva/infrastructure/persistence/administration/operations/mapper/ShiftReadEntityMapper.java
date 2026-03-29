package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.mapper;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.administration.operations.enu.ShiftType;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ExcludedBlock;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftStatus;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ExcludedBlockEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ShiftEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShiftReadEntityMapper {

    public Shift toDomain(ShiftEntity entity) {
        if (entity == null) return null;

        // Crear Shift usando reconstruct (asumiendo que existe o creamos método)
        Shift shift = Shift.create(
                DentistId.of(entity.getDentist().getId()),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                ShiftType.valueOf(entity.getType())
        );

        // Establecer campos adicionales usando reflexión o método reconstruct
        // Por simplicidad, asumimos que existe un método reconstruct
        return Shift.reconstruct(
                ShiftId.from(entity.getId()),
                DentistId.of(entity.getDentist().getId()),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime(),
                ShiftType.valueOf(entity.getType()),
                ShiftStatus.of(ShiftStatus.Status.valueOf(entity.getStatus())),
                entity.getCancellationReason(),
                entity.getExcludedBlocks().stream()
                        .map(this::toExcludedBlockDomain)
                        .collect(Collectors.toList()),
                entity.getVersion()
        );
    }

    private ExcludedBlock toExcludedBlockDomain(ExcludedBlockEntity entity) {
        return new ExcludedBlock(
                entity.getBlockStartTime(),
                entity.getBlockEndTime(),
                entity.getReason()
        );
    }
}
