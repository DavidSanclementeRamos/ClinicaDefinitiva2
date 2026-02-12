package com.example.ClinicaDefinitiva.infrastructure.rest.controller.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.Patient.PagePatientDto;
import com.example.ClinicaDefinitiva.application.portsInput.actor.PatientUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.patient.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper.PatientReadMapperRest;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.patientRestMapper.PatientWriteMapperRest;
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
@RequestMapping("/api/v1/patients")
public class PatientController {

    private final PatientReadMapperRest readMapperRest;
    private final PatientWriteMapperRest writeMapperRest;
    private final PatientUseCase patientUserCase;

    public PatientController(PatientReadMapperRest readMapperRest,
                             PatientWriteMapperRest writeMapperRest,
                             PatientUseCase patientUserCase) {
        this.readMapperRest = readMapperRest;
        this.writeMapperRest = writeMapperRest;
        this.patientUserCase = patientUserCase;
    }

    // 🔹 Listado general con paginación
    @GetMapping
    public PageResponse<PatientPageResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PagePatientDto> patientPage = patientUserCase.findAll(PageRequest.of(page, size));

        List<PatientPageResponse> content = patientPage.getContent()
                .stream()
                .map(readMapperRest::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                patientPage.getNumber(),
                patientPage.getSize(),
                patientPage.getTotalElements(),
                patientPage.getTotalPages(),
                patientPage.isLast()
        );
    }

    @GetMapping("/{id}")
    public PatientReadResponse findById(@PathVariable Long id) {
        return readMapperRest.toResponse(patientUserCase.findById(id));
    }

    // 🔹 Buscar pacientes por contrato
    @GetMapping("/contract/{contractId}")
    public PageResponse<PatientPageResponse> findByContractId(
            @PathVariable Long contractId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PagePatientDto> patientPage = patientUserCase.findByContractId(contractId, PageRequest.of(page, size));

        List<PatientPageResponse> content = patientPage.getContent()
                .stream()
                .map(readMapperRest::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                patientPage.getNumber(),
                patientPage.getSize(),
                patientPage.getTotalElements(),
                patientPage.getTotalPages(),
                patientPage.isLast()
        );
    }

    // 🔹 Buscar pacientes por guardián
    @GetMapping("/guardian/{guardianId}")
    public PageResponse<PatientPageResponse> findByGuardianId(
            @PathVariable String guardianId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PagePatientDto> patientPage = patientUserCase.findByGuardianId(guardianId, PageRequest.of(page, size));

        List<PatientPageResponse> content = patientPage.getContent()
                .stream()
                .map(readMapperRest::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                patientPage.getNumber(),
                patientPage.getSize(),
                patientPage.getTotalElements(),
                patientPage.getTotalPages(),
                patientPage.isLast()
        );
    }

    @PostMapping
    public ResponseEntity<PatientReadResponse> save(@Valid @RequestBody PatientCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readMapperRest.toResponse(
                        patientUserCase.save(writeMapperRest.toCreateDto(request))));
    }

    @PutMapping("/{id}/contact")
    public PatientReadResponse updateContact(@PathVariable Long id,
                                             @Valid @RequestBody PatientUpdateContactRequest request) {
        return readMapperRest.toResponse(
                patientUserCase.updateContactData(writeMapperRest.toUpdateContactDto(request), id));
    }

    @PutMapping("/{id}/sensitive")
    public PatientReadResponse updateSensitive(@PathVariable Long id,
                                               @Valid @RequestBody PatientUpdateSensitiveRequest request) {
        return readMapperRest.toResponse(
                patientUserCase.updateSensitiveData(writeMapperRest.toUpdateSensitiveDto(request), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientUserCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
