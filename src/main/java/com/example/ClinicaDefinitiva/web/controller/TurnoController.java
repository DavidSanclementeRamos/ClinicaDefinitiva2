package com.example.ClinicaDefinitiva.web.controller;



import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.persistence.dto.TurnoDto;
import com.example.ClinicaDefinitiva.persistence.dto.secretarioDto.ReadSecretarioDto;
import com.example.ClinicaDefinitiva.services.TurnoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping(

        path = "/api/v1/turnos",
        produces = MediaType.APPLICATION_JSON_VALUE

)
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<TurnoDto> findById(@PathVariable long id){
        return ResponseEntity.ok( turnoService.findById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TurnoDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
             Page<TurnoDto> resultado = turnoService.findAll(PageRequest.of(page, size));
        return  ResponseEntity.ok( resultado);
    }




    @GetMapping("/buscarTurnoPorOdontoogoId/{id}")
    public ResponseEntity<List<TurnoDto>> findByOdontologoId(@PathVariable long id){
        return ResponseEntity.ok(turnoService.findByOdontologoId(id));
    }

    @GetMapping("/buscarTurnoPorEstado/{estado}")
    public ResponseEntity<List<TurnoDto>> findByEstado(@PathVariable Estado estado){

        return ResponseEntity.ok(turnoService.findByEstado(estado));
    }


    @GetMapping("/disponibilidad")
    public ResponseEntity<Map<String, String>> verificarDisponibilidad(
            @RequestParam Long idOdontologo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime hora) {

        boolean existe = turnoService.existsByOdontologo_IdAndFechaAndHora(idOdontologo, fecha, hora);
        Map<String, String> respuesta = new HashMap<>();

        if (existe) {
            respuesta.put("mensaje", "El odontólogo ya tiene una cita programada el " + fecha + " a las " + hora + ".");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(respuesta);
        } else {
            respuesta.put("mensaje", "El horario solicitado está disponible para el odontólogo.");
            return ResponseEntity.ok(respuesta);
        }
    }


    @GetMapping("/buscarTurnoPorFeccha")
    public ResponseEntity<List<TurnoDto>> findByFecha(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalDate hasta){
        return ResponseEntity.ok( turnoService.findByFechaBetween(desde, hasta));
    }

    @GetMapping("/buscarTurnoPorPacienteId/{id}")
    public ResponseEntity<List<TurnoDto>>  findByPacienteId(@PathVariable long id){
    return ResponseEntity.ok( turnoService.findByPacienteId(id));

    }


    @PostMapping
    public ResponseEntity<TurnoDto> save(
            @Valid @RequestBody TurnoDto turnoDto, UriComponentsBuilder uriBuilder){
        TurnoDto response = turnoService.save(turnoDto);

        URI uri = uriBuilder.path("/api/secretarios/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TurnoDto> update(
            @PathVariable long idTurno,
            @Valid @RequestBody  TurnoDto turnoDto){
        return ResponseEntity.ok( turnoService.update(idTurno, turnoDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleaById(@PathVariable long id){

        turnoService.deleaById(id);
    }



}
