package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.CreateSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.UpdateSecretarioDto;
import com.example.ClinicaDefinitiva.services.SecretarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/secretari@")
public class SecretarioController {



    private final SecretarioService secretarioService;

    public SecretarioController(SecretarioService secretarioService) {
        this.secretarioService = secretarioService;
    }


    public List<ReadSecretarioDto> findAll(){
      return  secretarioService.findAll();
    }

    @GetMapping("/sector{sector}")
    public List<ReadSecretarioDto> findBySector(@PathVariable String sector){
        return secretarioService.findBySector(sector);
    }

    @GetMapping("/usuario/{usuario}")
    public List<ReadSecretarioDto> findByUsuario(@PathVariable long usuarioId){
        return secretarioService.findByUsuario(usuarioId);
    }

    @PostMapping
    public ResponseEntity<ReadSecretarioDto> save(@Valid @RequestBody CreateSecretarioDto createSecretarioDto){
        ReadSecretarioDto response = secretarioService.save(createSecretarioDto);
        return  ResponseEntity.created(URI.create("/api/secretari@/" + response.getId()))
                .body(response);
    }

    @PutMapping("{id}")
    public ReadSecretarioDto update(@Valid @RequestBody long id, UpdateSecretarioDto updateSecretarioDto){
        return secretarioService.update(id, updateSecretarioDto);
    }



}
