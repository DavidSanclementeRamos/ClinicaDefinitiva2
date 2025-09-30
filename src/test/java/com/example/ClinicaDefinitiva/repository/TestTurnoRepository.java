package com.example.ClinicaDefinitiva.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.example.ClinicaDefinitiva.builder.HorarioBuilder;
import com.example.ClinicaDefinitiva.builder.OdontologoBuilder;
import com.example.ClinicaDefinitiva.builder.TurnoBuilder;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
@ActiveProfiles("test") // Usa application-test.properties
public class TestTurnoRepository {

    @Autowired
    private TurnoRepository turnoRepository;

    @PersistenceContext
    private EntityManager em;

    // Builders base para datos consistentes entre tests
    private Odontologo odontologoBase;
    private Paciente pacienteBase;
    private Turno turnoBase;

    @BeforeEach
    void setUp() {
        odontologoBase = new OdontologoBuilder()
                .withNombre("Dra. Rivera")
                .withFechaNacimiento(LocalDate.EPOCH)
                .withDni("M-1001").builder();

        pacienteBase.setNombre("David");
        pacienteBase.setDni("CC-123");
        pacienteBase.setFecha_nacimiento(LocalDate.EPOCH);

        // Persistimos referencias para evitar transients si no hay cascadas
         em.persist(odontologoBase);
         em.persist(pacienteBase);

        turnoBase = new TurnoBuilder()
                .withOdontologo(odontologoBase)
                .withPaciente(pacienteBase)
                .withFechaTurno(LocalDate.of(2025, 8, 20))
                .withHorario(new HorarioBuilder().setHoraInicio( LocalTime  .of(10, 0))
                .conEstado(Estado.CONFIRMADO);
    }

    // -------------------- CRUD --------------------

    @Nested
    @DisplayName("CRUD")
    class Crud {

        @Test
        @DisplayName("Create: persiste Turno con relaciones válidas")
        void create_persisteTurno() {
            var turno = turnoBase.build();

            var guardado = turnoRepository.saveAndFlush(turno);
            clear();

            var reloaded = turnoRepository.findById(guardado.getId());
            assertThat(reloaded).isPresent();
            assertThat(reloaded.get().getOdontologo().getId()).isEqualTo(guardado.getOdontologo().getId());
            assertThat(reloaded.get().getPaciente().getId()).isEqualTo(guardado.getPaciente().getId());
            assertThat(reloaded.get().getEstado()).isEqualTo(Estado.CONFIRMADO);
        }

        @Test
        @DisplayName("Read: findById ausente retorna Optional.empty()")
        void read_findByIdAusente() {
            Optional<Turno> inexistente = turnoRepository.findById(-1L);
            assertThat(inexistente).isEmpty();
        }

        @Test
        @DisplayName("Update: cambia estado y hora, persiste cambios")
        void update_cambiaEstadoYHora() {
            var guardado = turnoRepository.saveAndFlush(turnoBase.build());
            clear();

            var managed = turnoRepository.findById(guardado.getId()).orElseThrow();
            managed.setEstado(Estado.REPROGRAMADO);
            managed.setHoraTurno(LocalTime.of(11, 30));

            turnoRepository.saveAndFlush(managed);
            clear();

            var reloaded = turnoRepository.findById(guardado.getId()).orElseThrow();
            assertThat(reloaded.getEstado()).isEqualTo(Estado.REPROGRAMADO);
            assertThat(reloaded.getHoraTurno()).isEqualTo(LocalTime.of(11, 30));
        }

        @Test
        @DisplayName("Delete: elimina por id; id inexistente no falla")
        void delete_eliminaYNoFallaConInexistente() {
            var guardado = turnoRepository.saveAndFlush(turnoBase.build());
            clear();

            turnoRepository.deleteById(guardado.getId());
            turnoRepository.flush();

            assertThat(turnoRepository.findById(guardado.getId())).isEmpty();

            // rama: eliminar inexistente no lanza error en Spring Data JPA
            turnoRepository.deleteById(-123L);
            turnoRepository.flush();
        }
    }

    // -------------------- Consultas derivadas --------------------

    @Nested
    @DisplayName("Consultas derivadas")
    class ConsultasDerivadas {

        @Test
        @DisplayName("Por fecha: encuentra por fecha exacta y retorna vacío si no hay")
        void porFecha_exactaYVacio() {
            var f = LocalDate.of(2025, 8, 21);
            persist(turnoBase.clone().conFecha(f).build());

            var ok = turnoRepository.findByFechaTurno(f);
            var vacio = turnoRepository.findByFechaTurno(LocalDate.of(2025, 8, 22));

            assertThat(ok).hasSize(1);
            assertThat(vacio).isEmpty();
        }

        @Test
        @DisplayName("Por rango: Between incluye extremos; fuera del rango es vacío")
        void porRango_betweenIncluyeExtremos() {
            var f1 = LocalDate.of(2025, 8, 20);
            var f2 = LocalDate.of(2025, 8, 21);
            var f3 = LocalDate.of(2025, 8, 22);

            persist(turnoBase.clone().conFecha(f1).build());
            persist(turnoBase.clone().conFecha(f2).build());
            persist(turnoBase.clone().conFecha(f3).build());

            // extremos incluidos
            var enRango = turnoRepository.findByFechaTurnoBetween(f1, f2);
            assertThat(enRango).extracting("fechaTurno").containsExactlyInAnyOrder(f1, f2);

            // fuera del rango
            var fuera = turnoRepository.findByFechaTurnoBetween(LocalDate.of(2025, 8, 18), LocalDate.of(2025, 8, 19));
            assertThat(fuera).isEmpty();
        }

        @Test
        @DisplayName("Por odontólogo: devuelve múltiples y soporta vacío")
        void porOdontologo_multipleYVacio() {
            var otroOdont = persist(new OdontologoBuilder().conNombre("Dr. López").conMatricula("M-2002").build());
            var paciente = persist(new PacienteBuilder().conNombre("Ana").conDocumento("CC-456").build());

            var t1 = persist(turnoBase.clone().conHora(LocalTime.of(9, 0)).build());
            var t2 = persist(turnoBase.clone().conHora(LocalTime.of(10, 0)).build());
            var tOtro = persist(turnoBase.clone().conOdontologo(otroOdont).conPaciente(paciente).conHora(LocalTime.of(9, 30)).build());

            var resultados = turnoRepository.findByOdontologo_Id(t1.getOdontologo().getId());
            assertThat(resultados).contains(t1, t2).doesNotContain(tOtro);

            var vacio = turnoRepository.findByOdontologo_Id(-1L);
            assertThat(vacio).isEmpty();
        }

        @Test
        @DisplayName("Por paciente: devuelve múltiples y soporta vacío")
        void porPaciente_multipleYVacio() {
            var otroPaciente = persist(new PacienteBuilder().conNombre("Luis").conDocumento("CC-789").build());

            var t1 = persist(turnoBase.clone().conHora(LocalTime.of(9, 0)).build());
            var t2 = persist(turnoBase.clone().conHora(LocalTime.of(10, 0)).build());
            var tOtro = persist(turnoBase.clone().conPaciente(otroPaciente).conHora(LocalTime.of(11, 0)).build());

            var resultados = turnoRepository.findByPaciente_Id(t1.getPaciente().getId());
            assertThat(resultados).contains(t1, t2).doesNotContain(tOtro);

            var vacio = turnoRepository.findByPaciente_Id(-1L);
            assertThat(vacio).isEmpty();
        }

        @Test
        @DisplayName("Por estado: filtra correctamente múltiples estados")
        void porEstado_filtra() {
            var conf = persist(turnoBase.clone().conEstado(Estado.CONFIRMADO).build());
            var canc = persist(turnoBase.clone().conHora(LocalTime.of(12, 0)).conEstado(Estado.CANCELADO).build());
            var reprog = persist(turnoBase.clone().conHora(LocalTime.of(13, 0)).conEstado(Estado.REPROGRAMADO).build());

            var confirmados = turnoRepository.findByEstado(Estado.CONFIRMADO);
            assertThat(confirmados).contains(conf).doesNotContain(canc, reprog);
        }

        @Test
        @DisplayName("Solapamiento: true si coincide odontólogo+fecha+hora; false en otras combinaciones")
        void solapamiento_trueYFalse() {
            var odont = persist(new OdontologoBuilder().conNombre("Dra. Sol").conMatricula("M-3003").build());
            var fecha = LocalDate.of(2025, 8, 25);
            var hora = LocalTime.of(10, 0);

            persist(turnoBase.clone().conOdontologo(odont).conFecha(fecha).conHora(hora).build());

            // true: misma terna
            boolean existe = turnoRepository.existsByOdontologo_IdAndFechaTurnoAndHoraTurno(odont.getId(), fecha, hora);
            assertThat(existe).isTrue();

            // false: cambia hora
            boolean cambiaHora = turnoRepository.existsByOdontologo_IdAndFechaTurnoAndHoraTurno(odont.getId(), fecha, LocalTime.of(11, 0));
            assertThat(cambiaHora).isFalse();

            // false: cambia fecha
            boolean cambiaFecha = turnoRepository.existsByOdontologo_IdAndFechaTurnoAndHoraTurno(odont.getId(), fecha.plusDays(1), hora);
            assertThat(cambiaFecha).isFalse();

            // false: cambia odontólogo
            var otroOdont = persist(new OdontologoBuilder().withNombre("Dr. Luna").withDni("M-4004").build());
            boolean cambiaOdont = turnoRepository.existsByOdontologo_IdAndFechaTurnoAndHoraTurno(otroOdont.getId(), fecha, hora);
            assertThat(cambiaOdont).isFalse();
        }
    }


}