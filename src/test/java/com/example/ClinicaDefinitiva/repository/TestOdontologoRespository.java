package com.example.ClinicaDefinitiva.repository;


import com.example.ClinicaDefinitiva.Enum.Especialidades;
import com.example.ClinicaDefinitiva.builder.OdontologoBuilder;
import com.example.ClinicaDefinitiva.builder.TurnoBuilder;
import com.example.ClinicaDefinitiva.builder.UsuarioBuilder;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Turno;
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

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@ActiveProfiles("test") // Usa application-test.properties
@DisplayName("OdontologoRepository")
public class TestOdontologoRespository {

        @Autowired
        OdontologoRepository odontologoRepository;
        @Autowired
        UsuarioRepository usuarioRepository;
        @Autowired
        TurnoRepository turnoRepository;
        @Autowired
        EntityManager em;
        @Autowired
        PacienteRepository pacienteRepository;

        // Datos base compartidos para ramas de prueba
        private Usuario usuarioA, usuarioB, usuarioC, usuarioSinOdontologo;
        private Odontologo odoA, odoB, odoC;
        private Turno tAinLower, tAinUpper, tAout, tBout;

        @BeforeEach
        void setUp() {
            // Limpieza para aislar cada prueba
            turnoRepository.deleteAll();
            odontologoRepository.deleteAll();
            usuarioRepository.deleteAll();

            // Usuarios base
            usuarioA = new UsuarioBuilder().withNombreUsuario("userA").builder();
            usuarioB = new UsuarioBuilder().withNombreUsuario("userB").builder();
            usuarioC = new UsuarioBuilder().withNombreUsuario("userC").builder();
            usuarioSinOdontologo = new UsuarioBuilder().withNombreUsuario("userNoOdo").builder();
            usuarioRepository.saveAll(List.of(usuarioA, usuarioB, usuarioC, usuarioSinOdontologo));

            // Odontólogos base (mínimos y sincronizados)
            odoA = new OdontologoBuilder()
                    .withUsuario(usuarioA)
                    .withFechaNacimiento(LocalDate.EPOCH)
                    .withEspecialidad(Especialidades.CIRUGIA_ORAL)
                    .withTelefono("1231231223")
                    .builder();
            odoB = new OdontologoBuilder()
                    .withUsuario(usuarioB)
                    .withFechaNacimiento(LocalDate.EPOCH)
                    .withEspecialidad(Especialidades.ENDODONCIA)
                    .withTelefono("123123345")

                    .builder();
            odoC = new OdontologoBuilder()
                    .withUsuario(usuarioC)
                    .withFechaNacimiento(LocalDate.EPOCH)
                    .withEspecialidad(Especialidades.CIRUGIA_ORAL)
                    .withTelefono("4441231223")

                    .builder();
            odontologoRepository.saveAll(List.of(odoA, odoB, odoC));
            Paciente paciente = new Paciente();
            paciente.setFecha_nacimiento(LocalDate.EPOCH);
            pacienteRepository.save(paciente);

            // Turnos para ramas de rango
            tAinLower = new TurnoBuilder()
                    .withOdontologo(odoA)
                    .withPaciente(paciente)
                    .withFechaTurno(LocalDate.of(2025, 8, 17)) // borde inferior
                    .builder();
            tAinUpper = new TurnoBuilder()
                    .withOdontologo(odoA)
                    .withPaciente(paciente)
                    .withFechaTurno(LocalDate.of(2025, 8, 20)) // borde superior
                    .builder();
            tAout = new TurnoBuilder()
                    .withOdontologo(odoA)
                    .withPaciente(paciente)
                    .withFechaTurno(LocalDate.of(2025, 9, 10)) // fuera de rango típico
                    .builder();
            tBout = new TurnoBuilder()
                    .withOdontologo(odoB)
                    .withPaciente(paciente)
                    .withFechaTurno(LocalDate.of(2025, 6, 1)) // fuera
                    .builder();
            turnoRepository.saveAll(List.of(tAinLower, tAinUpper, tAout, tBout));

            // Asegurar estado sincronizado en BD
            em.flush();
            em.clear();
        }



        @DisplayName("save: persiste un odontólogo con usuario y especialidad mínimas")
        @Test
        void save_Odontolgo() {
            /**
             * Verifica que se pueda persistir un Odontólogo con los atributos mínimos
             * requeridos y la relación con Usuario sincronizada.
             * - Se usa builder para crear las instancias con el mínimo indispensable.
             * - Se espera ID no nulo y relación Usuario establecida.
             */
            Usuario u = usuarioRepository.save(new UsuarioBuilder().withNombreUsuario("nuevo").builder());
            Odontologo nuevo = new OdontologoBuilder()
                    .withUsuario(u)
                    .withFechaNacimiento(LocalDate.of(1990, 1, 1))
                    .withEspecialidad(Especialidades.CIRUGIA_ORAL)
                    .builder();

            Odontologo guardado = odontologoRepository.save(nuevo);

            assertThat(guardado.getId()).isNotNull();
            assertThat(guardado.getUnUsuario().getId()).isEqualTo(u.getId());
            assertThat(guardado.getEspecialidad()).isEqualTo(Especialidades.CIRUGIA_ORAL);
        }

        @DisplayName("update: muta campos escalares y refleja cambios al recargar")
        @Test
        void update_actualizaCampos() {
            /**
             * Verifica que el dirty checking aplique sobre una entidad gestionada:
             * - Cambia la especialidad y persiste.
             * - Recarga desde BD y valida los nuevos valores.
             * Nota: no se usan builders en la mutación para reflejar flujo real (cargar-mutuar-guardar).
             */
            Odontologo managed = odontologoRepository.findById(odoA.getId()).orElseThrow();
            managed.setEspecialidad(Especialidades.ORTODONCIA);

            em.flush();
            em.clear();

            Odontologo reloaded = odontologoRepository.findById(odoA.getId()).orElseThrow();
            assertThat(reloaded.getEspecialidad()).isEqualTo(Especialidades.ORTODONCIA);
        }

        @DisplayName("findById: devuelve el odontólogo cuando existe")
        @Test
        void findById_existente() {
            /**
             * Verifica que findById retorne presente para un ID existente.
             */
            assertThat(odontologoRepository.findById(odoB.getId()))
                    .isPresent()
                    .get()
                    .extracting(Odontologo::getId)
                    .isEqualTo(odoB.getId());
        }

        @DisplayName("findById: Optional.empty cuando no existe")
        @Test
        void findById_inexistente() {
            /**
             * Verifica que findById retorne Optional.empty para un ID inexistente.
             */
            assertThat(odontologoRepository.findById(-12345L)).isNotPresent();
        }

        @DisplayName("findAll: devuelve todos los odontólogos persistidos")
        @Test
        void findAll_devuelveTodos() {
            /**
             * Verifica que findAll retorna el conjunto completo de registros.
             * En el setup hay 3 odontólogos persistidos.
             */
            List<Odontologo> all = odontologoRepository.findAll();
            assertThat(all)
                    .hasSize(3)
                    .extracting(Odontologo::getId)
                    .containsExactlyInAnyOrder(odoA.getId(), odoB.getId(), odoC.getId());
        }

        @DisplayName("delete: elimina el odontólogo y no aparece en consultas posteriores")
        @Test
        void delete_eliminaRegistro() {
            /**
             * Verifica que un registro eliminado no se pueda volver a recuperar.
             * Se elimina un odontólogo sin turnos asociados para evitar efectos de FK/cascade.
             */
            odontologoRepository.delete(odoC);
            em.flush();
            em.clear();

            assertThat(odontologoRepository.findById(odoC.getId())).isNotPresent();
        }



        @DisplayName("findByUnUsuario_Id: presente cuando el odontólogo referencia al usuario")
        @Test
        void findByUnUsuario_Id_presente() {
            /**
             * Verifica que el repositorio devuelva el Odontólogo cuyo Usuario
             * tiene el ID indicado.
             * - Setup: odontólogo A con usuarioA.
             * - Expectativa: Optional.present con el ID de odontólogo A.
             */
            var opt = odontologoRepository.findByUnUsuario_Id(usuarioA.getId());
            assertThat(opt).isPresent();
            assertThat(opt.get().getId()).isEqualTo(odoA.getId());
        }

        @DisplayName("findByUnUsuario_Id: Optional.empty para usuario sin odontólogo asociado")
        @Test
        void findByUnUsuario_Id_usuarioSinOdontologo() {
            /**
             * Verifica que, si el usuario existe pero no está asociado a ningún odontólogo,
             * el resultado sea Optional.empty.
             */
            var opt = odontologoRepository.findByUnUsuario_Id(usuarioSinOdontologo.getId());
            assertThat(opt).isNotPresent();
        }

        @DisplayName("findByUnUsuario_Id: Optional.empty para usuario inexistente")
        @Test
        void findByUnUsuario_Id_inexistente() {
            /**
             * Verifica que la búsqueda por un ID de usuario inexistente retorne Optional.empty.
             */
            assertThat(odontologoRepository.findByUnUsuario_Id(999999L)).isNotPresent();
        }



        @DisplayName("findByEspecialidad: devuelve solo los de la especialidad solicitada")
        @Test
        void findByEspecialidad_match() {
            /**
             * Verifica que se devuelvan únicamente los odontólogos con la especialidad solicitada.
             * - Setup: A y C son CIRUGIA; B es ENDODONCIA.
             * - Expectativa: A y C en la lista; B no aparece.
             */
            List<Odontologo> cirujanos = odontologoRepository.findByEspecialidad(Especialidades.CIRUGIA_ORAL);

            assertThat(cirujanos)
                    .hasSize(2)
                    .extracting(Odontologo::getId)
                    .containsExactlyInAnyOrder(odoA.getId(), odoC.getId());
        }

        @DisplayName("findByEspecialidad: lista vacía cuando no hay coincidencias")
        @Test
        void findByEspecialidad_sinCoincidencias() {
            /**
             * Verifica que la consulta retorne lista vacía cuando no hay registros
             * con la especialidad solicitada.
             */
            List<Odontologo> perio = odontologoRepository.findByEspecialidad(Especialidades.PERIODONCIA);
            assertThat(perio).isEmpty();
        }



        @DisplayName("findByListaTurnos_FechaTurnoBetween: incluye bordes")
        @Test
        void findByListaTurnos_FechaTurnoBetween_bordes() {
            /**
             * Verifica que:
             * - Between es inclusivo: turnos en los límites inferior/superior califican.
             * - No admite duplicado de odontólogo.
             *
             * Setup: Odontólogo A con turnos en 2025-08-17 y 2025-08-20 (ambos bordes).
             * Rango: [2025-08-17 – 2025-08-20].
             * Expectativa: la lista contiene 2 filas correspondientes al mismo odontólogo A.
             */
            LocalDate desde = LocalDate.of(2025, 8, 17);
            LocalDate hasta = LocalDate.of(2025, 8, 20);

            List<Odontologo> lista = odontologoRepository.findDistinctByListaTurnos_FechaTurnoBetween(desde, hasta);

            assertThat(lista).hasSize(1);
            assertThat(lista).extracting(Odontologo::getId)
                    .containsOnly(odoA.getId()); // ambas filas pertenecen a A
            assertThat(lista).extracting(Odontologo::getId).contains(odoA.getId(), odoA.getId());
        }

        @DisplayName("findByListaTurnos_FechaTurnoBetween: incluye odontólogo si tiene ≥1 turno en rango (mixto)")
        @Test
        void findByListaTurnos_FechaTurnoBetween_mixtoDentroFuera() {
            /**
             * Verifica que un odontólogo con turnos dentro y fuera del rango sea incluido
             * si al menos uno cae dentro.
             *
             * Setup: A tiene turnos dentro (17, 20) y fuera (09-10).
             * Rango: [2025-08-16 – 2025-08-18] (solo 17 califica).
             * Expectativa: aparece una fila (A) porque solo hay un turno en rango.
             */
            LocalDate desde = LocalDate.of(2025, 8, 16);
            LocalDate hasta = LocalDate.of(2025, 8, 18);

            List<Odontologo> lista = odontologoRepository.findDistinctByListaTurnos_FechaTurnoBetween(desde, hasta);

            assertThat(lista).hasSize(1);
            assertThat(lista.get(0).getId()).isEqualTo(odoA.getId());
        }

        @DisplayName("findByListaTurnos_FechaTurnoBetween: excluye odontólogos sin turnos en el rango")
        @Test
        void findByListaTurnos_FechaTurnoBetween_sinCoincidencias() {
            /**
             * Verifica que se devuelva lista vacía si ningún turno cae dentro del rango.
             *
             * Setup: A (17, 20, 09-10), B (06-01).
             * Rango: [2025-07-01 – 2025-07-15].
             * Expectativa: lista vacía (ningún turno está en el rango).
             */
            LocalDate desde = LocalDate.of(2025, 7, 1);
            LocalDate hasta = LocalDate.of(2025, 7, 15);

            List<Odontologo> lista = odontologoRepository.findDistinctByListaTurnos_FechaTurnoBetween(desde, hasta);

            assertThat(lista).isEmpty();
        }

        @DisplayName("findByListaTurnos_FechaTurnoBetween: no incluye odontólogos sin turnos")
        @Test
        void findByListaTurnos_FechaTurnoBetween_odontologoSinTurnosExcluido() {
            /**
             * Verifica que un odontólogo sin turnos no aparece en resultados de Between.
             *
             * Setup: C sin turnos asociados.
             * Rango: [2025-08-16 – 2025-08-25].
             * Expectativa: los IDs retornados nunca incluyen el de C.
             */
            LocalDate desde = LocalDate.of(2025, 8, 16);
            LocalDate hasta = LocalDate.of(2025, 8, 25);

            List<Odontologo> lista = odontologoRepository.findDistinctByListaTurnos_FechaTurnoBetween(desde, hasta);

            assertThat(lista).extracting(Odontologo::getId).doesNotContain(odoC.getId());
        }
    }
