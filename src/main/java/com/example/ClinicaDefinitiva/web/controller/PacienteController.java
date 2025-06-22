package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.CreatePacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.UpdatePacienteDto;
import com.example.ClinicaDefinitiva.services.PacienteServise;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
//@ResquiredArgsConstructor
@RequestMapping("/api/pacientes" )
public class PacienteController {

    private final PacienteServise pacienteServise;

    public PacienteController(PacienteServise pacienteServise) {
        this.pacienteServise = pacienteServise;
    }


    @GetMapping("/{id}/")
    public ReadPacienteDto findId(@PathVariable long idPaciente){
        return pacienteServise.findId(idPaciente);
    }

    public List<ReadPacienteDto> findAll(){
        return pacienteServise.findAll();
    }


    @GetMapping("/responsable{responsableId}")
    public List<ReadPacienteDto> findByResponsableId(@PathVariable long responsableId){
        return (List<ReadPacienteDto>) pacienteServise.findByResponsableId(responsableId);
    }

    @GetMapping("/turno{turnoId}")
     public List<ReadPacienteDto> findByTurnoId(@PathVariable long turnoId){
        return  pacienteServise.findByTurnoId(turnoId);
     }
    /*@PostMapping
    public ResponseEntity<ReadPacienteDto> save(@Valid @RequestBody CreatePacienteDto createPacienteDto){

        ReadPacienteDto response = pacienteServise.save(createPacienteDto) ;
        return  ResponseEntity.created(URI.create("/api/paciente/" + response.getId()))
                .body(response);
    }*/
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody CreatePacienteDto createPacienteDto) {
        try {
            ReadPacienteDto response = pacienteServise.save(createPacienteDto);
            return ResponseEntity.created(URI.create("/api/paciente/" + response.getId()))
                    .body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    @PutMapping("{id}")
     public ReadPacienteDto update(@Valid @RequestBody long id, UpdatePacienteDto updatePacienteDto){
        return pacienteServise.update(id, updatePacienteDto);
    }

}
