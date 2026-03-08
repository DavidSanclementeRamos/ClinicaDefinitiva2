package com.example.ClinicaDefinitiva.infrastructure.rest.controller.dentalService;





import com.example.ClinicaDefinitiva.application.dto.dentalService.*;
import com.example.ClinicaDefinitiva.application.dto.dentalService.ReadServiceDto;
import com.example.ClinicaDefinitiva.application.portsInput.dentalService.ProvidedServiceUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.dentalService.service.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService.ProvidedServiceRestReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.dentalService.ProvidedServiceRestWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@RequestMapping("/api/v1/services")
public class ProvidedServiceController {

    private final ProvidedServiceUseCase useCase;
    private final ProvidedServiceRestReadMapper readMapper;
    private final ProvidedServiceRestWriteMapper writeMapper;

    public ProvidedServiceController(ProvidedServiceUseCase useCase,
                                     ProvidedServiceRestReadMapper readMapper,
                                     ProvidedServiceRestWriteMapper writeMapper) {
        this.useCase = useCase;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadServiceResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadServiceDto dto = useCase.findById(ServiceId.of(id), requesterId, requesterRolId);
        return ResponseEntity.ok(readMapper.toRest(dto));
    }


    @GetMapping
    public ResponseEntity<Page<PageServiceResponse>> findAll(
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageServiceDto> services = useCase.findAll(pageable, requesterId, requesterRolId);
        Page<PageServiceResponse> response = services.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<Page<PageServiceResponse>> findByStatus(
            @PathVariable String status,
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageServiceDto> services = useCase.findByStatus(
                status,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageServiceResponse> response = services.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Page<PageServiceResponse>> findByCategory(
            @PathVariable String category,
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageServiceDto> services = useCase.findByCategory(
                category,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageServiceResponse> response = services.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<Page<PageServiceResponse>> findByType(
            @PathVariable String type,
            @PageableDefault(size = 20, sort = "name") Pageable pageable,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        Page<PageServiceDto> services = useCase.findByType(
                type,
                pageable,
                requesterId,
                requesterRolId
        );
        Page<PageServiceResponse> response = services.map(readMapper::toPageRest);

        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<ReadServiceResponse> create(
            @Valid @RequestBody CreateServiceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        CreateServiceDto dto = writeMapper.toServiceCreate(request);
        ReadServiceDto created = useCase.create(dto, requesterId, requesterRolId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readMapper.toRest(created));
    }

    @PutMapping("/{id}/information")
    public ResponseEntity<ReadServiceResponse> updateInformation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateServiceInfoRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateServiceInfoDto dto = writeMapper.toServiceUpdateInfo(request);
        ReadServiceDto updated = useCase.updateInformation(
                dto,
                ServiceId.of(id),
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PatchMapping("/{id}/rate")
    public ResponseEntity<ReadServiceResponse> updateRate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateServiceRateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateServiceRateDto dto = writeMapper.toServiceUpdateRate(request);
        ReadServiceDto updated = useCase.updateRate(
                dto,
                ServiceId.of(id),
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }

    @PutMapping("/{id}/details")
    public ResponseEntity<ReadServiceResponse> updateDetails(
            @PathVariable Long id,
            @Valid @RequestBody UpdateServiceDetailsRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        UpdateServiceDetailsDto dto = writeMapper.toServiceUpdateDetails(request);
        ReadServiceDto updated = useCase.updateDetails(
                dto,
                ServiceId.of(id),
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(updated));
    }


    @PatchMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable Long id,
            @RequestParam String reason,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.deactivate(
                ServiceId.of(id),
                reason,
                requesterId,
                requesterRolId
        );
    }

    @PatchMapping("/{id}/reactivate")
    public ResponseEntity<ReadServiceResponse> reactivate(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        ReadServiceDto reactivated = useCase.reactivate(
                ServiceId.of(id),
                requesterId,
                requesterRolId
        );

        return ResponseEntity.ok(readMapper.toRest(reactivated));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserIdentityId requesterId = userDetails.getId();
        RolId requesterRolId = userDetails.getActiveRolId();

        useCase.deleteById(
                ServiceId.of(id),
                requesterId,
                requesterRolId
        );
    }
}
