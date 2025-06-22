package com.example.ClinicaDefinitiva.web.controller;



import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.services.TurnoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/turno")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @PostMapping()
    public ResponseEntity<TurnoDto> save(@Valid @RequestBody TurnoDto turnoDto){
        TurnoDto response = turnoService.save(turnoDto);
        return  ResponseEntity.created(URI.create("/api/turno/" + response.getId()))
                .body(response);
    }
    @PutMapping("{id}")
    public TurnoDto update( @Valid @RequestBody long idTurno, TurnoDto turnoDto){
        return turnoService.update(idTurno, turnoDto);
    }
    public List<TurnoDto> findAll(){
        return turnoService.findAll();
    }
    void deleaById(long id){

    }
    @GetMapping("fecha/fecha")
    public List<TurnoDto> findByFecha( @PathVariable LocalDate fecha){
        return turnoService.findByFecha(fecha);
    }

    @GetMapping("/turno/turnoId")
    public List<TurnoDto> findByPacienteId(@PathVariable long idPaciente){
    return turnoService.findByPacienteId(idPaciente);

    }
    @GetMapping("{id}")
   public TurnoDto findById(@PathVariable long idTurno){
       return turnoService.findById(idTurno);
    }

}
