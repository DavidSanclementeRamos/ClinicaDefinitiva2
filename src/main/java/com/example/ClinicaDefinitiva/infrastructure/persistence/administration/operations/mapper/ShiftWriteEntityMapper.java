package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.mapper;

import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ExcludedBlock;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ExcludedBlockEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ShiftEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ShiftWriteEntityMapper {

    public ShiftEntity toEntity(Shift shift) {
        if (shift == null) return null;

        ShiftEntity entity = new ShiftEntity();

        if (shift.getId() != null && shift.getId().value() != null) {
           entity.setId(shift.getId().value());
        }

        entity.setDate(shift.getDate());
        entity.setStartTime(shift.getStartTime());
        entity.setEndTime(shift.getEndTime());
        entity.setType(shift.getType().name());
        entity.setStatus(shift.getStatus().getValue().name());
        entity.setCancellationReason(shift.getCancellationReason());
        entity.setVersion(shift.getVersion() != null ? shift.getVersion() : 0L);

        // Mapear bloques excluidos
        entity.setExcludedBlocks(
                shift.getExcludedBlocks().stream()
                        .map(block -> toExcludedBlockEntity(block, entity))
                        .collect(Collectors.toList())
        );

        return entity;
    }

    private ExcludedBlockEntity toExcludedBlockEntity(ExcludedBlock block, ShiftEntity shiftEntity) {
        ExcludedBlockEntity entity = new ExcludedBlockEntity();
        entity.setShift(shiftEntity);
        entity.setBlockStartTime(block.getStart());
        entity.setBlockEndTime(block.getEnd());
        entity.setReason(block.getReason());
        return entity;
    }
}
