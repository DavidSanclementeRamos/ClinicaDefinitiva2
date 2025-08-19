package com.example.ClinicaDefinitiva;

import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.persistence.entity.Horario;
import com.example.ClinicaDefinitiva.persistence.entity.Odontologo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

public class TestDataFactory {

    private Horario crearHorario(DayOfWeek dia, String nombreOdontologo, String dni) {
        Odontologo od = new Odontologo();
        od.setDni(dni);
        od.setNombre(nombreOdontologo);
        od.setApellido("Porunga");
        od.setFecha_nacimiento(LocalDate.of(1995, Month.JUNE, 10));
      //  odontologoRepository.save(od);

        Horario h = new Horario();
        h.setEstado(Estado.ACTIVO);
        h.setDiaSemana(dia);
        h.setHoraInicio(LocalTime.of(9, 0));
        h.setHoraFin(LocalTime.of(12, 0));
        h.setUnOdontologo(od);
        return null ;// horarioRepository.save(h);
    }
}
