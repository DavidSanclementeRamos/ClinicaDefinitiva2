package com.example.ClinicaDefinitiva.infrastructure.rest.controller;




import com.example.ClinicaDefinitiva.application.dto.service.CreateProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.service.ReadProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.dto.service.UpdateProvidedServiceDto;
import com.example.ClinicaDefinitiva.application.mapper.ProvidedServiceMapper;
import com.example.ClinicaDefinitiva.application.usecase.ProvidedServiceUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.DentalServiceValidation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Validated
@RestController
@RequestMapping("/api/dental-services")
public class DentalServiceController {

    private final ProvidedServiceUseCase service;
    private final ProvidedServiceMapper mapper;

    public DentalServiceController(ProvidedServiceUseCase service, ProvidedServiceMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ReadProvidedServiceDto> create(@Valid @RequestBody CreateProvidedServiceDto dto) {
        // validaciones adicionales de coherencia entre serviceType y details
        DentalServiceValidation.validateCreateDto(dto);

        var domain = mapper.toDomain(dto);
        var created = service.create(dto); // ApplicationService espera DTO y usa mapper internamente
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadProvidedServiceDto> findById(@PathVariable("id") String id) {
        var dto = service.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ReadProvidedServiceDto>> list(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int p = page.orElse(0);
        int s = size.orElse(20);
        Pageable pageable = PageRequest.of(Math.max(0, p), Math.max(1, s));
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/by-category")
    public ResponseEntity<Page<ReadProvidedServiceDto>> findByCategory(
            @RequestParam("category") String category,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int p = page.orElse(0);
        int s = size.orElse(20);
        Pageable pageable = PageRequest.of(Math.max(0, p), Math.max(1, s));
        return ResponseEntity.ok(service.findByCategory(category, pageable));
    }

    @GetMapping("/by-type")
    public ResponseEntity<Page<ReadProvidedServiceDto>> findByServiceType(
            @RequestParam("serviceType") String serviceType,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        int p = page.orElse(0);
        int s = size.orElse(20);
        Pageable pageable = PageRequest.of(Math.max(0, p), Math.max(1, s));
        return ResponseEntity.ok(service.findByServiceType(serviceType, pageable));
    }

    @GetMapping("/orthodontic/by-duration")
    public ResponseEntity<Page<ReadProvidedServiceDto>> findOrthodonticByDuration(
            @RequestParam("months") Integer months,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        if (months == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "months is required");
        int p = page.orElse(0);
        int s = size.orElse(20);
        Pageable pageable = PageRequest.of(Math.max(0, p), Math.max(1, s));
        return ResponseEntity.ok(service.findOrthodonticByTreatmentDuration(months, pageable));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReadProvidedServiceDto> update(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateProvidedServiceDto dto) {
        // Si DTO contiene details, validar coherencia
        DentalServiceValidation.validateUpdateDto(dto);
        var updated = service.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") String id) {
        service.delete(id);
    }
}
