package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.persistence.dto.HorarioDto;
import com.example.ClinicaDefinitiva.services.HorarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/horario")
public class HorarioController {

    private final HorarioService horarioService;

    public HorarioController(HorarioService horarioService) {
        this.horarioService = horarioService;
    }

    @GetMapping("{id}")
    public HorarioDto findId(@PathVariable long horarioId){
        return horarioService.findId(horarioId);
    }

    @PostMapping()
    public ResponseEntity<HorarioDto> save(@Valid @RequestBody HorarioDto horarioDto){
        HorarioDto response = horarioService.save(horarioDto);
        return  ResponseEntity.created(URI.create("/api/horario/" + response.getId()))
                .body(response);
    }

    @PutMapping("{id}")
    public HorarioDto update( @Valid @RequestBody long horarioId, HorarioDto horarioDto){
        return horarioService.update(horarioId, horarioDto);
    }

    public List<HorarioDto> findAll(){
      return  horarioService.findAll();
    }

  /*  @GetMapping("/odontologo{odontologoId}")
    public List<HorarioDto> findByOdontologo(@PathVariable long odontologoId){
        return horarioService.findByOdontologo(odontologoId);
    }*/

   // void deleaById(long id);
}
