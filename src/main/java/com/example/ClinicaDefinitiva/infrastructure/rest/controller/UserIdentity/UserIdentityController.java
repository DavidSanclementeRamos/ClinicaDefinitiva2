package com.example.ClinicaDefinitiva.infrastructure.rest.controller.UserIdentity;

import com.example.ClinicaDefinitiva.application.dto.user.PageUserIdentityDto;
import com.example.ClinicaDefinitiva.application.portsInput.userIdentity.UserIdentityUseCase;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.PageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityCreateRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityPageResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityReadResponse;
import com.example.ClinicaDefinitiva.infrastructure.rest.dto.userResponse.UserIdentityUpdateRequest;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity.UserRestToServiceMapper;
import com.example.ClinicaDefinitiva.infrastructure.rest.mapper.userIdentity.UserServiceToRestMapper;
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
@RequestMapping("/api/v1/users")
public class UserIdentityController {

    private final UserServiceToRestMapper restMapper;
    private final UserRestToServiceMapper serviceMapper;
    private final UserIdentityUseCase useCase;

    public UserIdentityController(UserServiceToRestMapper restMapper,
                                  UserRestToServiceMapper serviceMapper,
                                  UserIdentityUseCase useCase) {
        this.restMapper = restMapper;
        this.serviceMapper = serviceMapper;
        this.useCase = useCase;
    }

    @GetMapping
    public PageResponse<UserIdentityPageResponse> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageUserIdentityDto> userPage = useCase.findAll(PageRequest.of(page, size));

        List<UserIdentityPageResponse> content = userPage.getContent()
                .stream()
                .map(restMapper::toRestDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    @GetMapping("/{id}")
    public UserIdentityReadResponse findById(@PathVariable Long id) {
        return restMapper.toRestDto(useCase.findById(id));
    }

    @GetMapping("/email/{email}")
    public PageResponse<UserIdentityPageResponse> findByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageUserIdentityDto> userPage = useCase.findByEmail(email, PageRequest.of(page, size));

        List<UserIdentityPageResponse> content = userPage.getContent()
                .stream()
                .map(restMapper::toRestDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    @GetMapping("/email/{email}/status/{status}")
    public PageResponse<UserIdentityPageResponse> findByEmailAndStatus(
            @PathVariable String email,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageUserIdentityDto> userPage = useCase.findByEmailAndStatus(email, status, PageRequest.of(page, size));

        List<UserIdentityPageResponse> content = userPage.getContent()
                .stream()
                .map(restMapper::toRestDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    @GetMapping("/{id}/status/{status}")
    public PageResponse<UserIdentityPageResponse> findByIdAndStatus(
            @PathVariable Long id,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PageUserIdentityDto> userPage = useCase.findByIdAndStatus(id, status, PageRequest.of(page, size));

        List<UserIdentityPageResponse> content = userPage.getContent()
                .stream()
                .map(restMapper::toRestDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                userPage.getNumber(),
                userPage.getSize(),
                userPage.getTotalElements(),
                userPage.getTotalPages(),
                userPage.isLast()
        );
    }

    @PostMapping
    public ResponseEntity<UserIdentityReadResponse> register(@Valid @RequestBody UserIdentityCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(restMapper.toRestDto(
                        useCase.register(serviceMapper.toCreate(request))));
    }

    @PutMapping("/{id}/login/success")
    public UserIdentityReadResponse recordSuccessfulLogin(@PathVariable Long id) {
        return restMapper.toRestDto(useCase.recordSuccessfulLogin(id));
    }

    @PutMapping("/{id}/login/fail")
    public UserIdentityReadResponse recordFailedLogin(@PathVariable Long id) {
        return restMapper.toRestDto(useCase.recordFailedLogin(id));
    }

    @PutMapping("/{id}")
    public UserIdentityReadResponse editUserData(@PathVariable Long id,
                                        @Valid @RequestBody UserIdentityUpdateRequest request) {
        return restMapper.toRestDto(
                useCase.editUserData(serviceMapper.toUpdate(request), id));
    }

    @PutMapping("/{id}/verify")
    public UserIdentityReadResponse verify(@PathVariable Long id) {
        return restMapper.toRestDto(useCase.verify(id));
    }

    @PutMapping("/{id}/deactivate")
    public UserIdentityReadResponse deactivate(@PathVariable Long id,
                                      @RequestParam String reason) {
        return restMapper.toRestDto(useCase.deactivate(id, reason));
    }

    @PutMapping("/{id}/suspend")
    public UserIdentityReadResponse suspend(@PathVariable Long id,
                                   @RequestParam String reason) {
        return restMapper.toRestDto(useCase.suspend(id, reason));
    }

    @GetMapping("/{id}/sensitive")
    public UserIdentityReadResponse canPerformSensitiveAction(@PathVariable Long id) {
        return restMapper.toRestDto(useCase.canPerformSensitiveAction(id));
    }

}
