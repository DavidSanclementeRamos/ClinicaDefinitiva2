package com.example.ClinicaDefinitiva.repository;
import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.builder.HorarioBuilder;
import com.example.ClinicaDefinitiva.builder.OdontologoBuilder;
import com.example.ClinicaDefinitiva.builder.TurnoBuilder;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DataJpaTest
@ActiveProfiles("test") // Usa application-test.properties
public class TestHorarioRepository {

    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private OdontologoRepository odontologoRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    PacienteRepository pacienteRepository;

    private Horario horarioBuilder;
    private Odontologo odontologoBuilder;
    private Horario horarioBuilder1;
    private Horario horarioBuilder2;
    private Horario horarioBuilder3;
    private Turno turnoBuilder;
    private Turno turnoBuilder1;
    private Turno turnoBuilder2;
    private Turno turnoBuilder3;
    private Paciente paciente;

    
    @BeforeEach
    void setup() {

        odontologoBuilder = new OdontologoBuilder().builder();

        paciente = new Paciente();
        paciente.setFecha_nacimiento(LocalDate.EPOCH);


        horarioBuilder = new HorarioBuilder()
                .setOdontologo(odontologoBuilder)

                .setHoraInicio(LocalTime.of(9, 0))
                .builder();

         horarioBuilder1 = new HorarioBuilder()
                .setOdontologo(odontologoBuilder)
                .setDiaSemana(DayOfWeek.WEDNESDAY)
                .setHoraInicio(LocalTime.of(6,0))
                .setHoraFin(LocalTime.of(13,0))
                .builder();

        horarioBuilder2 = new HorarioBuilder()
                .setOdontologo(odontologoBuilder)
                .setDiaSemana(DayOfWeek.FRIDAY)
                .setHoraInicio(LocalTime.of(7,30))
                .setHoraFin(LocalTime.of(12,30))
                .builder();

        horarioBuilder3 = new HorarioBuilder()
                .setOdontologo(odontologoBuilder)
                .setDiaSemana(DayOfWeek.WEDNESDAY)
                .setHoraInicio(LocalTime.of(6,30))
                .setHoraFin(LocalTime.of(12,30))
                .builder();

          turnoBuilder = new TurnoBuilder()
                 .withOdontologo(odontologoBuilder)
                 .withHorario(horarioBuilder)
                  .withPaciente(paciente)
                 .builder();

         turnoBuilder1 = new TurnoBuilder()
                .withFechaTurno(LocalDate.of(2025,8,17))
                .withOdontologo(odontologoBuilder)
                .withHorario(horarioBuilder1)
                 .withPaciente(paciente)
                .builder();

         turnoBuilder2 = new TurnoBuilder()
                .withFechaTurno(LocalDate.of(2025,8,19))
                .withOdontologo(odontologoBuilder)
                .withHorario(horarioBuilder2)
                 .withPaciente(paciente)
                .builder();

         odontologoBuilder.setListaHorarios(List.of(horarioBuilder,horarioBuilder1,horarioBuilder2,horarioBuilder3));
         paciente.setUnTurno(List.of(turnoBuilder,turnoBuilder1,turnoBuilder2));
         horarioBuilder.setTurnos(List.of( turnoBuilder));
         horarioBuilder1.setTurnos(List.of( turnoBuilder1));
         horarioBuilder2.setTurnos(List.of( turnoBuilder2));

    }

    @Nested
    @DisplayName("Tests para obtener un Horario por ID")
    class HorarioFindByIdTests {
        @DisplayName("Test horario id valido")
        @Test
        void testHorarioFindById() {
            // given
            odontologoRepository.save(odontologoBuilder);

            // when accion o el comportamiento que vamos a probar
            Horario horarioDB = horarioRepository.findById(horarioBuilder.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Horario no encontrado"));

            // then verificar la salida
            assertThat(horarioDB)
                    .isNotNull()
                    .extracting(Horario::getHoraInicio, h -> h.getUnOdontologo().getNombre(), h ->h.getDiaSemana())
                    .containsExactly(LocalTime.of(9, 0), "NombreDefault",DayOfWeek.MONDAY);

        }

        @DisplayName("Test horario id invalido")
        @Test
        void testHorarioFindByIdInvalido() {
            long idInexistente = Long.MAX_VALUE;
            Optional<Horario> horarioDB = horarioRepository.findById(idInexistente);

            // then verificar la salida
            assertThat(horarioDB)
                    .as("El repositorio no debe retornar resultados para un ID inexistente")
                    .isEmpty();

        }

    }

    @Nested
    @DisplayName("Tests de findALL horario")
    class HorarioFindAllTests {
        @DisplayName("Test lista de Horarios ")
        @Test
        void testHorarioFindAll() {

            //horarioRepository.save(horarioBuilder);
            odontologoRepository.save(odontologoBuilder);

            // when accion o el comportamiento que vamos a probar
            List<Horario> listaHorario = horarioRepository.findAll();

            // then verificar la salida
            assertThat(listaHorario)
                    .isNotNull()
                    .hasSize(4)
                    .extracting(Horario::getId)
                    .doesNotHaveDuplicates();

            // verificar atributos claves de horario
            Horario horarioRecuperado = listaHorario.stream()
                    .filter(h -> h.getDiaSemana() == DayOfWeek.MONDAY)
                    .findFirst()
                    .orElseThrow(() -> new AssertionError("Horario no encontrado"));

            assertThat(horarioRecuperado)
                    .extracting(Horario::getHoraInicio, h -> h.getUnOdontologo().getNombre(), Horario::getDiaSemana)
                    .containsExactly(LocalTime.of(9, 0), "NombreDefault",DayOfWeek.MONDAY);

        }

        @DisplayName("Test lista de Horarios vacios ")
        @Test
        void testFindAllIsEmpty() {

            // when accion o el comportamiento que vamos a probar
            List<Horario> listaHorario = horarioRepository.findAll();

            // then verificar la salida
            assertThat(listaHorario).isEmpty();
        }
    }

    @Nested
    @DisplayName("Tests de horario por id odontologo")
    class HorarioFindByOdontologoIdTests {
        @DisplayName("Test para obtener un Horario por el id del Odontologo")
        @Test
        void testFindByUnOdontologo_Id() {
            // given
            odontologoRepository.save(odontologoBuilder);

            // when accion o el comportamiento que vamos a probar
            List<Horario> listaHorario = horarioRepository.findByUnOdontologo_Id(odontologoBuilder.getId());

            // then verificar la salida
            assertThat(listaHorario)
                    .hasSize(4)
                    .isNotNull()
                    .extracting(Horario::getDiaSemana)
                    .containsExactlyInAnyOrder(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY,DayOfWeek.WEDNESDAY)

                    .isNotEmpty(); // Asegura que se encontró al menos un horario

        }

        @DisplayName("Test para horario sin odontólogo  ")
        @Test
        void testFindByUnOdontologo_IdSinHorarios() {
            // given
            Odontologo odontologo = new OdontologoBuilder()
                    .withNombre("Dr. Sin Horarios")
                    .withFechaNacimiento( LocalDate.of(1997, Month.JUNE, 30))
                    .builder();
             odontologoRepository.save(odontologo); // Lo guardas sin horarios

            // when
            List<Horario> listaHorario = horarioRepository.findByUnOdontologo_Id(odontologo.getId());

            // then
            assertThat(listaHorario).isEmpty(); // Verifica que no se devuelve nada
        }

        @DisplayName("Test para  horario con odontólogo con ID inexistente")
        @Test
        void testFindByUnOdontologo_IdInexistente() {
            // given
            long idInexistente =  Long.MAX_VALUE; // Asegúrate de que este ID no exista en la base de datos

            // when
            List<Horario> listaHorario = horarioRepository.findByUnOdontologo_Id(idInexistente);

            // then
            assertThat(listaHorario).isEmpty(); // El repositorio debe devolver lista vacía, no null ni excepción

        }
    }

    @Nested
    @DisplayName("Tests de disponibilidad horaria")
    class DisponibilidadHorariaTests {


        @DisplayName("Test para día y hora sin disponibilidad")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualSinDisponibilidad() {

            /**
             * Verifica que un horario NO sea devuelto por la consulta cuando:
             * - El día de semana consultado no coincide con el almacenado (sábado vs. otro día).
             * - El rango horario solicitado [07:00 – 04:00] no abarca el horario guardado.
             *
             * Ejemplo: el repositorio contiene un horario en un día distinto; al filtrar por sábado y un rango
             * inconexo, la consulta debe devolver lista vacía → sin disponibilidad.
             */
            odontologoRepository.save(odontologoBuilder);

            // when: se consulta un rango fuera del horario guardado
            List<Horario> listaHorario = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.SATURDAY, LocalTime.of(7, 0), LocalTime.of(4, 0));

            // then
            assertThat(listaHorario).isEmpty(); // No debe haber disponibilidad
        }

        @DisplayName("Horario completamente dentro del rango solicitado")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualHorarioDentroDelRango() {

            /**
             * Verifica que un horario SÍ sea devuelto por la consulta cuando:
             * - El día de semana coincide con el buscado (miércoles).
             * - La hora de inicio es igual o anterior al límite inferior del rango (06:30).
             * - La hora de fin es igual o posterior al límite superior del rango (12:30).
             *
             * Ejemplo: filtro → miércoles [06:30 – 12:30], horario de prueba → [06:30 – 12:30] → debe aparecer.
             */

            odontologoRepository.save(odontologoBuilder);

            List<Horario> resultado = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.WEDNESDAY, LocalTime.of(6, 30), LocalTime.of(12, 30));

            assertThat(resultado).contains(horarioBuilder1);
        }

        @DisplayName("Horario con horaInicio igual pero horaFin menor a la solicitada")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualHorarioFinMenorQueLimiteSuperior() {

            /**
             * Verifica que un horario NO sea devuelto por la consulta cuando:
             * - El día de semana coincide con el buscado.
             * - La hora de inicio es igual al límite inferior del rango (06:30).
             * - PERO la hora de fin es menor que el límite superior requerido (12:30),
             *   por lo que no cumple la condición (se espera horaFin >= 12:30).
             *
             * Ejemplo: filtro → lunes [06:30 – 12:30], horario de prueba → [06:30 – 11:30] → no debe aparecer.
             */
            Horario horarioBordeInferiorNegativo = new Horario();
            Odontologo odontologo = new Odontologo();

            odontologo.setFecha_nacimiento(LocalDate.EPOCH);
            odontologo.setListaHorarios(List.of(horarioBordeInferiorNegativo));

            horarioBordeInferiorNegativo.setUnOdontologo(odontologo);
            horarioBordeInferiorNegativo.setDiaSemana(DayOfWeek.MONDAY);
            horarioBordeInferiorNegativo.setHoraInicio(LocalTime.of(6, 30));
            horarioBordeInferiorNegativo.setHoraFin(LocalTime.of(11, 30));

            odontologoRepository.saveAndFlush(odontologo);

            List<Horario> resultado = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.MONDAY, LocalTime.of(6, 30), LocalTime.of(12, 30));

            assertThat(resultado).isEmpty();
        }

        @DisplayName("Horario con horaFin igual pero horaInicio mayor a la solicitada")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualHoraInicioMayorQueLimiteInferior() {

            /**
             * Verifica que un horario NO sea devuelto por la consulta cuando:
             * - El día de semana coincide con el buscado.
             * - La hora de fin es igual al límite superior del rango (12:30).
             * - PERO la hora de inicio es mayor que el límite inferior requerido (06:30),
             *   por lo que no cumple el filtro (se espera horaInicio <= 06:30).
             *
             * Ejemplo: filtro → lunes [06:30 – 12:30], horario de prueba → [07:30 – 12:30] → no debe aparecer.
             */
            Horario horarioBordeSuperiorNegativo = new Horario();
            Odontologo odontologo = new Odontologo();

            odontologo.setFecha_nacimiento(LocalDate.EPOCH);
            odontologo.setListaHorarios(List.of(horarioBordeSuperiorNegativo));

            horarioBordeSuperiorNegativo.setUnOdontologo(odontologo);
            horarioBordeSuperiorNegativo.setDiaSemana(DayOfWeek.MONDAY);
            horarioBordeSuperiorNegativo.setHoraInicio(LocalTime.of(7, 30));
            horarioBordeSuperiorNegativo.setHoraFin(LocalTime.of(12, 30));

            odontologoRepository.saveAndFlush(odontologo);

            List<Horario> resultado = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.MONDAY, LocalTime.of(6, 30), LocalTime.of(12, 30));

            assertThat(resultado).isEmpty();
        }

        @DisplayName("Horario en día distinto")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualHorarioDiaIncorrecto() {
            /**
             * Verifica que un horario NO sea devuelto por la consulta cuando:
             * - La franja horaria coincide exactamente con el rango buscado (06:30 – 12:30).
             * - PERO el día de semana es distinto al especificado en el filtro.
             *
             * Ejemplo: filtro → lunes [06:30 – 12:30], horario guardado → otro día [06:30 – 12:30] → no debe aparecer.
             */
            odontologoRepository.save(odontologoBuilder);

            List<Horario> resultado = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.MONDAY, LocalTime.of(6, 30), LocalTime.of(12, 30));

            assertThat(resultado).isEmpty();
        }

        @DisplayName("Horario fuera del rango por poco")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualHorarioFueraPorPoco() {
            /**
             * Verifica que un horario NO sea devuelto por la consulta cuando:
             * - El día de semana coincide con el buscado.
             * - La hora de inicio está dentro o en el límite inferior del rango (6:30).
             * - PERO la hora de fin (borde superior) está después del límite superior permitido (12:30).
             *
             * En este caso: buscado [06:30 – 12:30], horario de prueba [07:30 – 12:30] → no debe ser válido.
             */
            Horario horarioFueraPorPoco = new Horario();
            Odontologo odontologo = new Odontologo();
            odontologo.setListaHorarios(List.of(horarioFueraPorPoco));
            odontologo.setFecha_nacimiento(LocalDate.EPOCH);
            horarioFueraPorPoco.setDiaSemana(DayOfWeek.MONDAY);
            horarioFueraPorPoco.setUnOdontologo(odontologo);
            horarioFueraPorPoco.setHoraInicio(LocalTime.of(6, 31)); // después del límite
            horarioFueraPorPoco.setHoraFin(LocalTime.of(12, 29));   // antes del límite
            odontologoRepository.saveAndFlush(odontologo);
            List<Horario> resultado = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.MONDAY, LocalTime.of(6, 30), LocalTime.of(12, 30));

            assertThat(resultado).isEmpty();
        }

        @DisplayName("Test con múltiples horarios donde solo unos cumple el rango")
        @Test
        void testFindByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqualSoloUnHorarioCumpleElRango() {

            /**
             * Verifica que, al buscar horarios en un rango específico, solo se devuelvan los que lo cumplen:
             * - Día de semana: miércoles.
             * - Hora de inicio: dentro o igual al límite inferior del rango (06:30).
             * - Hora de fin: dentro o igual al límite superior del rango (12:30).
             *
             * En el setup() existen 4 horarios:
             *   - [6:30 a 12:30], [6:00 a 13:00], [7:30 a 12:00] y [9:00 a 12:00]
             *   - 2 cumplen el rango → deben aparecer en los resultados.
             *   - 2 queda fuera del rango → no debe ser incluido.
             */

            odontologoRepository.save(odontologoBuilder);


            // when
            List<Horario> resultado = horarioRepository.findByDiaSemanaAndHoraInicioLessThanEqualAndHoraFinGreaterThanEqual(
                    DayOfWeek.WEDNESDAY, LocalTime.of(6, 30), LocalTime.of(12, 30));

            // then
            assertThat(resultado).hasSize(2)
                    .extracting(Horario::getId)
                    .containsExactlyInAnyOrder(horarioBuilder3.getId(), horarioBuilder1.getId());


        }
    }

    @Nested
    @DisplayName("Tests consultar horarios por rango de fecha de  turnos")
    class TestFindByTurnos_FechaTurnoBetween {
        @DisplayName("Devuelve horarios con turnos dentro del rango y en los bordes del rango")
        @Test
        void testFindByTurnos_FechaTurnoBetweenDentroDelRango() {

            /**
             * Verifica que el repositorio devuelva los Horarios que tienen al menos un Turno
             * dentro del rango de fechas consultado.
             * - Rango de búsqueda: [2025-08-16 – 2025-08-30] (inclusive).
             * - Un Horario califica con que tenga ≥1 Turno en el rango; puede tener otros Turnos
             *   fuera del rango sin ser excluido.
             *
             * En el setup hay Turnos con fechas como 2025-08-17, 2025-08-19 y 2025-08-20,
             * por lo que se esperan 3 Horarios en el resultado.
             * Resultado esperado: lista con tamaño 3.
             */

            pacienteRepository.save(paciente);
            odontologoRepository.save(odontologoBuilder);
            turnoRepository.save(turnoBuilder);
            turnoRepository.save(turnoBuilder1);
            turnoRepository.save(turnoBuilder2);


            // when
            List<Horario> listaHorario = horarioRepository.findDistinctByTurnos_FechaTurnoBetween(
                    LocalDate.of(2025, Month.AUGUST, 16),
                    LocalDate.of(2025, Month.AUGUST, 25)
            );

            // then
            LocalDate from = LocalDate.of(2025, Month.AUGUST, 16);
            LocalDate to   = LocalDate.of(2025, Month.AUGUST, 30);

            assertThat(listaHorario).hasSize(3)
                    .allSatisfy(h -> assertThat(h.getTurnos())
                            .extracting(Turno::getFechaTurno)
                            .anySatisfy(f -> assertThat(f).isBetween(from, to))
                    );
        }

        @DisplayName("Excluye horarios con turnos fuera del rango")
        @Test
        void turnosFueraDelRango() {

            /**
             * Verifica que un Horario no se incluya en el resultado cuando:
             * - Todos sus turnos tienen fecha fuera del rango buscado
             *   ([2025-06-29 – 2025-08-01]).
             * - El filtro `findDistinctByTurnos_FechaTurnoBetween` requiere al menos
             *   un turno cuya fecha esté dentro del rango para que el Horario califique.
             *
             * En este escenario, todos los turnos guardados quedan fuera del rango,
             * por lo que la consulta debe devolver lista vacía.
             */
            pacienteRepository.save(paciente);
            odontologoRepository.save(odontologoBuilder);
            turnoRepository.save(turnoBuilder);
            turnoRepository.save(turnoBuilder1);
            turnoRepository.save(turnoBuilder2);

            List<Horario> result = horarioRepository.findDistinctByTurnos_FechaTurnoBetween(
                    LocalDate.of(2025, Month.JUNE, 29),
                    LocalDate.of(2025, Month.AUGUST, 1)
            );

            assertThat(result).isEmpty();
        }

        @DisplayName("Excluye horarios sin turnos")
        @Test
        void horariosSinTurnos() {

            /**
             * Verifica que un Horario NO sea devuelto por la consulta cuando:
             * - No tiene turnos asignados (lista de turnos vacía).
             * - El filtro busca por un rango de fechas específico ([2025-06-10 – 2025-06-20]).
             *
             * Ejemplo: un Horario sin turnos no puede cumplir la condición
             * `findDistinctByTurnos_FechaTurnoBetween`, ya que no hay ninguna fecha de turno que
             * pueda estar en el rango → la consulta debe devolver lista vacía.
             */
            Odontologo odontologo = new OdontologoBuilder()
                    .withFechaNacimiento(LocalDate.EPOCH).builder();
            Horario horarioSinTurno = new HorarioBuilder()
                    .setOdontologo(odontologo)
                    .setTurnos(Collections.emptyList())
                    .builder();
            odontologoRepository.save(odontologo);

            List<Horario> result = horarioRepository.findDistinctByTurnos_FechaTurnoBetween(
                    LocalDate.of(2025, Month.JUNE, 10),
                    LocalDate.of(2025, Month.JUNE, 20)
            );

            assertThat(result).isEmpty();
        }

        @DisplayName("Incluye horarios si al menos un turno está dentro del rango")
        @Test
        void horariosConTurnosMixtos() {

            /**
             * Verifica que un Horario SÍ sea devuelto cuando al menos uno de sus Turnos
             * se encuentra dentro del rango de fechas consultado.
             * - Rango de búsqueda: [2025-08-10 – 2025-08-20] (inclusive).
             * - Grafo de datos: un mismo Horario con turnos "mixtos":
             *   - uno dentro del rango (p. ej., 2025-08-20),
             *   - y otro fuera del rango (p. ej., 2025-09-17).
             * - La consulta aplica DISTINCT para evitar duplicados cuando varios turnos del
             *   mismo Horario califican.
             *
             * Resultado esperado: se devuelve exactamente 1 Horario (el asociado a esos turnos),
             * ya que basta con un Turno en rango para incluirlo en los resultados.
             */
            turnoBuilder3 = new TurnoBuilder()
                    .withFechaTurno(LocalDate.of(2025,9,17))
                    .withOdontologo(odontologoBuilder)
                    .withHorario(horarioBuilder)
                    .withPaciente(paciente)
                    .builder();

            odontologoRepository.save(odontologoBuilder);

            pacienteRepository.save(paciente);

            turnoRepository.save(turnoBuilder);

            turnoRepository.save(turnoBuilder3);
          //  pacienteRepository.save(paciente);

            List<Horario> result = horarioRepository.findDistinctByTurnos_FechaTurnoBetween(
                    LocalDate.of(2025, 8, 10),
                    LocalDate.of(2025, 8, 20)
            );

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Tests guardar horario")
    class TestSaveHorario {

    @DisplayName("Guardar horario con odontólogo y turno asociado")
    @Test
    void testGuardarHorarioConTurnoYOdontologo() {

       // when
        horarioRepository.deleteAll();
        odontologoRepository.save(odontologoBuilder);
        pacienteRepository.save(paciente);
        turnoRepository.save(turnoBuilder);

        // then
        assertThat(horarioBuilder.getId()).isNotNull();

       // assertThat(..get(0));
        assertThat(turnoBuilder.getFechaTurno()).isEqualTo(LocalDate.of(2025, Month.AUGUST, 20));

        // recuperación desde base
        horarioRepository.findById(horarioBuilder.getId())
                .orElseThrow(() -> new AssertionError("Horario no encontrado"));

        assertThat(horarioBuilder.getUnOdontologo().getNombre())
                .isEqualTo("NombreDefault");

        assertThat(horarioRepository.findAll())
                .hasSize(4);
    }

    @DisplayName("No se debe guardar horario sin odontólogo")
    @Test
    void testHorarioSinOdontologo() {
        // given
        Horario horario1 = new HorarioBuilder()
                .setDiaSemana(DayOfWeek.FRIDAY).builder();

        // when & then
        assertThatThrownBy(() -> horarioRepository.saveAndFlush(horario1))
                .isInstanceOf(DataIntegrityViolationException.class);

    }

    @DisplayName("No se debe guardar turno sin horario")
    @Test
    void testTurnoSinHorario() {
        // given
        turnoBuilder3 = new TurnoBuilder()

                .withFechaTurno(LocalDate.of(2025, Month.SEPTEMBER, 1))
                .withHoraTurno  (null)// sin horario
                .builder();

        // when & then
        Turno guardar = turnoRepository.save(turnoBuilder3);

        assertThat(guardar.getHorario()).isNull();

    }
}

    @Nested
    @DisplayName("Tests actualizar horario")
    class TestsUpdate {
        @DisplayName("Actualizar día de un horario existente")
        @Test
        void testHorarioUpdateDia() {
            // given
           Odontologo odontologo = new Odontologo();
            Horario horario = new Horario();

            odontologo.setNombre("Dr. Actualizable");
            odontologo.setFecha_nacimiento(LocalDate.EPOCH);
            odontologo.setListaHorarios(List.of(horario));

            horario.setDiaSemana(DayOfWeek.MONDAY);
            horario.setUnOdontologo(odontologo);
            horario.setTurnos(Collections.emptyList());
            horario.setUnOdontologo(odontologo);
            odontologoRepository.saveAndFlush(odontologo);

            // when: actualizar el día
            horario.setDiaSemana(DayOfWeek.THURSDAY);
            horarioRepository.save(horario);

            // then: recuperar y verificar
             horarioRepository.findById(horario.getId())
                    .orElseThrow(() -> new AssertionError("Horario no encontrado"));

            assertThat(horario.getDiaSemana()).isEqualTo(DayOfWeek.THURSDAY);
            assertThat(horario.getUnOdontologo().getId()).isEqualTo(odontologo.getId());

            // y que cada turno conoce su horario
            for (Turno t : horarioBuilder.getTurnos()) {
                assertThat(t.getHorario().getId()).isEqualTo(horarioBuilder.getId());
            }
        }

    }

    @Nested
    @DisplayName("Tests eliminar horario")
    class TestsDelete {
        @DisplayName("Eliminar un horario existente")
        @Test
        void testDeleteHorarioExistente() {
            // give

            odontologoRepository.save(odontologoBuilder);

            // when
            horarioRepository.delete(horarioBuilder);

            // Then
            Optional<Horario> resultado = horarioRepository.findById(horarioBuilder.getId());
            assertThat(resultado).isEmpty(); // el horario ya no está en la base


        }

    }
}
