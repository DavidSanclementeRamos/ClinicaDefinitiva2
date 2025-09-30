package com.example.ClinicaDefinitiva.repository;
import com.example.ClinicaDefinitiva.Enum.Sector;
import com.example.ClinicaDefinitiva.persistence.entity.Secretario;

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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("SecretarioRepository")
@ActiveProfiles("test") // Usa application-test.properties
public class TestSecretarioRepository {

    @Autowired SecretarioRepository secretarioRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired
    EntityManager em;

    // Usuarios (persistidos explícitamente para evitar transients)
    private Usuario userSec1, userSec2, userLibre;

    // Secretarios semilla
    private Secretario secRecepcion, secCaja, secSinUsuario;

    @BeforeEach
    void setUp() {
        // Aislamiento por prueba
        secretarioRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Usuarios
        userSec1 = new Usuario();
        userSec1.setNombreUsuario("sec1.user");

        userSec1.setFechaDeCreacion(LocalDate.of(2025, 1, 10));
        userSec1 = usuarioRepository.save(userSec1);

        userSec2 = new Usuario();
        userSec2.setNombreUsuario("sec2.user");
        userSec2.setFechaDeCreacion(LocalDate.of(2025, 1, 11));
        userSec2 = usuarioRepository.save(userSec2);

        userLibre = new Usuario();
        userLibre.setNombreUsuario("sin.secretario");
        userLibre.setFechaDeCreacion(LocalDate.of(2025, 1, 12));
        usuarioRepository.save(userLibre);

        // Secretarios
        secRecepcion = new Secretario();
        secRecepcion.setNombre("Ana María");
        secRecepcion.setDni("SEC-001");
        secRecepcion.setTelefono("300-5001");
        secRecepcion.setFecha_nacimiento(LocalDate.EPOCH);
        secRecepcion.setSector(Sector.RECEPCION);
        secRecepcion.setUnUsuario(userSec1);
        secRecepcion = secretarioRepository.save(secRecepcion);

        secCaja = new Secretario();
        secCaja.setNombre("Juan Carlos");
        secCaja.setDni("SEC-002");
        secCaja.setTelefono("300-5002");
        secCaja.setFecha_nacimiento(LocalDate.EPOCH);
        secCaja.setSector(Sector.FACTURACION);
        secCaja.setUnUsuario(userSec2);
        secCaja = secretarioRepository.save(secCaja);

        // Secretario sin usuario (rama sin asociación)
        secSinUsuario = new Secretario();
        secSinUsuario.setNombre("Anabella");
        secSinUsuario.setDni("SEC-003");
        secSinUsuario.setTelefono("300-5003");
        secSinUsuario.setFecha_nacimiento(LocalDate.EPOCH);
        secSinUsuario.setSector(Sector.RECEPCION);
        secSinUsuario = secretarioRepository.save(secSinUsuario);

        em.flush();
        em.clear();
    }


    @DisplayName("save: persiste un secretario con datos mínimos y usuario asociado")
    @Test
    void  SaveSecretario() {
        /**
         * Intención:
         * - Persistir un Secretario con campos mínimos y relación void save_persiste con Usuario ya persistido.
         * Expectativa: ID generando y asociaciones íntegras.
         */
        Usuario u = new Usuario();
        u.setNombreUsuario("nuevo.sec");
        u.setFechaDeCreacion(LocalDate.now());
        u = usuarioRepository.save(u);

        Secretario s = new Secretario();
        s.setNombre("Nuevo Secretario");
        s.setDni("SEC-999");
        s.setTelefono("300-5999");
        s.setFecha_nacimiento(LocalDate.EPOCH);
        s.setSector(Sector.FACTURACION);
        s.setUnUsuario(u);

        Secretario guardado = secretarioRepository.save(s);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUnUsuario().getId()).isEqualTo(u.getId());
        assertThat(guardado.getDni()).isEqualTo("SEC-999");
        assertThat(guardado.getSector()).isEqualTo(Sector.FACTURACION);
    }

    @DisplayName("update: muta campos escalares y confirma al recargar")
    @Test
    void update_actualizaCampos() {
        /**
         * Intención:
         * - Verificar dirty checking al modificar teléfono y sector.
         */
        Secretario managed = secretarioRepository.findById(secRecepcion.getId()).orElseThrow();
        managed.setTelefono("300-5888");
        managed.setSector(Sector.FACTURACION);

        em.flush();
        em.clear();

        Secretario reloaded = secretarioRepository.findById(secRecepcion.getId()).orElseThrow();
        assertThat(reloaded.getTelefono()).isEqualTo("300-5888");
        assertThat(reloaded.getSector()).isEqualTo(Sector.FACTURACION);
    }

    @DisplayName("findById: presente cuando existe")
    @Test
    void findById_existente() {
        assertThat(secretarioRepository.findById(secCaja.getId()))
                .isPresent()
                .get()
                .extracting(Secretario::getId)
                .isEqualTo(secCaja.getId());
    }

    @DisplayName("findById: Optional.empty cuando no existe")
    @Test
    void findById_inexistente() {
        assertThat(secretarioRepository.findById(-12345L)).isNotPresent();
    }

    @DisplayName("findAll: devuelve todos los secretarios persistidos")
    @Test
    void findAll_devuelveTodos() {
        List<Secretario> all = secretarioRepository.findAll();
        assertThat(all)
                .hasSize(3)
                .extracting(Secretario::getId)
                .containsExactlyInAnyOrder(secRecepcion.getId(), secCaja.getId(), secSinUsuario.getId());
    }

    @DisplayName("delete: elimina el secretario y no aparece en consultas posteriores")
    @Test
    void delete_eliminaRegistro() {
        secretarioRepository.delete(secSinUsuario);
        em.flush();
        em.clear();

        assertThat(secretarioRepository.findById(secSinUsuario.getId())).isNotPresent();
    }


    @DisplayName("findBySector por sector")
    @Test
    void findBySector_filtra() {
        /**
         * Intención:
         * - Validar que se filtra por sector exacto (enum).
         * Setup:
         *   - RECEPCION: secRecepcion, secSinUsuario
         *   - CAJA: secCaja
         */
        List<Secretario> recepcion = secretarioRepository.findBySector(Sector.RECEPCION);
        assertThat(recepcion)
                .extracting(Secretario::getId)
                .containsExactlyInAnyOrder(secRecepcion.getId(), secSinUsuario.getId());

        List<Secretario> caja = secretarioRepository.findBySector(Sector.FACTURACION);
        assertThat(caja)
                .hasSize(1)
                .first()
                .extracting(Secretario::getId)
                .isEqualTo(secCaja.getId());
    }

    @DisplayName("findBySector: lista vacía cuando no hay coincidencias")
    @Test
    void findBySector_sinCoincidencias() {
        /**
         * Nota:
         * - Si tu enum no tiene ADMINISTRACION, ajusta a un valor válido que
         *   no esté presente en el setUp.
         */
        List<Secretario> admin = secretarioRepository.findBySector(Sector.ADMINISTRACION);
        assertThat(admin).isNullOrEmpty();
    }


    @DisplayName("findByNombreContainingIgnoreCase: devuelve coincidencia sin importar mayúsculas")
    @Test
    void findByNombreContainingIgnoreCase() {

        /** - Validar búsqueda parcial e insensitive.
         * Setup:
         *   - "Ana María"
         *   - "Juan Carlos"
         *   - "Anabella"
         * Búsqueda "ana" debe devolver Ana María y Anabella.
         */
        List<Secretario> resultado = secretarioRepository.findByNombreContainingIgnoreCase("ana");
        assertThat(resultado).extracting(Secretario::getNombre)
                .containsExactlyInAnyOrder("Ana María", "Anabella");
    }

    @DisplayName("findByNombreContainingIgnoreCase: lista vacía cuando no hay match")
    @Test
    void findByNombreContainingIgnoreCase_sinCoincidencias() {
        List<Secretario> resultado = secretarioRepository.findByNombreContainingIgnoreCase("zzz");
        assertThat(resultado).isEmpty();
    }


    @DisplayName("findByUnUsuario_Id: presente cuando el secretario referencia al usuario")
    @Test
    void findByUnUsuario_Id_presente() {
        Optional<Secretario>  listaSecretario= secretarioRepository.findByUnUsuario_Id(userSec1.getId());
        assertThat(listaSecretario).isPresent();
        assertThat(listaSecretario.get().getId()).isEqualTo(secRecepcion.getId());
    }

    @DisplayName("findByUnUsuario_Id: Optional.empty cuando el usuario existe pero no está asociado")
    @Test
    void findByUnUsuario_Id_usuarioSinSecretario() {
        assertThat(secretarioRepository.findByUnUsuario_Id(userLibre.getId())).isNotPresent();
       // assertThat(opt).isNotPresent();
    }

    @DisplayName("findByUnUsuario_Id: Optional.empty para usuario inexistente")
    @Test
    void findByUnUsuario_Id_inexistente() {
        assertThat( secretarioRepository.findByUnUsuario_Id(999_999L)).isNotPresent();
    }



    @DisplayName("existsByDni existencia del documento: true/false según")
    @Test
    void existsByDni () {
        assertThat(   secretarioRepository.existsByDni("SEC-001")).isTrue();
        assertThat(secretarioRepository.existsByDni("SEC-NO-EXISTE")).isFalse();
    }

    @DisplayName("existsByTelefono: true/false según existencia del teléfono")
    @Test
    void existsByTelefono_trueFalse() {
        assertThat(secretarioRepository.existsByTelefono("300-5002")).isTrue();
        assertThat(secretarioRepository.existsByTelefono("300-5999")).isFalse();
    }

}

