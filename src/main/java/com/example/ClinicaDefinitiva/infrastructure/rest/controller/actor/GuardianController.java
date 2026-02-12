package com.example.ClinicaDefinitiva.infrastructure.rest.controller.actor;

import com.example.ClinicaDefinitiva.application.dto.actor.guardian.PageGuardianDto;
import com.example.ClinicaDefinitiva.application.portsInput.actor.GuardianUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.guardian.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.guardianRestMapper.GuardianReadMapperRest;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.guardianRestMapper.GuardianWriteMapperRest;
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
@RequestMapping("/api/v1/guardians")
public class GuardianController {

    private final GuardianReadMapperRest readMapperRest;
    private final GuardianWriteMapperRest writeMapperRest;
    private final GuardianUseCase guardianUserCase;

    public GuardianController(GuardianReadMapperRest readMapperRest,
                              GuardianWriteMapperRest writeMapperRest,
                              GuardianUseCase guardianUserCase) {
        this.readMapperRest = readMapperRest;
        this.writeMapperRest = writeMapperRest;
        this.guardianUserCase = guardianUserCase;
    }

    @GetMapping
    public PageResponse<GuardianPageResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageGuardianDto> guardianPage = guardianUserCase.findAll(PageRequest.of(page, size));

        List<GuardianPageResponse> content = guardianPage.getContent()
                .stream()
                .map(readMapperRest::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                guardianPage.getNumber(),
                guardianPage.getSize(),
                guardianPage.getTotalElements(),
                guardianPage.getTotalPages(),
                guardianPage.isLast()
        );
    }

    @GetMapping("/{id}")
    public GuardianReadResponse findById(@PathVariable Long id) {
        return readMapperRest.toResponse(guardianUserCase.findById(id));
    }

    @GetMapping("/patient/{patientId}")
    public GuardianReadResponse findByPatientId(@PathVariable Long patientId) {
        return readMapperRest.toResponse(guardianUserCase.findByPatientId(patientId));
    }

    @PostMapping
    public ResponseEntity<GuardianReadResponse> save(@Valid @RequestBody GuardianCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readMapperRest.toResponse(
                        guardianUserCase.save(writeMapperRest.toCreateDto(request))));
    }

    @PutMapping("/{id}/contact")
    public GuardianReadResponse updateContact(@PathVariable Long id,
                                              @Valid @RequestBody GuardianUpdateContactRequest request) {
        return readMapperRest.toResponse(
                guardianUserCase.updateContactData(writeMapperRest.toUpdateContactDto(request), id));
    }

    @PutMapping("/{id}/sensitive")
    public GuardianReadResponse updateSensitive(@PathVariable Long id,
                                                @Valid @RequestBody GuardianUpdateSensitiveRequest request) {
        return readMapperRest.toResponse(
                guardianUserCase.updateSensitiveData(writeMapperRest.toUpdateSensitiveDto(request), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        guardianUserCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
