package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.exceptions.entityNotFount.HorarioNotfountException;
import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.services.HorarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(
        path = "/api/v1/horarios",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
)
public class HorarioController {

    private final HorarioService horarioService;


    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioDto> findId(@PathVariable long id){
        return horarioService.findId(id)
                .map( ResponseEntity::ok)
                .orElseThrow(HorarioNotfountException::new);
    }

    @GetMapping
    public ResponseEntity<Page<HorarioDto>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size); // sin ordenamiento
        Page<HorarioDto> resultado = horarioService.findAll(PageRequest.of(page, size));

        return ResponseEntity.ok(resultado);

    }

    @PostMapping
    public ResponseEntity<HorarioDto> save(@Valid @RequestBody HorarioDto horarioDto, UriComponentsBuilder uriBuilder){
        HorarioDto response = horarioService.save(horarioDto);

        URI uri = uriBuilder.path("/api/horarios/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);

    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioDto> update(
            @PathVariable  long id,
            @Valid @RequestBody HorarioDto dto){
        HorarioDto actualizado = horarioService.update(id, dto);
        return ResponseEntity.ok(actualizado);

    }



    @GetMapping("/odontologos/{odontologoId}")
    public ResponseEntity<HorarioDto> findByOdontologo_id(@PathVariable long odontologoId){
        return ResponseEntity.ok((HorarioDto) horarioService.findByOdontologo_Id(odontologoId));
    }


    @GetMapping("/disponibilidadPorDia")
    public ResponseEntity<List<HorarioDto>> findByDiaYHora(
            @RequestParam DayOfWeek dia,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hasta) {
        return ResponseEntity.ok( horarioService
                .findByDiaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(dia, desde, hasta));

    }
    @GetMapping("/rangoDeFecha")
    public ResponseEntity<List<HorarioDto>> findByFechaBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(horarioService.findByFechaBetween(desde, hasta));
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        horarioService.deleaById(id);
    }



}
