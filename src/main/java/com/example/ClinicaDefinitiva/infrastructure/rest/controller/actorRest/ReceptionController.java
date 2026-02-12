package com.example.ClinicaDefinitiva.infrastructure.rest.controller.actorRest;

import com.example.ClinicaDefinitiva.application.dto.actor.Receptionist.PageReceptionistDto;
import com.example.ClinicaDefinitiva.application.portsInput.actor.ReceptionUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.actorDto.reception.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.receptionReadMapper.ReceptionReadMapperRest;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.actor.receptionReadMapper.ReceptionWriteMapperRest;
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
@RequestMapping("/api/v1/receptions")
public class ReceptionController {

    private final ReceptionReadMapperRest readMapperRest;
    private final ReceptionWriteMapperRest writeMapperRest;
    private final ReceptionUseCase receptionUserCase;

    public ReceptionController(ReceptionReadMapperRest readMapperRest,
                               ReceptionWriteMapperRest writeMapperRest,
                               ReceptionUseCase receptionUserCase) {
        this.readMapperRest = readMapperRest;
        this.writeMapperRest = writeMapperRest;
        this.receptionUserCase = receptionUserCase;
    }

    @GetMapping
    public PageResponse<ReceptionPageResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageReceptionistDto> receptionPage = receptionUserCase.findAll(PageRequest.of(page, size));

        List<ReceptionPageResponse> content = receptionPage.getContent()
                .stream()
                .map(readMapperRest::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                receptionPage.getNumber(),
                receptionPage.getSize(),
                receptionPage.getTotalElements(),
                receptionPage.getTotalPages(),
                receptionPage.isLast()
        );
    }

    @GetMapping("/{id}")
    public ReceptionReadResponse findById(@PathVariable Long id) {
        return readMapperRest.toResponse(receptionUserCase.findById(id));
    }

    @GetMapping("/sector/{sector}")
    public PageResponse<ReceptionPageResponse> findBySector(
            @PathVariable String sector,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageReceptionistDto> receptionistPage = receptionUserCase.findBySector(sector, PageRequest.of(page, size));

        List<ReceptionPageResponse> content = receptionistPage.getContent()
                .stream()
                .map(readMapperRest::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                receptionistPage.getNumber(),
                receptionistPage.getSize(),
                receptionistPage.getTotalElements(),
                receptionistPage.getTotalPages(),
                receptionistPage.isLast()
        );
    }

    @PostMapping
    public ResponseEntity<ReceptionReadResponse> save(@Valid @RequestBody ReceptionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(readMapperRest.toResponse(
                        receptionUserCase.save(writeMapperRest.toCreateDto(request))));
    }

    @PutMapping("/{id}/contact")
    public ReceptionReadResponse updateContact(@PathVariable Long id,
                                               @Valid @RequestBody ReceptionUpdateContactRequest request) {
        return readMapperRest.toResponse(
                receptionUserCase.updateContact(writeMapperRest.toUpdateContactDto(request), id));
    }

    @PutMapping("/{id}/sensitive")
    public ReceptionReadResponse updateSensitive(@PathVariable Long id,
                                                 @Valid @RequestBody ReceptionUpdateSensitiveRequest request) {
        return readMapperRest.toResponse(
                receptionUserCase.updateSensitive(writeMapperRest.toUpdateSensitiveDto(request), id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        receptionUserCase.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
