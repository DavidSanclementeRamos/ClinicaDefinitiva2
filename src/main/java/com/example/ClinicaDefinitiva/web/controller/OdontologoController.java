package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.CreateOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.ReadOdontologoDto;
import com.example.ClinicaDefinitiva.persistence.dto.odontologoDto.UpdateOdontologoDto;
import com.example.ClinicaDefinitiva.services.OdontologoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
//@ResquiredArgsConstructor
@RequestMapping("/api/odontologos" )

public class OdontologoController {

    private final OdontologoService odontologoService;

    public OdontologoController(OdontologoService odontologoService) {
        this.odontologoService = odontologoService;
    }

    @GetMapping
    public List<ReadOdontologoDto> findAll() {
        return odontologoService.findAll();
    }

    @GetMapping("/{id}/")
    public ReadOdontologoDto findId(@PathVariable long idOdontologo) {
        return odontologoService.findId(idOdontologo);
    }

    @GetMapping("/turno/{idTurno}")
    public List<ReadOdontologoDto> findByTurnoId(@PathVariable long turnoId) {
        return (List<ReadOdontologoDto>) odontologoService.findByTurnoId(turnoId);
    }

    @GetMapping(" /horario/{}idHorario")
    public List<ReadOdontologoDto> findByHorarioId(@PathVariable long horarioId){
        return (List<ReadOdontologoDto>) odontologoService.findByHorarioId(horarioId);
    }

    @PostMapping
   public ResponseEntity<ReadOdontologoDto> save(@Valid @RequestBody CreateOdontologoDto createOdontologoDto){
        ReadOdontologoDto response = odontologoService.save(createOdontologoDto);
        return ResponseEntity.created(URI.create("/api/odontologos/" + response.getId()))
                .body(response);
    }

    @PutMapping("{id}")
    public ReadOdontologoDto update (long id, @Valid @RequestBody UpdateOdontologoDto updateOdontologoDto){

        return odontologoService.update(id, updateOdontologoDto);
    }



}
