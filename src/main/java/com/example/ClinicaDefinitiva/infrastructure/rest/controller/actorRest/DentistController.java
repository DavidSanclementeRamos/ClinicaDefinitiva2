package com.example.ClinicaDefinitiva.infrastructure.rest.controller.actorRest;

import com.example.ClinicaDefinitiva.application.dto.actor.dentist.PageDentistDto;
import com.example.ClinicaDefinitiva.application.portsInput.actor.DentistUseCase;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.dentist.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentistReadMapper.DentistReadRestMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.dentistReadMapper.DentistWriteRestMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/api/v1/dentists")
public class DentistController {

    private final DentistReadRestMapper readRestMapper;
    private final DentistWriteRestMapper writeRestMapper;
    private final DentistUseCase dentistUseCase;

    public DentistController(DentistReadRestMapper readRestMapper,
                             DentistWriteRestMapper writeRestMapper,
                             DentistUseCase dentistUseCase) {
        this.readRestMapper = readRestMapper;
        this.writeRestMapper = writeRestMapper;
        this.dentistUseCase = dentistUseCase;
    }

    @GetMapping
    public PageResponse<DentistPageResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageDentistDto> dentistPage = dentistUseCase.findAll(PageRequest.of(page, size));

        List<DentistPageResponse> content = dentistPage.getContent()
                .stream()
                .map(readRestMapper::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                dentistPage.getNumber(),
                dentistPage.getSize(),
                dentistPage.getTotalElements(),
                dentistPage.getTotalPages(),
                dentistPage.isLast()
        );
    }

    @GetMapping("/availability/{status}")
    public PageResponse<DentistPageResponse> findByAvailability(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageDentistDto> dentistPage = dentistUseCase.findByAvailability(status, PageRequest.of(page, size));

        List<DentistPageResponse> content = dentistPage.getContent()
                .stream()
                .map(readRestMapper::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                dentistPage.getNumber(),
                dentistPage.getSize(),
                dentistPage.getTotalElements(),
                dentistPage.getTotalPages(),
                dentistPage.isLast()
        );
    }

    @GetMapping("/specialty/{specialty}")
    public PageResponse<DentistPageResponse> findBySpecialty(
            @PathVariable String specialty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageDentistDto> dentistPage = dentistUseCase.findBySpecialty(specialty, PageRequest.of(page, size));

        List<DentistPageResponse> content = dentistPage.getContent()
                .stream()
                .map(readRestMapper::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                dentistPage.getNumber(),
                dentistPage.getSize(),
                dentistPage.getTotalElements(),
                dentistPage.getTotalPages(),
                dentistPage.isLast()
        );
    }

    @GetMapping("/{id}")
    public DentistReadResponse findById(@PathVariable Long id) {
        return readRestMapper.toResponse(dentistUseCase.findById(id));
    }

    @PostMapping
    public ResponseEntity<DentistReadResponse> save(@Valid @RequestBody DentistCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readRestMapper.toResponse(
                        dentistUseCase.save(writeRestMapper.toCreateDto(request))));
    }

    @PutMapping("/{id}/contact")
    public DentistReadResponse updateContact(@PathVariable Long id,
                                             @Valid @RequestBody DentistUpdateContactRequest request) {
        return readRestMapper.toResponse(
                dentistUseCase.updateContactData(writeRestMapper.toUpdateContactDto(request), id));
    }

    @PutMapping("/{id}/sensitive")
    public DentistReadResponse updateSensitive(@PathVariable Long id,
                                               @Valid @RequestBody DentistUpdateSensitiveRequest request) {
        return readRestMapper.toResponse(
                dentistUseCase.updateSensitiveData(writeRestMapper.toUpdateSensitiveDto(request), id));
    }

    @PutMapping("/{id}/availability")
    public DentistReadResponse updateAvailability(@PathVariable Long id,
                                                  @Valid @RequestBody DentistUpdateStatusRequest request) {
        return readRestMapper.toResponse(
                dentistUseCase.updateStatus(writeRestMapper.toUpdateStatusDto(request), id));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        dentistUseCase.deleteById(id);
    }
}
