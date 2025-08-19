package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;
import com.example.ClinicaDefinitiva.services.SecretarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping(

        path = "/api/v1/secretarios",
        produces = MediaType.APPLICATION_JSON_VALUE

)
public class SecretarioController {



    private final SecretarioService secretarioService;

    public SecretarioController(SecretarioService secretarioService) {
        this.secretarioService = secretarioService;
    }

    @PreAuthorize("hasAuthority('GET_SECRETARIO_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<ReadSecretarioDto> findId(@PathVariable long id){
       return ResponseEntity.ok(secretarioService.findId(id));
    }

    @PreAuthorize("hasAuthority('GET_SECRETARIOS_LIST')")
    @GetMapping
    public ResponseEntity<Page<ReadSecretarioDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<ReadSecretarioDto> resultado = secretarioService.findAll(PageRequest.of(page, size));

        return  ResponseEntity.ok( resultado);
    }

    @PreAuthorize("hasAuthority('GET_SECRETARIO_POR_NOMBRE')")
    @GetMapping("/buscarSecretarioPorNombre/{nombre}")
    public ResponseEntity<List<ReadSecretarioDto>> findByNombreContainingIgnoreCase(
            @PathVariable String nombre){

        return ResponseEntity.ok(secretarioService.findByNombreContainingIgnoreCase(nombre));

    }

    @PreAuthorize("hasAuthority('GET_SECRETARIO_SECTOR')")
    @GetMapping("/buscarSecretarioPorSector{sector}")
    public ResponseEntity<List<ReadSecretarioDto>> findBySector(@PathVariable Sector sector){
        return ResponseEntity.ok( secretarioService.findBySector(sector));
    }

    @PreAuthorize("hasAuthority('GET_SECRETARIO_POR_USUARIO')")
    @GetMapping("/buscarSecretarioPorUsuarioId/{id}")
    public ResponseEntity<ReadSecretarioDto> findByUsuario(@PathVariable long Id){
        return ResponseEntity.ok(secretarioService.findByUsuarioId(Id));
    }

    @PreAuthorize("hasAuthority('POST_SECRETARIO')")
    @PostMapping
    public ResponseEntity<ReadSecretarioDto> save(
            @Valid @RequestBody CreateSecretarioDto createSecretarioDto, UriComponentsBuilder uriBuilder){

        ReadSecretarioDto response = secretarioService.save(createSecretarioDto);

        URI uri = uriBuilder.path("/api/secretarios/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAuthority('PUT_SECRETARIO')")
    @PutMapping("{id}")
    public ResponseEntity<ReadSecretarioDto> update(
            @PathVariable long id,
            @Valid @RequestBody  UpdateSecretarioDto updateSecretarioDto){
        return ResponseEntity.ok( secretarioService.update(id, updateSecretarioDto));
    }

    @PreAuthorize("hasAuthority('DELETE_SECRETARIO')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {secretarioService.deleaById(id);}


    }
