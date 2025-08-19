package com.example.ClinicaDefinitiva.service;

import com.example.ClinicaDefinitiva.exceptions.entityNotFount.UsuarioNotfoundException;
import com.example.ClinicaDefinitiva.mapper.UsuarioMapperResponse;
import com.example.ClinicaDefinitiva.persistence.dto.usuarioDto.ReadUsuarioDto;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import com.example.ClinicaDefinitiva.repository.UsuarioRepository;
import com.example.ClinicaDefinitiva.services.impl.UsuarioImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapperResponse usuarioMapper;

    @InjectMocks
    private UsuarioImpl usuarioService;

    // Ejemplo de test para findId
    @Test
    void findId_deberiaRetornarUsuarioCuandoExiste() {
        long id = 1L;
        Usuario usuario = new Usuario();
        ReadUsuarioDto dto = new ReadUsuarioDto();

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));
        when(usuarioMapper.readUsuarioDto(usuario)).thenReturn(dto);

        ReadUsuarioDto resultado = usuarioService.findId(id);

        assertEquals(dto, resultado);
        verify(usuarioRepository).findById(id);
        verify(usuarioMapper).readUsuarioDto(usuario);
    }

    @Test
    void findId_deberiaLanzarExcepcionCuandoNoExiste() {
        long id = 99L;

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UsuarioNotfoundException.class, () -> usuarioService.findId(id));
        verify(usuarioRepository).findById(id);
    }

    // Puedes seguir con findAll, findByEmail, save, update, deleteById...
}
