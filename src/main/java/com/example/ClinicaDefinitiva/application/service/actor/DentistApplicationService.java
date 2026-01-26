package com.example.ClinicaDefinitiva.application.service.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.*;
import com.example.ClinicaDefinitiva.application.mapper.DentistReadMapper;
import com.example.ClinicaDefinitiva.application.mapper.actorMapper.dentistMapper.DentistWriteMapper;
import com.example.ClinicaDefinitiva.application.portsInput.actor.DentistUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.portsOutput.actorRepository.DentistRepository;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.service.DentistAvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DentistApplicationService implements DentistUseCase {
    private final DentistRepository dentistRepository;
    private final ScheduleRepository scheduleRepository;
    private final DentistAvailabilityService availabilityService;
    private final DentistReadMapper dentistReadMapper;
    private final DentistWriteMapper dentistWriteMapper;

    public DentistApplicationService(DentistRepository dentistRepository, ScheduleRepository scheduleRepository, DentistAvailabilityService availabilityService, DentistReadMapper dentistReadMapper, DentistWriteMapper dentistWriteMapper) {
        this.dentistRepository = dentistRepository;
        this.scheduleRepository = scheduleRepository;
        this.availabilityService = availabilityService;
        this.dentistReadMapper = dentistReadMapper;
        this.dentistWriteMapper = dentistWriteMapper;
    }


    @Override
    public ReadDentistDto findById(Long id) {

        Dentist dentist = dentistRepository.findById(DentistId.from(id)).
                orElseThrow(() -> new IllegalArgumentException(" Dentist no found"));
        return dentistReadMapper.toDto(dentist);
    }

    @Override
    public Page<PageDentistDto> findAll(Pageable pageable) {
        Page<Dentist> dentists = dentistRepository.findAll(pageable);
        if(dentists.isEmpty()){
            throw new IllegalArgumentException("No found");
        }
        return dentists.map(dentistReadMapper::pageToDto);
    }

    @Override
    public ReadDentistDto save(CreateDentistDto createDentistDto) {

        Dentist dentist = dentistWriteMapper.dtoCreateToDentist(createDentistDto);
        dentistRepository.save(dentist);
        return dentistReadMapper.toDto(dentist);
    }

    @Override
    public ReadDentistDto updateContactData(UpdateDentistContactDto updateDentistDto, Long id) {
        Dentist dentist =  dentistRepository.findById(DentistId.from(id))
                .orElseThrow(() -> new IllegalArgumentException("No found"));

        dentistWriteMapper.dtoUpdateContactToDentist(updateDentistDto, dentist);
        dentistRepository.save(dentist);
        return dentistReadMapper.toDto(dentist);
    }

    @Override
    public ReadDentistDto updateSensitiveData(UpdateDentistSensitiveDto updateDentistDto,Long id) {
        Dentist dentist =  dentistRepository.findById(DentistId.from(id))
                .orElseThrow(() -> new IllegalArgumentException("No found"));

        dentistWriteMapper.dtoUpdateSensitiveToDentist(updateDentistDto, dentist);
        dentistRepository.save(dentist);
        return dentistReadMapper.toDto(dentist);
    }

    @Override
    public ReadDentistDto updateStatus(UpdateDentistStatusDto updateDentistStatusDto, Long id) {
        Dentist dentist = dentistRepository.findById(DentistId.from(id))
                .orElseThrow(() -> new IllegalArgumentException("No found"));

        Schedule schedule = scheduleRepository.findByDentistId(updateDentistStatusDto.dentistId())
                .orElseThrow(() -> new IllegalArgumentException("No found"));
        // Aquí usas el mapper de escritura
        DentistAvailabilityStatus newStatus = dentistWriteMapper.toAvailabilityStatus(updateDentistStatusDto);
        availabilityService.changeAvailability(dentist, schedule, newStatus, updateDentistStatusDto.getHoursRange());
        dentistRepository.save(dentist);
        return dentistReadMapper.toDto(dentist);
    }

    @Override
    public Page<PageDentistDto> findByAvailability(String status, Pageable pageable) {
        Page<Dentist> dentists =  dentistRepository.findByAvailability(status, pageable);
        if(dentists.isEmpty()){
            throw new EntityNotFoundException("Dentist with availability " + status + " not found");
        }
        return dentists.map(dentistReadMapper::pageToDto);

    }

    @Override
    public Page<PageDentistDto> findBySpecialty(String specialty, Pageable pageable) {

        Page<Dentist> dentists =  dentistRepository.findBySpecialty(specialty, pageable);
        if(dentists.isEmpty()){
            throw new EntityNotFoundException("Dentist with specialty " + specialty + " not found");
        }
        return dentists.map(dentistReadMapper::pageToDto);

    }

    @Override
    public void deleteById(Long id) {
        if (!dentistRepository.existsById(id)) {
            throw new EntityNotFoundException("Dentist with id " + id + " not found");
        }
        dentistRepository.deleteById(DentistId.from(id));
    }
}
