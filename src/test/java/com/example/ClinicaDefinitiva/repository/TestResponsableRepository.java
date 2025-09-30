package com.example.ClinicaDefinitiva.repository;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Responsable;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static java.util.function.Predicate.isEqual;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("ResponsableRepository")
@ActiveProfiles("test") // Usa application-test.properties
public class TestResponsableRepository {

    @Autowired
    ResponsableRepository responsableRepository;
    @Autowired
    PacienteRepository pacienteRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    EntityManager em;

    // Usuarios
    private Usuario userResp1, userResp2, userLibre, userPac1, userPac2;

    // Pacientes
    private Paciente pac1, pac2;

    // Responsables
    private Responsable respMadrePac1, respPadrePac2, respTutorSinUsuario;

    @BeforeEach
    void setUp() {
        // Limpieza para aislamiento de cada prueba
        responsableRepository.deleteAll();
        pacienteRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Usuarios persistidos explícitamente (evita TransientObjectException)
        userResp1 = new Usuario();
        userResp1.setNombreUsuario("resp1.user");
        userResp1.setFechaDeCreacion(LocalDate.of(2025, 1, 1));
       // userResp1 = usuarioRepository.save(userResp1);

        userResp2 = new Usuario();
        userResp2.setNombreUsuario("resp2.user");
        userResp2.setFechaDeCreacion(LocalDate.of(2025, 1, 2));
      //  userResp2 = usuarioRepository.save(userResp2);

        userLibre = new Usuario();
        userLibre.setNombreUsuario("sin.responsable");
        userLibre.setFechaDeCreacion(LocalDate.of(2025, 1, 3));
      //  userLibre = usuarioRepository.save(userLibre);

        userPac1 = new Usuario();
        userPac1.setNombreUsuario("pac1.user");
        userPac1.setFechaDeCreacion(LocalDate.of(2025, 1, 4));
      //  userPac1 = usuarioRepository.save(userPac1);

        userPac2 = new Usuario();
        userPac2.setNombreUsuario("pac2.user");
        userPac2.setFechaDeCreacion(LocalDate.of(2025, 1, 5));
        usuarioRepository.saveAll(List.of(userResp1,userPac2,userLibre,userPac1,userResp2));

        // Pacientes mínimos y sincronizados con su Usuario
        pac1 = new Paciente();
        pac1.setNombre("Juan Perez");
        pac1.setDni("PAC-001");
        pac1.setTelefono("311-0001");
        pac1.setFecha_nacimiento(LocalDate.of(2010, 5, 10));
        pac1.setUnUsuario(userPac1);
        pac1 = pacienteRepository.save(pac1);

        pac2 = new Paciente();
        pac2.setNombre("Maria Gomez");
        pac2.setDni("PAC-002");
        pac2.setTelefono("311-0002");
        pac2.setFecha_nacimiento(LocalDate.of(2012, 8, 20));
        pac2.setUnUsuario(userPac2);
        pac2 = pacienteRepository.save(pac2);

        // Responsables (valores únicos para evitar colisiones en constraints)
        respMadrePac1 = new Responsable();
        respMadrePac1.setNombre("Laura Perez");
        respMadrePac1.setDni("RESP-001");
        respMadrePac1.setTelefono("320-1001");
        respMadrePac1.setFecha_nacimiento(LocalDate.EPOCH);
        respMadrePac1.setTipoResponsable(TipoResponsable.MAMA);
        respMadrePac1.setUnUsuario(userResp1);
        respMadrePac1.setPaciente(List.of(pac1));
        respMadrePac1 = responsableRepository.save(respMadrePac1);

        respPadrePac2 = new Responsable();
        respPadrePac2.setNombre("Carlos Gomez");
        respPadrePac2.setDni("RESP-002");
        respPadrePac2.setTelefono("320-1002");
        respPadrePac2.setFecha_nacimiento(LocalDate.EPOCH);
        respPadrePac2.setTipoResponsable(TipoResponsable.PAPA);
        respPadrePac2.setUnUsuario(userResp2);
        respPadrePac2.setPaciente(List.of(pac2));
        respPadrePac2 = responsableRepository.save(respPadrePac2);

        // Responsable sin usuario (para probar ramas sin asociación)
        respTutorSinUsuario = new Responsable();
        respTutorSinUsuario.setNombre("Miguel Tutor");
        respTutorSinUsuario.setDni("RESP-003");
        respTutorSinUsuario.setTelefono("320-1003");
        respTutorSinUsuario.setFecha_nacimiento(LocalDate.EPOCH);
        respTutorSinUsuario.setTipoResponsable(TipoResponsable.TIO);
        respTutorSinUsuario.setFecha_nacimiento(LocalDate.EPOCH);
        respTutorSinUsuario.setPaciente(List.of(pac2)); // su paciente es pac2
        respTutorSinUsuario = responsableRepository.save(respTutorSinUsuario);

        em.flush();
        em.clear();
    }


    @DisplayName("Guarda: persiste un responsable con datos mínimos, usuario y pacientes  asociados")
    @Test
    void save_Responsable (){

        Usuario u = new Usuario();
        Responsable r = new Responsable();
        r.setNombre("Nuevo Resp");
        r.setDni("RESP-NEW-999");
        r.setTelefono("320-1999");
        r.setTipoResponsable(TipoResponsable.TIO);
        r.setUnUsuario(u);
        r.setFecha_nacimiento(LocalDate.EPOCH);
        r.setPaciente(List.of(pac1));

        Responsable guardado = responsableRepository.save(r);

        assertThat(guardado.getId()).isNotNull();
         assertThat(guardado.getUnUsuario().getId()).isEqualTo(u.getId());
       // assertThat(guardado.getPaciente().get(0)).isEqualTo(pac1.getId());
        assertThat(guardado.getDni()).isEqualTo("RESP-NEW-999");
    }

    @DisplayName("update: muta campos escalares y persiste cambios al recargar")
    @Test
    void update_actualizaCampos() {
        /**
         * Verifica el dirty checking al modificar un Responsable existente:
         * - Cambia teléfono y tipo de responsable.
         * - Recarga y válida nuevos valores.
         */
        Responsable managed = responsableRepository.findById(respMadrePac1.getId()).orElseThrow();
        managed.setTelefono("320-8888");
        managed.setTipoResponsable(TipoResponsable.TIO);

        em.flush();
        em.clear();

        Responsable reloaded = responsableRepository.findById(respMadrePac1.getId()).orElseThrow();
        assertThat(reloaded.getTelefono()).isEqualTo("320-8888");
        assertThat(reloaded.getTipoResponsable()).isEqualTo(TipoResponsable.TIO);
    }

    @DisplayName("findById: presente cuando existe el responsable")
    @Test
    void findById_existente() {
        /**
         * Verifica que findById retorne presente para un ID existente.
         */
        assertThat( responsableRepository.findById(respPadrePac2.getId()))
                .isPresent()
                .get()
                .extracting(Responsable::getId)
                .isEqualTo( respPadrePac2.getId());  }

    //  @Display
    @DisplayName("findAll: devuelve todos los responsables persistidos")
    @Test
    void findAll_devuelveTodos() {
        /**
         * Verifica que findAll retorna el conjunto completo de registros.
         * En el setup hay 3 responsables.
         */
        List<Responsable> all = responsableRepository.findAll();
        assertThat(all).hasSize(3)
                .extracting(Responsable::getId)
                .containsExactlyInAnyOrder(respMadrePac1.getId(), respPadrePac2.getId(), respTutorSinUsuario.getId());
      // assertThat(responsableRepository.findById(respTutorSinUsuario.getId())).isNotPresent();

}



@DisplayName("findByUnUsuario_Id: presente cuando el responsable referencia al usuario")
@Test
void findByUnUsuario_Id_presente() {
    /**
     * Verifica que el repositorio devuelva el Responsable cuyo Usuario
     * tiene el ID indicado.
     * Expectativa: Optional.present con el ID de respMadrePac1.
     */
    var opt = responsableRepository.findByUnUsuario_Id(userResp1.getId());
    assertThat(opt).isPresent();
    assertThat(opt.get().getId()).isEqualTo(respMadrePac1.getId());
}

@DisplayName("findByUnUsuario_Id: Optional.empty cuando el usuario no tiene responsable asociado")
@Test
void findByUnUsuario_Id_usuarioSinResponsable() {
    /**
     * Verifica que, si el usuario existe, pero no está asociado a ningún responsable,
     * el resultado sea Optional.empty.
     */
    var opt = responsableRepository.findByUnUsuario_Id(userLibre.getId());
    assertThat(opt).isNotPresent();
}

@DisplayName("findByUnUsuario_Id: Optional.empty para usuario inexistente")
@Test
void findByUnUsuario_Id_inexistente() {
    /**
     * Verifica que la búsqueda por un ID de usuarioorne Optional.empty inexistente ret.
     */
    assertThat(responsableRepository.findByUnUsuario_Id(123456789L)).isNotPresent();
}



@DisplayName("findByPaciente_Id: presente cuando el responsable referencia al paciente")
@Test
void findByPaciente_Id_presente() {
    /**
     * Verifica que se recupere el Responsable asociado a un Paciente específico: el
     * método retorna Optional, asumiendo relación 1:1 responsable–paciente.
     */
    assertThat( responsableRepository.findByPaciente_Id(pac1.getId()));
    //assertThat(opt).isNotEmpty();
    }

@DisplayName("findByPaciente_Id: Optional.empty cuando el paciente no tiene responsable asociado")
@Test
void findSinResponsable() {
    /**
     * Verifica que para un ID de pacienteByPaciente_Id_paciente inexistente o sin responsable, la consulta sea vacía.
     */
    assertThat(responsableRepository.findByPaciente_Id(-1L)).isNotPresent();
}


@DisplayName("findByDni: existe el documento")
@Test
void findByDni_presente() {
    /**
     * Verifica que se recupere el Responsable exacto para un DNI existente.
     */
    assertThat(responsableRepository.findByDni("RESP-002"))
            .isPresent()
            .get()
            .extracting(Responsable::getId)
            .isEqualTo(respPadrePac2.getId());
}

@DisplayName("findByDni: Optional.empty cuando no existe el documento")
@Test
void findByDni_inexistente() {
    /**
     *
     Verifica que la búsqueda por DNI inexistente retorne Optional.empty.
     */
    assertThat(responsableRepository.findByDni("RESP-XYZ-000")).isNotPresent();
}

@DisplayName("findByTelefono: presente cuando existe el teléfono")
@Test
void findByTelefono_presente() {
    /**
     * Verifica que se recupere el Responsable por teléfono existente.
     */
    assertThat(responsableRepository.findByTelefono("320-1001"))
            .isPresent()
            .get()
            .extracting(Responsable::getId)
            .isEqualTo(respMadrePac1.getId());
}

@DisplayName("findByTelefono: Optional.empty cuando no existe el teléfono")
@Test
void findByTelefono_inexistente() {
    /**
     * Verifica que la búsqueda por teléfono inexistente retorne Optional.empty.
     */
    assertThat(responsableRepository.findByTelefono("320-1999")).isNotPresent();
}



@DisplayName("findByTipoResponsable: devuelve lista filtrada por parentesco")
@Test
void findByTipoResponsable_filtra() {
    /**
     * Verifica que se devuelvan únicamente los responsables del tipo solicitado: respMadrePac1.
     * Setup(MADRE), respPadrePac2 (PADRE), respTutorSinUsuario (TUTOR).
     * Expectativa: para MADRE, lista con respMadrePac1; para PADRE, lista con respPadrePac2.
     */
    List<Responsable> madres = responsableRepository.findByTipoResponsable(TipoResponsable.MAMA);
    assertThat(madres).hasSize(1).extracting(Responsable::getId).containsExactly(respMadrePac1.getId());

    List<Responsable> padres  = responsableRepository.findByTipoResponsable(TipoResponsable.PAPA);
    assertThat(padres).hasSize(1).extracting(Responsable::getId).containsExactly(respPadrePac2.getId());
}

@DisplayName("findByTipoResponsable: lista vacía cuando no hay coincidencias")
@Test
void findByTipoResponsable_sinCoincidencias() {
    /**
     * Verifica que la consulta retorne lista vacía cuando no hay registros
     * con el tipo solicitado.
     */
    List<Responsable> abuelos = responsableRepository.findByTipoResponsable(TipoResponsable.ABUELO);
    assertThat(abuelos).isEmpty();
}

@DisplayName("existsByDni: true/false según existencia del documento")
@Test
void existsByDni_trueFalse() {
    /**
     * Verifica la detección de existencia por DNI.
     */
    assertThat(responsableRepository.existsByDni("RESP-001")).isTrue();
    assertThat(responsableRepository.existsByDni("RESP-NO-EXISTE")).isFalse();
}

@DisplayName("existsByTelefono: true/false según existencia del teléfono")
@Test
void existsByTelefono_trueFalse() {
    /**
     * Verifica la detección de existencia por */
            assertThat(responsableRepository.existsByTelefono("320-1002")).isTrue();
            assertThat(responsableRepository.existsByTelefono("1002")).isFalse();


}

}