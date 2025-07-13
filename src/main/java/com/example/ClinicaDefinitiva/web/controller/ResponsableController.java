package com.example.ClinicaDefinitiva.web.controller;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.persistence.dto.pacienteDto.ReadPacienteDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CambioResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.CreateEndReadResponsableDto;
import com.example.ClinicaDefinitiva.persistence.dto.responsableDto.UpdateResponsableDto;
import com.example.ClinicaDefinitiva.services.ResponsableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
//@ResquiredArgsConstructor
@RequestMapping(
        path = "/api/v1/responsables",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class ResponsableController  {

    private final ResponsableService responsableService;

    public ResponsableController(ResponsableService responsableService) {
        this.responsableService = responsableService;
    }
    @GetMapping("/buscarResponsablePorUsuarioId/{id}")
    public ResponseEntity< Optional<CreateEndReadResponsableDto> >findByUsuario_Id(@PathVariable Long id){

        return ok(responsableService.findByUsuario_Id(id));
    }
    @GetMapping("/buscarResponsablePorPacienteId/{id}")
    public ResponseEntity<List<CreateEndReadResponsableDto>> findByPacientes_Id(@PathVariable Long id){
        return ok(responsableService.findByPacientes_Id(id));
    }
    @GetMapping("/buscarPorDocumento/{documento}")
    public ResponseEntity<Optional<CreateEndReadResponsableDto>> findByDocumento(@PathVariable String documento){
        return ok(responsableService.findByDocumento(documento));
    }

    @GetMapping("/buscarPorTelefono/{telefono}")
    public ResponseEntity<Optional<CreateEndReadResponsableDto>>findByTelefono(@PathVariable String telefono){
        return ok(responsableService.findByTelefono(telefono));
    }

    @GetMapping("/buscarPorTipoRelacion/{tipoRelacion}")
    public ResponseEntity<List<CreateEndReadResponsableDto>> findByTipoRelacion(
            @PathVariable TipoResponsable tipoRelacion){
        return ok(responsableService.findByTipoRelacion(tipoRelacion));
    }

    @PutMapping("/cambio/{id}")
    public ResponseEntity<CreateEndReadResponsableDto> updateCambio(
           @PathVariable long id,
           @Valid @RequestBody CambioResponsableDto cambioResponsableDto){
        return ok(responsableService.updateCambio(id,cambioResponsableDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreateEndReadResponsableDto> update(
            @PathVariable long id,
            @Valid @RequestBody UpdateResponsableDto updateResponsableDto){
       return ResponseEntity.ok(responsableService.update(id, updateResponsableDto));
    }

    @PostMapping
    public ResponseEntity<CreateEndReadResponsableDto> save(
            @Valid @RequestBody  CreateEndReadResponsableDto createEndReadResponsableDto, UriComponentsBuilder uriBuilder){

        CreateEndReadResponsableDto response = responsableService.save(createEndReadResponsableDto);
        URI uri = uriBuilder.path("/api/responsables/{id}")

                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {responsableService.deleaById(id);}


    }
