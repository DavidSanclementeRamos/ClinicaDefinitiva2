package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;
import com.example.ClinicaDefinitiva.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@PreAuthorize("denyAll()")
@RestController
@RequestMapping(

         "/api/v1/usuarios"
      //  produces = MediaType.APPLICATION_JSON_VALUE
      //  consumes = MediaType.APPLICATION_JSON_VALUE

)
public class UsuarioController {
    private final UsuarioService usuarioService;


    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PreAuthorize("hasAuthority('GET_USUARIO_ID')")
    @GetMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> findId(@PathVariable long id){
     return  ResponseEntity.ok(usuarioService.findId(id));
    }

    @PreAuthorize("hasAuthority('GET_USUARIOS_LIST')")
    @GetMapping
    public ResponseEntity<Page<ReadUsuarioDto>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size); // sin ordenamiento
        Page<ReadUsuarioDto> usuarios = usuarioService.findAll(pageable);
        return ResponseEntity.ok(usuarios);
    }

    @PreAuthorize("hasAuthority('GET_USUARIO_POR_EMAIL')")
    @GetMapping("/buscarUsuarioPorEmail")
    public ResponseEntity<ReadUsuarioDto> findByEmail(@RequestParam String email){
        return ResponseEntity.ok(usuarioService.findByEmail(email));
    }

  /*  @GetMapping("/buscarUsuarioPorRol/{rol}")
    public ResponseEntity<List<ReadUsuarioDto>> findByRol(@PathVariable Roles rol){
        return ResponseEntity.ok(usuarioService.findByRol(rol));
    }*/

    @PreAuthorize("hasAuthority('GET_USUARIO_POR_NOMBRE')")
    @GetMapping("/buscarUsuarioPorNombre/{nombre}")
    public ResponseEntity<ReadUsuarioDto> findByNombreUsuario(
            @PathVariable String nombre){
       return ResponseEntity.ok(usuarioService.findByNombreUsuario(nombre));
    }





    @PreAuthorize("hasAuthority('POST_USUARIOS')")
    @PostMapping
    public ResponseEntity<ReadUsuarioDto> save(
            @Valid @RequestBody CreateUsuarioDto createUsuarioDto, UriComponentsBuilder uriBuilder){
        ReadUsuarioDto response = usuarioService.save(createUsuarioDto) ;

        URI uri = uriBuilder.path("/api/v1/usuarios/{id}")
                .buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PreAuthorize("hasAuthority('PUT_USUARIOS')")
    @PutMapping("/{id}")
    public ResponseEntity<ReadUsuarioDto> update(
            @PathVariable long id,
            @Valid @RequestBody  UpdateUsuarioDto updateUsuarioDto){
        return ResponseEntity.ok(usuarioService.update(id, updateUsuarioDto));
    }


    @PreAuthorize("hasAuthority('DELETE_USUARIOS')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {usuarioService.deleaById(id);}


    }
