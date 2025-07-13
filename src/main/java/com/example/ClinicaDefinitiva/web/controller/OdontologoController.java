package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.exceptions.entityNotFount.OdontologoNotfountException;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;
import com.example.ClinicaDefinitiva.services.OdontologoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
//@ResquiredArgsConstructor
@RequestMapping(
        path = "/api/v1/odontologos",
        produces = MediaType.APPLICATION_JSON_VALUE
)

public class OdontologoController {

    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ReadOdontologoDto> findId(@PathVariable long id) {
        return odontologoService.findId(id).map(ResponseEntity::ok)
                .orElseThrow(OdontologoNotfountException::new);
    }



    @GetMapping
    public ResponseEntity<Page<ReadOdontologoDto>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size)
    {
        Page<ReadOdontologoDto> resultado = odontologoService.findAll(PageRequest.of(page, size));

            return ResponseEntity.ok( resultado);

    }


     @GetMapping("/buscarUsuario/{id}")
    public ResponseEntity<Optional<ReadOdontologoDto>> findByUsuario_Id(@PathVariable Long id){
        return ResponseEntity.ok(odontologoService.findByUsuario_Id(id));
    }

    @GetMapping("/buscarPorEspecialidad/{especialidad}")
    public ResponseEntity<List<ReadOdontologoDto>> findByEspecialidadContainingIgnoreCase(
            @PathVariable Especialidades especialidad){

        return ResponseEntity.ok(odontologoService
                .findByEspecialidad(especialidad));
    }

    @GetMapping("/buscarPorFechaTurno")
    public ResponseEntity<List<ReadOdontologoDto>> findConTurnosEntreFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta){
        return ResponseEntity.ok(odontologoService.findConTurnosEntreFechas(desde, hasta));
    }


    @PostMapping
   public ResponseEntity<ReadOdontologoDto> save(@Valid @RequestBody CreateOdontologoDto createOdontologoDto, UriComponentsBuilder uriBuilder){
        ReadOdontologoDto response = odontologoService.save(createOdontologoDto);

        URI uri = uriBuilder.path("/api/odontologos/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<ReadOdontologoDto> update (
            @PathVariable long id,
            @Valid @RequestBody UpdateOdontologoDto updateOdontologoDto){

        ReadOdontologoDto atualizado = odontologoService.update(id, updateOdontologoDto);
        return ResponseEntity.ok(atualizado);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) { odontologoService.deleaById(id);}


    }
