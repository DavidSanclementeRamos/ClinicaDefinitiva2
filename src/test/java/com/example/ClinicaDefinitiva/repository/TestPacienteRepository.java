package com.example.ClinicaDefinitiva.repository;
import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.builder.OdontologoBuilder;
import com.example.ClinicaDefinitiva.builder.TurnoBuilder;
import com.example.ClinicaDefinitiva.builder.UsuarioBuilder;
import com.example.ClinicaDefinitiva.persistence.entity.*;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("PacienteRepository")
@ActiveProfiles("test") // Usa application-test.properties
public class TestPacienteRepository {

    @Autowired PacienteRepository pacienteRepository;
    @Autowired UsuarioRepository usuarioRepository;
    @Autowired OdontologoRepository odontologoRepository;
    @Autowired TurnoRepository turnoRepository;
    @Autowired
    EntityManager em;

    // Datos compartidos
    private Usuario userAna, userAndres, userLibre;
    private Paciente ana, andres, beatriz;
    private Odontologo odontologo;
    private Disponibilidad disponibilidad;

    // Fechas de prueba
    private final LocalDate FECHA_EN_RANGO_1 = LocalDate.of(2025, 8, 20);
    private final LocalDate FECHA_EN_RANGO_2 = LocalDate.of(2025, 8, 17);
    private final LocalDate FECHA_FUERA      = LocalDate.of(2025, 9, 1);

    @BeforeEach
    void setUp() {
        // Limpieza para aislamiento
        turnoRepository.deleteAll();
        pacienteRepository.deleteAll();
        odontologoRepository.deleteAll();
        usuarioRepository.deleteAll();
      //  em.flush();

        // Usuarios base
        userAna   = usuarioRepository.save(new UsuarioBuilder().withNombreUsuario("ana.user").withFechaDeCreacion(LocalDate.of(2025,1,1)).builder());
        userAndres= usuarioRepository.save(new UsuarioBuilder().withNombreUsuario("andres.user").withFechaDeCreacion(LocalDate.of(2025,1,2)).builder());
        userLibre = usuarioRepository.save(new UsuarioBuilder().withNombreUsuario("sin.paciente").withFechaDeCreacion(LocalDate.of(2025,1,3)).builder());
        usuarioRepository.saveAll(List.of(userAna, userLibre,userAndres));

        // Odontólogo y disponibilidad mínimos para poder crear turnos
      /*  odontologo = odontologoRepository.save(new OdontologoBuilder()
                .withUsuario( new UsuarioBuilder().withNombreUsuario("odo.user").withFechaDeCreacion(LocalDate.of(2025,1,4)).builder())
                .withFechaNacimiento(LocalDate.of(1980,1,1))
                .withEspecialidad(Especialidades.CIRUGIA_ORAL)
                .withTelefono("320-0000") // evita colisiones de unicidad
                .builder());
      //  usuarioRepository.save()
        odontologoRepository.save(odontologo);*/
        Usuario odoUser = usuarioRepository.save(
                new UsuarioBuilder()
                        .withNombreUsuario("odo.user")
                        .withFechaDeCreacion(LocalDate.of(2025,1,4))
                        .builder()
        );

        odontologo = odontologoRepository.save(
                new OdontologoBuilder()
                        .withUsuario(odoUser)
                        .withFechaNacimiento(LocalDate.of(1980,1,1))
                        .withEspecialidad(Especialidades.CIRUGIA_ORAL)
                        .withTelefono("320-0000")
                        .builder()
        );

        disponibilidad = new Disponibilidad();
        disponibilidad.setUnOdontologo(odontologo);
        disponibilidad.setDiaSemana(java.time.DayOfWeek.WEDNESDAY);
        disponibilidad.setHoraInicio(LocalTime.of(6, 30));
        disponibilidad.setHoraFin(LocalTime.of(12, 30));
        // si Disponibilidad requiere persistencia explícita, usa un repositorio/EntityManager adecuado
        em.persist(disponibilidad);

        // Pacientes base (valores únicos para evitar constraints)
        ana = new Paciente();
        ana.setNombre("Ana María");
        ana.setDni("DNI-ANA-001");
        ana.setTelefono("300-0001");
        ana.setFecha_nacimiento(LocalDate.EPOCH);
        ana.setUnUsuario(userAna);

        andres = new Paciente();
        andres.setNombre("andres"); // minúsculas para probar ignore case
        andres.setDni("DNI-AND-002");
        andres.setTelefono("300-0002");
        andres.setFecha_nacimiento(LocalDate.EPOCH);
        andres.setUnUsuario(userAndres);

        beatriz = new Paciente();
        beatriz.setNombre("Beatriz");
        beatriz.setDni("DNI-BEA-003");
        beatriz.setTelefono("300-0003");
        beatriz.setFecha_nacimiento(LocalDate.EPOCH);
        // sin usuario asociado

        pacienteRepository.saveAll(List.of(ana, andres, beatriz));

        // Turnos: asocia paciente + odontólogo + disponibilidad
        Turno tAna1 = new TurnoBuilder()
                .withPaciente(ana)
                .withOdontologo(odontologo)
                .withHorario(disponibilidad)
                .withFechaTurno(FECHA_EN_RANGO_1) // 2025-08-20
                .builder();

        Turno tAna2 = new TurnoBuilder()
                .withPaciente(ana)
                .withOdontologo(odontologo)
                .withHorario(disponibilidad)
                .withFechaTurno(FECHA_EN_RANGO_2) // 2025-08-17
                .builder();

        Turno tAndresOut = new TurnoBuilder()
                .withPaciente(andres)
                .withOdontologo(odontologo)
                .withHorario(disponibilidad)
                .withFechaTurno(FECHA_FUERA) // 2025-09-01 (fuera)
                .builder();

        turnoRepository.saveAll(List.of(tAna1, tAna2, tAndresOut));

        em.flush();
        em.clear();
    }



    @DisplayName("save: persiste un paciente con datos mínimos y usuario asociado")
    @Test
    void save_persistePaciente() {
        /**
         * Verifica que se pueda persistir un Paciente con:
         * - Nombre, DNI y teléfono válidos.
         * - Usuario asociado sincronizado.
         * Resultado esperado: ID generado y atributos persistidos.
         */
        Usuario user = usuarioRepository.save(new UsuarioBuilder().withNombreUsuario("nuevo.user").withFechaDeCreacion(LocalDate.now()).builder());
        Paciente p = new Paciente();
        p.setNombre("Nuevo Paciente");
        p.setFecha_nacimiento(LocalDate.EPOCH);
        p.setDni("DNI-NEW-999");
        p.setTelefono("300-0999");
        p.setUnUsuario(user);
        em.flush();
        Paciente guardado = pacienteRepository.save(p);

        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getUnUsuario().getId()).isEqualTo(user.getId());
        assertThat(guardado.getDni()).isEqualTo("DNI-NEW-999");
    }

    @DisplayName("update: muta campos escalares y persiste cambios al recargar")
    @Test
    void update_actualizaCampos() {
        /**
         * Verifica el dirty checking al modificar un Paciente existente:
         * - Cambia nombre y teléfono.
         * - Recarga y válida nuevos valores.
         */
        Paciente managed = pacienteRepository.findById(ana.getId()).orElseThrow();
        managed.setNombre("Ana M.");
        managed.setTelefono("300-0101");

        em.flush();
        em.clear();

        Paciente reloaded = pacienteRepository.findById(ana.getId()).orElseThrow();
        assertThat(reloaded.getNombre()).isEqualTo("Ana M.");
        assertThat(reloaded.getTelefono()).isEqualTo("300-0101");
    }

    @DisplayName("findById: presente cuando existe el paciente")
    @Test
    void findById_existente() {
        /**
         * Verifica que findById retorne presente para un ID existente.
         */
        assertThat(pacienteRepository.findById(andres.getId()))
                .isPresent()
                .get()
                .extracting(Paciente::getId)
                .isEqualTo(andres.getId());
    }

    @DisplayName("findById: Optional.empty cuando no existe")
    @Test
    void findById_inexistente() {
        /**
         * Verifica que findById retorne Optional.empty para un ID inexistente.
         */
        assertThat(pacienteRepository.findById(-9999L)).isNotPresent();
    }

    @DisplayName("findAll: devuelve todos los pacientes persistidos")
    @Test
    void findAll_devuelveTodos() {
        /**
         * Verifica que findAll retorna el conjunto completo de registros.
         * En el setup hay 3 pacientes.
         */
        List<Paciente> all = pacienteRepository.findAll();
        assertThat(all).hasSize(3)
                .extracting(Paciente::getId)
                .containsExactlyInAnyOrder(ana.getId(), andres.getId(), beatriz.getId());
    }

    @DisplayName("delete: elimina el paciente y no aparece en consultas posteriores")
    @Test
    void delete_eliminaRegistro() {
        /**
         * Verifica que un registro eliminado no se pueda volver a recuperar.
         * Se elimina un paciente sin dependencias que bloqueen la FK (beatriz).
         */
        pacienteRepository.delete(beatriz);
        em.flush();
        em.clear();

        assertThat(pacienteRepository.findById(beatriz.getId())).isNotPresent();
    }


    @DisplayName("findByNombreContainingIgnoreCase: match parcial sin sensibilidad a mayúsculas/minúsculas")
    @Test
    void findByNombreContainingIgnoreCase_parcial() {
        /**
         * Verifica que la búsqueda por nombre sea parcial e ignore el case:
         * - "an" debe encontrar "Ana María" y "andres".
         * - "BEA" debe encontrar "Beatriz".
         */
        List<Paciente> anMatches = pacienteRepository.findByNombreContainingIgnoreCase("an");
        assertThat(anMatches).extracting(Paciente::getNombre)
                .containsExactlyInAnyOrder("Ana María", "andres");

        List<Paciente> beaMatches = pacienteRepository.findByNombreContainingIgnoreCase("BEA");
        assertThat(beaMatches).extracting(Paciente::getNombre)
                .containsExactly("Beatriz");
    }

    @DisplayName("findByNombreContainingIgnoreCase: lista vacía cuando no hay coincidencias")
    @Test
    void findByNombreContainingIgnoreCase_sinCoincidencias() {
        /**
         * Verifica que no devuelva resultados cuando el patrón no coincide con ningún nombre.
         */
        assertThat(pacienteRepository.findByNombreContainingIgnoreCase("xyz")).isEmpty();
    }



    @DisplayName("findByDni: presente cuando existe el documento")
    @Test
    void findByDni_presente() {
        /**
         * Verifica que se recupere el Paciente exacto para un DNI existente.
         */
        assertThat(pacienteRepository.findByDni("DNI-AND-002"))
                .isPresent()
                .get()
                .extracting(Paciente::getId)
                .isEqualTo(andres.getId());
    }

    @DisplayName("findByDni: Optional.empty cuando no existe el documento")
    @Test
    void findByDni_inexistente() {
        /**
         * Verifica que la búsqueda por DNI inexistente retorne Optional.empty.
         */
        assertThat(pacienteRepository.findByDni("DNI-NO-EXISTE")).isNotPresent();
    }



    @DisplayName("findByUnUsuario_Id: presente cuando el paciente referencia al usuario")
    @Test
    void findByUnUsuario_Id_presente() {
        /**
         * Verifica que se recupere el Paciente asociado a un Usuario específico.
         */
        assertThat(pacienteRepository.findByUnUsuario_Id(userAna.getId()))
                .isPresent()
                .get()
                .extracting(Paciente::getId)
                .isEqualTo(ana.getId());
    }

    @DisplayName("findByUnUsuario_Id: Optional.empty cuando el usuario no tiene paciente asociado")
    @Test
    void findByUnUsuario_Id_usuarioSinPaciente() {
        /**
         * Verifica que un Usuario existente pero sin paciente asociado devuelve Optional.empty.
         */
        assertThat(pacienteRepository.findByUnUsuario_Id(userLibre.getId())).isNotPresent();
    }

    @DisplayName("findByUnUsuario_Id: Optional.empty para ID de usuario inexistente")
    @Test
    void findByUnUsuario_Id_inexistente() {
        /**
         * Verifica que buscar por un ID de usuario inexistente devuelva Optional.empty.
         */
        assertThat(pacienteRepository.findByUnUsuario_Id(123456789L)).isNotPresent();
    }


    @DisplayName("findByUnTurno_FechaTurno: incluye pacientes con turno en la fecha exacta")
    @Test
    void findByUnTurno_FechaTurno_incluyeFecha() {
        /**
         * Verifica que se incluyan los Pacientes que tengan al menos un Turno
         * exactamente en la fecha consultada.
         * - Setup: Ana con turnos 2025-08-17 y 2025-08-20; Andrés con 2025-09-01.
         * - Búsqueda: 2025-08-20 → debe incluir a Ana y no a Andrés.
         */
        List<Paciente> lista = pacienteRepository.findByUnTurno_FechaTurno(FECHA_EN_RANGO_1);

        assertThat(lista).extracting(Paciente::getId)
                .contains(ana.getId())
                .doesNotContain(andres.getId());
    }

    @DisplayName("findByUnTurno_FechaTurno: lista vacía cuando ningún turno coincide con la fecha")
    @Test
    void findByUnTurno_FechaTurno_sinCoincidencias() {
        /**
         * Verifica que se retorne lista vacía si ningún Paciente tiene turnos en esa fecha.
         */
        List<Paciente> lista = pacienteRepository.findByUnTurno_FechaTurno(LocalDate.of(2025, 7, 1));
        assertThat(lista).isEmpty();
    }



    @DisplayName("existsByTelefono: true cuando hay paciente con ese teléfono")
    @Test
    void existsByTelefono_true() {
        /**
         * Verifica que el repositorio detecta existencia por teléfono.
         */
        assertThat(pacienteRepository.existsByTelefono("300-0001")).isTrue();
    }

    @DisplayName("existsByTelefono: false cuando no hay paciente con ese teléfono")
    @Test
    void existsByTelefono_false() {
        /**
         * Verifica que retorna false cuando no existe el teléfono.
         */
        assertThat(pacienteRepository.existsByTelefono("300-0999")).isFalse();
    }

    @DisplayName("existsByDni: true cuando hay paciente con ese DNI")
    @Test
    void existsByDni_true() {
        /**
         * Verifica que el repositorio detecta existencia por DNI.
         */
        assertThat(pacienteRepository.existsByDni("DNI-ANA-001")).isTrue();
    }

    @DisplayName("existsByDni: false cuando no hay paciente con ese DNI")
    @Test
    void existsByDni_false() {
        /**
         * Verifica que retorna false cuando el DNI no existe.
         */
        assertThat(pacienteRepository.existsByDni("DNI-XYZ-000")).isFalse();
    }
}



