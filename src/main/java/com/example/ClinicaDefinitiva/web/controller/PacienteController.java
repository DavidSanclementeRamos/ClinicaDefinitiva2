package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.UpdatePacienteDto;
import com.example.ClinicaDefinitiva.services.PacienteServise;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/v1/pacientes",
        produces = MediaType.APPLICATION_JSON_VALUE

)
public class PacienteController {

    private final PacienteServise pacienteServise;

    public PacienteController(PacienteServise pacienteServise) {
        this.pacienteServise = pacienteServise;
    }

    @PreAuthorize("hasAuthority('GET_PACIENTE_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<ReadPacienteDto> findId(@PathVariable long id){
        return ResponseEntity.ok( pacienteServise.findId(id));
    }

    @PreAuthorize("hasAuthority('GET_PACIENTE_LIST')")
    @GetMapping
    public ResponseEntity<Page< ReadPacienteDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<ReadPacienteDto> resultado = pacienteServise.findAll(PageRequest.of(page, size));
        Pageable pageable = PageRequest.of(page, size); // sin ordenamiento

        return  ResponseEntity.ok( resultado);
    }

    @PreAuthorize("hasAuthority('GET_PACIENTE_POR_NOMBRE')")
    @GetMapping("/buscarPorNombre/{nombre}")
    public ResponseEntity<List<ReadPacienteDto>> findByNombreContainingIgnoreCase(@PathVariable String nombre){

        return ResponseEntity.ok(pacienteServise.findByNombreContainingIgnoreCase(nombre));
    }

    @PreAuthorize("hasAuthority('GET_PACIENTE_POR_DOCUMENTO')")
    @GetMapping("/buscarPorDocumento/{documento}")
    public ResponseEntity<ReadPacienteDto> findByDocumento(@PathVariable String documento){

        return ResponseEntity.ok(pacienteServise.findByDocumento(documento));
    }


    @PreAuthorize("hasAuthority('GET_PACIENTE_POR_USUARIO')")
    @GetMapping("/buscarPorUsuarioId/{id}")
    public ResponseEntity<ReadPacienteDto> findByUsuario_Id(@PathVariable Long id){

        return ResponseEntity.ok(pacienteServise.findByUsuario_Id(id));
    }

    @PreAuthorize("hasAuthority('GET_PACIENTE_TURNO')")
    @GetMapping("/buscarPorFechaDeTurno")
    public ResponseEntity<List<ReadPacienteDto>> findConTurnosParaFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate fecha){

        return ResponseEntity.ok(pacienteServise.findConTurnosParaFecha(fecha));
    }



    @PreAuthorize("hasAuthority('POST_PACIENTE')")
    @PostMapping
    public ResponseEntity<ReadPacienteDto> save(@Valid @RequestBody CreatePacienteDto createPacienteDto, UriComponentsBuilder uriBuilder) {
        ReadPacienteDto response = pacienteServise.save(createPacienteDto);
        URI uri = uriBuilder.path("/api/pacientes/{id}")

                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAuthority('PUT_PACIENTE')")
    @PutMapping("{id}")
     public ResponseEntity<ReadPacienteDto> update(
             @PathVariable long id,
             @Valid @RequestBody  UpdatePacienteDto updatePacienteDto){
        ReadPacienteDto atualizar =  pacienteServise.update(id, updatePacienteDto);
        return ResponseEntity.ok(atualizar);
    }

    @PreAuthorize("hasAuthority('DELETE_PACIENTE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {pacienteServise.deleaById(id);}


    }
