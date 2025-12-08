package com.example.ClinicaDefinitiva.domain.service;

import com.example.ClinicaDefinitiva.application.dto.person.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.WorkingHours;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.Dentist.exception.DentistMinimumAgeException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.WeeklyAvailabilityException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;

import java.time.LocalDateTime;
import java.util.Collection;

public class DentistOrchestratorService {
/** SERA ELIMINADA XD*/
    public Dentist registerDentist(PersonRegistrationData data,
                                   Specialties specialties,
                                   UserIdentity user,
                                   WorkingHours workingHours,
                                   WeeklyAvailability weeklyAvailability,
                                   Collection<Appointment> initialAppointments) {

        validarDatosIniciales(data, user, weeklyAvailability);

        // delega la construcción al agregado
        Dentist dentist = Dentist.register(
                data,
                specialties,
                user,
                workingHours,
                weeklyAvailability,
                initialAppointments
        );

        // validación post-construcción
        if (!dentist.isCompliantWithDeclaredWorkingHours()) {
            throw new IllegalStateException("El horario declarado no coincide con la disponibilidad registrada.");
        }

        return dentist;
    }

    public void solicitarVacaciones(Dentist dentist, LocalDateTime inicio, LocalDateTime fin) {
        dentist.validateVacationRequest(inicio, fin);
        // Aquí podrías emitir un evento de dominio, o mutar el estado si es legítimo
    }

    public void desactivarDentista(Dentist dentist) {
        dentist.deactivate();
        // Aquí podrías cambiar el estado del usuario o emitir un evento
    }

    private void validarDatosIniciales(PersonRegistrationData data, UserIdentity user, WeeklyAvailability weeklyAvailability) {
        if (!data.getAge().isBetween(25, 130)) {
            throw new DentistMinimumAgeException(ContextoEntidad.DENTIST, "Dentist must be at least 25 years old.");
        }
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "El usuario no puede estar inactivo");
        }
        if (!weeklyAvailability.HorasRegistradas(40)) {
            throw new WeeklyAvailabilityException(ContextoEntidad.DENTIST, "Debe registrar al menos 40 horas semanales");
        }
    }


}
