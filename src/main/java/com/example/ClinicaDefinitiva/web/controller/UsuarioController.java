package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;
import com.example.ClinicaDefinitiva.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RestController
@RequestMapping(

        path = "/api/v1/usuarios",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE

)
public class UsuarioController {
    private final UsuarioService usuarioService;


    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> findId(@PathVariable long id){
     return  ResponseEntity.ok(usuarioService.findId(id));
    }

    @GetMapping()
    public ResponseEntity<ReadUsuarioDto> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort){
        Page<ReadUsuarioDto> response = usuarioService.findAll(PageRequest.of(page, size, Sort.by(parseSort(sort))));
        return ResponseEntity.ok((ReadUsuarioDto) response);
    }

    @GetMapping("/buscarUsuarioPorEmail/{email}")
    public ResponseEntity<Optional<ReadUsuarioDto>> findByEmail(@PathVariable String email){
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

    @GetMapping("/buscarUsuarioPorRol/{rol}")
    public ResponseEntity<List<ReadUsuarioDto>> findByRol(@PathVariable Roles rol){
        return ResponseEntity.ok(usuarioService.findByRol(rol));
    }

    @GetMapping("/buscarUsuarioPorNombre/{nombre}")
    public ResponseEntity<Optional<ReadUsuarioDto>> findByNombreUsuario(
            @PathVariable String nombre){
       return ResponseEntity.ok(usuarioService.findByNombreUsuario(nombre));
    }






    @PostMapping
    public ResponseEntity<ReadUsuarioDto> save(
            @Valid @RequestBody CreateUsuarioDto createUsuarioDto, UriComponentsBuilder uriBuilder){
        ReadUsuarioDto response = usuarioService.save(createUsuarioDto) ;

        URI uri = uriBuilder.path("/api/secretarios/{id}")
                .buildAndExpand(response.getId_usuario()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> update(
            @PathVariable long id,
            @Valid @RequestBody  UpdateUsuarioDto updateUsuarioDto){
        return ResponseEntity.ok(usuarioService.update(id, updateUsuarioDto));
    }

    // Método auxiliar para Sort
    private Sort.Order[] parseSort(String[] sort) {
        return Stream.of(sort)
                .map(s -> {
                    String[] parts = s.split(",");
                    return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
                })
                .toArray(Sort.Order[]::new);
    }


}
