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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping(

        path = "/api/v1/secretarios",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE

)
public class SecretarioController {



    private final SecretarioService secretarioService;

    public SecretarioController(SecretarioService secretarioService) {
        this.secretarioService = secretarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadSecretarioDto> findId(@PathVariable long id){
       return ResponseEntity.ok(secretarioService.findId(id));
    }

    @GetMapping
    public ResponseEntity<ReadSecretarioDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort){
        Page<ReadSecretarioDto> resultado = secretarioService.findAll(PageRequest.of(page, size, Sort.by(parseSort(sort))));
      return  ResponseEntity.ok((ReadSecretarioDto) resultado);
    }

    @GetMapping("/buscarSecretarioPorNombre/{nombre}")
    public ResponseEntity<List<ReadSecretarioDto>> findByNombreContainingIgnoreCase(
            @PathVariable String nombre){

        return ResponseEntity.ok(secretarioService.findByNombreContainingIgnoreCase(nombre));

    }


    @GetMapping("/buscarSecretarioPorSector{sector}")
    public ResponseEntity<List<ReadSecretarioDto>> findBySector(@PathVariable Sector sector){
        return ResponseEntity.ok( secretarioService.findBySector(sector));
    }

    @GetMapping("/buscarSecretarioPorUsuarioId/{id}")
    public ResponseEntity<Optional<ReadSecretarioDto>> findByUsuario(@PathVariable long Id){
        return ResponseEntity.ok(secretarioService.findByUsuarioId(Id));
    }

    @PostMapping
    public ResponseEntity<ReadSecretarioDto> save(
            @Valid @RequestBody CreateSecretarioDto createSecretarioDto, UriComponentsBuilder uriBuilder){

        ReadSecretarioDto response = secretarioService.save(createSecretarioDto);

        URI uri = uriBuilder.path("/api/secretarios/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReadSecretarioDto> update(
            @PathVariable long id,
            @Valid @RequestBody  UpdateSecretarioDto updateSecretarioDto){
        return ResponseEntity.ok( secretarioService.update(id, updateSecretarioDto));
    }


    // Método auxiliar para Sort
    private Sort.Order[] parseSort(String[] sort) {
        return Stream.of(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .toArray(Sort.Order[]::new);
    }

}
