package com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.adapters;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.ReceptionId;
import com.example.ClinicaDefinitiva.domain.administration.operations.ShiftRepository;
import com.example.ClinicaDefinitiva.domain.administration.operations.model.Shift;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.entity.ShiftEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.jpaRepository.ShiftJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.mapper.ShiftReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.operations.mapper.ShiftWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Transactional
public class ShiftAdapter implements ShiftRepository {

    private final ShiftJpaRepository shiftJpaRepository;
    private final DentistJpaRepository dentistJpaRepository;
    private final ShiftReadEntityMapper readMapper;
    private final ShiftWriteEntityMapper writeMapper;

    public ShiftAdapter(
            ShiftJpaRepository shiftJpaRepository,
            DentistJpaRepository dentistJpaRepository,
            ShiftReadEntityMapper readMapper,
            ShiftWriteEntityMapper writeMapper) {
        this.shiftJpaRepository = shiftJpaRepository;
        this.dentistJpaRepository = dentistJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public Shift save(Shift shift) {
        ShiftEntity entity = writeMapper.toEntity(shift);
        
        // Establecer relación con Dentist
        if (shift.getDentistId() != null && shift.getDentistId().value() != null) {
            dentistJpaRepository.findById(shift.getDentistId().value())
                    .ifPresent(entity::setDentist);
        }
        
        ShiftEntity savedEntity = shiftJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Shift> findById(ShiftId id) {
        if (id == null || id.value() == null) {
            return Optional.empty();
        }
        return shiftJpaRepository.findById(id.value())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findAll(Pageable pageable) {
        return shiftJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findActiveByDentistAndDate(DentistId dentistId, LocalDate date, Pageable pageable) {
        return shiftJpaRepository.findActiveByDentistAndDate(
                dentistId.value(), 
                date, 
                "ACTIVE", 
                pageable
        ).map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findActiveByDentistAndDateRange(
            DentistId dentistId, 
            LocalDate startDate, 
            LocalDate endDate, 
            Pageable pageable) {
        return shiftJpaRepository.findActiveByDentistAndDateRange(
                dentistId.value(),
                startDate,
                endDate,
                "ACTIVE",
                pageable
        ).map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findOverlapping(
            DentistId dentistId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            boolean withLock,
            Pageable pageable) {
        
        if (withLock) {
            return shiftJpaRepository.findOverlappingWithLock(
                    dentistId.value(),
                    date,
                    startTime,
                    endTime,
                    pageable
            ).map(readMapper::toDomain);
        } else {
            return shiftJpaRepository.findOverlapping(
                    dentistId.value(),
                    date,
                    startTime,
                    endTime,
                    pageable
            ).map(readMapper::toDomain);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Shift> findActiveByDentist(DentistId dentistId, Pageable pageable) {
        return shiftJpaRepository.findActiveByDentist(dentistId.value(), "ACTIVE", pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public void delete(ShiftId id) {
        if (id != null && id.value() != null) {
            shiftJpaRepository.deleteById(id.value());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shift> findByReceptionistId(ReceptionId receptionId) {
        // Nota: Esto asume que Receptionist tiene relación con Shift
        // Si no es así, este método podría no ser necesario
        return shiftJpaRepository.findByReceptionistId(receptionId.getValue())
                .stream()
                .map(readMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shift> findActiveByDentistAndDate(DentistId dentistId, LocalDate date) {
        return shiftJpaRepository.findActiveByDentistAndDate(
                dentistId.value(), 
                date, 
                "ACTIVE"
        ).stream()
         .map(readMapper::toDomain)
         .collect(Collectors.toList());
    }
}