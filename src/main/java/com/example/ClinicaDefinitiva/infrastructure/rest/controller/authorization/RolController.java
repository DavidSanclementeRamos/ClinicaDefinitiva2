package com.example.ClinicaDefinitiva.infrastructure.rest.controller.authorization;

import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.CreateRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PageRolDto;
import com.example.ClinicaDefinitiva.application.dto.administration.authorization.rol.PermissionDto;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.authorization.RolUseCase;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserId;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.autorization.rol.*;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.authorization.rol.RolReadMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.authorization.rol.RolWriteMapper;
import com.example.ClinicaDefinitiva.infrastructure.security.adapter.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * - Extrae contexto de seguridad (UserId, RolId)
 */

@RestController
@RequestMapping("/api/v1/roles")
@Validated
public class RolController {
    private final RolUseCase rolUseCase;
    private final RolReadMapper rolReadMapper;
    private final RolWriteMapper rolWriteMapper;

    public RolController(RolUseCase rolUseCase, RolReadMapper rolReadMapper, RolWriteMapper rolWriteMapper) {
        this.rolUseCase = rolUseCase;
        this.rolReadMapper = rolReadMapper;
        this.rolWriteMapper = rolWriteMapper;
    }


    @GetMapping("/{id}")
    public ResponseEntity<RolReadResponse> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        return rolUseCase.findById(RolId.of(id), userId, rolId)
                .map(rolReadMapper::toResponse) // ← Mapper de aplicación → REST
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-enum/{rolEnum}")
    public ResponseEntity<RolReadResponse> findByRolEnum(
            @PathVariable String rolEnum,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        return rolUseCase.findByRolEnum(rolEnum, userId, rolId)
                .map(rolReadMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public PageResponse<RolPageResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        Page<PageRolDto> rolPage = rolUseCase.findAll(PageRequest.of(page, size), userId, rolId);

        List<RolPageResponse> content = rolPage.getContent()
                .stream()
                .map(rolReadMapper::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                rolPage.getNumber(),
                rolPage.getSize(),
                rolPage.getTotalElements(),
                rolPage.getTotalPages(),
                rolPage.isLast()
        );
    }

    @GetMapping("/editable")
    public PageResponse<RolPageResponse> findByEditable(
            @RequestParam(defaultValue = "true") boolean editable,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        Page<PageRolDto> rolPage = rolUseCase.findByEditable(editable, PageRequest.of(page, size), userId, rolId);

        List<RolPageResponse> content = rolPage.getContent()
                .stream()
                .map(rolReadMapper::toPageResponse)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                rolPage.getNumber(),
                rolPage.getSize(),
                rolPage.getTotalElements(),
                rolPage.getTotalPages(),
                rolPage.isLast()
        );
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolReadResponse createCustom(
            @Valid @RequestBody RolCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        CreateRolDto dto = rolWriteMapper.toCreateDto(request);
        return rolReadMapper.toResponse(rolUseCase.createCustom(dto, userId, rolId));
    }

    @PostMapping("/{id}/clone")
    public RolReadResponse cloneRole(
            @PathVariable Long id,
            @Valid @RequestBody String newDescription,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        return rolReadMapper.toResponse(rolUseCase.cloneRole(RolId.of(id), newDescription, userId, rolId));
    }

    @PostMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addPermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        PermissionDto dto = rolWriteMapper.toPermissionDto(request);
        rolUseCase.addPermission(RolId.of(id), dto, userId, rolId);
    }

    @DeleteMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        PermissionDto dto = rolWriteMapper.toPermissionDto(request);
        rolUseCase.removePermission(RolId.of(id), dto, userId, rolId);
    }

    @PutMapping("/{id}/permissions")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void setPermissions(
            @PathVariable Long id,
            @Valid @RequestBody Set<PermissionRequest> requests,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        Set<PermissionDto> dtos = requests.stream()
                .map(rolWriteMapper::toPermissionDto)
                .collect(Collectors.toSet());

        rolUseCase.setPermissions(RolId.of(id), dtos, userId, rolId);
    }

    @GetMapping("/{id}/permissions/check")
    public ResponseEntity<PermissionCheckResponse> hasPermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        PermissionDto dto = rolWriteMapper.toPermissionDto(request);
        boolean hasPermission = rolUseCase.hasPermission(RolId.of(id), dto, userId, rolId);

        return ResponseEntity.ok(new PermissionCheckResponse(hasPermission));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        rolUseCase.deleteById(RolId.of(id), userId, rolId);
    }

    @PostMapping("/{id}/activate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void activate(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        rolUseCase.activate(RolId.of(id), request.reason(), userId, rolId);
    }

    @PostMapping("/{id}/deactivate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deactivate(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        rolUseCase.deactivate(RolId.of(id), request.reason(), userId, rolId);
    }

    @PostMapping("/{id}/suspend")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void suspend(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        rolUseCase.suspend(RolId.of(id), request.reason(), userId, rolId);
    }

    @PostMapping("/{id}/mark-deleted")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markDeleted(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserId userId = userDetails.getId();
        RolId rolId = userDetails.getActiveRolId();

        rolUseCase.markDeleted(RolId.of(id), request.reason(), userId, rolId);
    }
}
