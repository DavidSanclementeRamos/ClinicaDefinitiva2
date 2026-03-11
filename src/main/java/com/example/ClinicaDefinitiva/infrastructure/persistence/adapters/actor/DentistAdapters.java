package com.example.ClinicaDefinitiva.infrastructure.persistence.adapters.actor;

import com.example.ClinicaDefinitiva.application.exceptions.actor.DentistNotFoundException;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.output.DentistRepository;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.actor.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.dentist.DentistReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.mapper.actor.dentist.DentistWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DentistAdapters implements DentistRepository {

    private final DentistJpaRepository dentistJpaRepository;
    private final DentistWriteEntityMapper writeEntityMapper;
    private final DentistReadEntityMapper readEntityMapper;

    public DentistAdapters(DentistJpaRepository dentistJpaRepository, DentistWriteEntityMapper writeEntityMapper, DentistReadEntityMapper readEntityMapper) {
        this.dentistJpaRepository = dentistJpaRepository;
        this.writeEntityMapper = writeEntityMapper;
        this.readEntityMapper = readEntityMapper;
    }


    @Override
    public Optional<Dentist> findById(DentistId id) {
        return dentistJpaRepository.findById(id.value())
                .map(readEntityMapper::toDomain);
    }

    @Override
    public Page<Dentist> findAll(Pageable pageable) {
        return dentistJpaRepository.findAll(pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public Dentist save(Dentist dentist) {
        return readEntityMapper.toDomain(dentistJpaRepository
                .save(writeEntityMapper.toEntity(dentist)));
    }

    

    @Override
    public Page<Dentist> findBySpecialty(String specialty, Pageable pageable) {
        return dentistJpaRepository.findBySpecialties(specialty,pageable)
                .map(readEntityMapper::toDomain);
    }

    @Override
    public void deleteById(DentistId id) {
        if(dentistJpaRepository.findById(id.value()).isEmpty()){
            throw new DentistNotFoundException("");
        }
        dentistJpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(Long id) {
        return dentistJpaRepository.existsById(id);
    }

    @Override
    public Optional<Dentist> findByUserId(UserIdentityId id) {
        return Optional.empty();
    }

    @Override
    public Page<Dentist> findByAvailability(String status, Pageable pageable) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
