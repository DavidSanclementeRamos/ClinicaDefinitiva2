package com.example.ClinicaDefinitiva.web.controller;


import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.CreateUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.UpdateUsuarioDto;
import com.example.ClinicaDefinitiva.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
//@ResquiredArgsConstructor
@RequestMapping("/api/usuario")
public class UsuarioController {
    private final UsuarioService usuarioService;


    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("{id}")
   public  ReadUsuarioDto findId(@PathVariable long idUsuario){
     return   usuarioService.findId(idUsuario);
   }


    public List<ReadUsuarioDto> findAll(){
        return usuarioService.findAll();
    }

    @GetMapping("/correo{nameCorreo}")
    public List<ReadUsuarioDto> findByCorreo(@PathVariable String correo){
      return null;
    }

    @GetMapping("/usuario{usuarioName}")
    public List<ReadUsuarioDto> findByNombreUsuario(@PathVariable String nombreUsuario){
        return null;
    }

    @PostMapping
    public ResponseEntity<ReadUsuarioDto> save(@Valid @RequestBody CreateUsuarioDto createUsuarioDto){
        ReadUsuarioDto response = usuarioService.save(createUsuarioDto) ;
        return   ResponseEntity.created(URI.create("/api/usuario/" + response.getId_usuario()))
                .body(response);
    }

    @PutMapping("{id}")
    public ReadUsuarioDto update( @Valid @RequestBody long idUsuario, UpdateUsuarioDto updateUsuarioDto){
        return usuarioService.update(idUsuario, updateUsuarioDto);
    }

}
