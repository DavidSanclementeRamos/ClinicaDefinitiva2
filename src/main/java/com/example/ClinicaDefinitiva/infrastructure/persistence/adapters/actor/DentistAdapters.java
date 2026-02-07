package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.application.exceptions.actorException.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.dentistEntityMapper.DentistReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actorMapper.dentistEntityMapper.DentistWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DentistAdapters implements DentistRepository {

    private final DentistJpaRepository dentistJpaRepository;
    private final DentistWriteEntityMapper dentistEntityMapper;
    private final DentistReadEntityMapper dentistReadEntityMapper;

    public DentistAdapters(DentistJpaRepository dentistJpaRepository, DentistWriteEntityMapper dentistEntityMapper, DentistReadEntityMapper dentistReadEntityMapper) {
        this.dentistJpaRepository = dentistJpaRepository;
        this.dentistEntityMapper = dentistEntityMapper;
        this.dentistReadEntityMapper = dentistReadEntityMapper;
    }

    @Override
    public Optional<Dentist> findById(DentistId id) {
        return dentistJpaRepository.findById(id.getValue())
                .map(dentistEntityMapper::toDomain);
    }

    @Override
    public Page<Dentist> findAll(Pageable pageable) {
        return dentistJpaRepository.findAll(pageable)
                .map(dentistEntityMapper::toDomain);
    }

    @Override
    public Dentist save(Dentist dentist) {
        return dentistEntityMapper.toDomain(dentistJpaRepository
                .save(dentistReadEntityMapper.toEntity(dentist)));
    }

    @Override
    public Page<Dentist> findByAvailability(String status, Pageable pageable) {
        return dentistJpaRepository.findByAvailability(status,pageable)
                .map(dentistEntityMapper::toDomain);
    }

    @Override
    public Page<Dentist> findBySpecialty(String specialty, Pageable pageable) {
        return dentistJpaRepository.findBySpecialty(specialty,pageable)
                .map(dentistEntityMapper::toDomain);
    }

    @Override
    public void deleteById(DentistId id) {
        if(dentistJpaRepository.findById(id.getValue()).isEmpty()){
            throw new DentistNotFoundException("");
        }
        dentistJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(Long id) {
        return dentistJpaRepository.existsById(id);
    }
}
