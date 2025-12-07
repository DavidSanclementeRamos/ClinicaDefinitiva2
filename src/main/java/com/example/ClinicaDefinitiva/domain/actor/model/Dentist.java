package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.Dentist.exception.DentistMinimumAgeException;
import com.example.ClinicaDefinitiva.domain.exceptions.WeeklyAvailabilityException;
import com.example.ClinicaDefinitiva.domain.exceptions.appointment.exception.PendingAppointmentsWithinHoursException;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.identity.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;
import com.example.ClinicaDefinitiva.domain.util.TimeIntervalRules;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public  class Dentist   {

    private final DentistId dentistId;
    private  Person personData;
    private  Specialties specialties;
    private  DentistAvailabilityStatus availabilityStatus;
    private  WorkingHours workingHours;
    private UserIdentity user;
    private  List<TimeSlot> timeSlotList;
    private  List<WeeklyAvailability> availabilityList;
    private  Schedule schedule;
    private  LocalDateTime lastUpdate;


    public Dentist(Builder b ) {
        this.personData = b.personData;
        this.specialties = b.specialties;
        this.user = b.user;
        this.workingHours = b.workingHours;
        this.timeSlotList = List.copyOf(b.timeSlotList);
        this.availabilityList = List.copyOf(b.availabilityList);
        this.availabilityStatus = b.availabilityStatus;
        this.lastUpdate = b.lastUpdate;
        this.dentistId = b.dentistId;
        this.schedule = new Schedule(b.appointments, new WeeklyAvailability(b.timeSlotList, List.of(b.workingHours)));
    }

    public Dentist(DentistId dentistId) {
        this.dentistId = dentistId;
    }

    // Factory method (fábrica semántica)
    public static Dentist registerDentist(
            DentistId id,
            Person data,
            Specialties specialties,
            UserIdentity user,
            WeeklyAvailability weeklyAvailability,
            LocalDateTime lastUpdate) {

        ensureActiveUser(user);

        if (!data.getAge().isBetween(25, 130)) {
            throw new DentistMinimumAgeException(ContextoEntidad.DENTIST, "Dentist must be at least 25 years old.");
        }

        if (!weeklyAvailability.HorasRegistradas(40)) {
            throw new WeeklyAvailabilityException(ContextoEntidad.DENTIST, "Debe registrar al menos 40 horas semanales");
        }
        // build
        return new Builder()
                .withDentistId(id)
                .withAddress(data.getAddress())
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullname(data.getFullname())
                .withPhoneNumber(data.getPhoneNumber())
                .withSpecialties(specialties)
                .withUser(user)
                .withTimeSlots(weeklyAvailability.getSlots())
                .withLastUpdate(lastUpdate)
                .withDocumentoEPS(data.getDocumentoEPS())
                .withAvailabilityStatus(DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE))
                .build();
        }

    public void updateSensitiveData(Person data, UserIdentity user, Specialties specialties, WorkingHours workingHours) {
        ensureActiveUser(user);

        if (!data.getAge().isBetween(25, 130)) {
            throw new DentistMinimumAgeException(ContextoEntidad.DENTIST, "El odontólogo debe tener al menos 25 años.");
        }
        this.personData = data.updateSensitive(
                data.getAge(),
                data.getBloodType(),
                data.getDateOfBirth(),
                data.getDni(),
                data.getDocumentoEPS(),
                data.getFullname()
        );
        this.lastUpdate = LocalDateTime.now();
        this.specialties = specialties;
        this.workingHours = workingHours;
    }

    public void updateContactData(Person data, UserIdentity user) {
        ensureActiveUser(user);

        this.personData = personData.updateContact(data.getAddress(), data.getPhoneNumber());
        this.lastUpdate= LocalDateTime.now();
    }

    // se puede desactivar si:
    // No tiene citas pendientes en X tiempo
    // el usuario está inactivo
    public Dentist deactivate(UserIdentity user, int hoursRange) {
        ensureActiveUser(user);
        if (this.schedule.hasAppointmentsWithinHours(hoursRange)) {
            throw new PendingAppointmentsWithinHoursException(ContextoEntidad.DENTIST, "");
        }
        return new Dentist.Builder()
                .withAvailabilityStatus(DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.UNAVAILABLE))
                .build();
    }
    // puede agendar una cita si
    // usuario está activo
    // el horario de trabajo está dentro del rango solicitado
    public void canScheduleBetween(LocalDateTime start,LocalDateTime end ) {
        ensureActiveUser();
        if (!workingHours.isWithinRange(start, end)){
             throw new IllegalArgumentException("El horario solicitado esta fuera de la jornada laboral del odontólogo ");
        }
    }

    // un odontólogo puede tener vacaciones si:
    public void validateVacationRequest(LocalDateTime vacationStart, LocalDateTime vacationEnd) {
        ensureActiveUser();
        if (!TimeIntervalRules.isValid(vacationStart, vacationEnd)) {
            throw new IllegalArgumentException("Rango de fechas inválido.");
        }

        List<Appointment> conflicts = schedule.getAppointments().stream()
                .filter(a -> TimeIntervalRules.overlaps(
                        a.getStart(), a.getEnd(), vacationStart, vacationEnd))
                .toList();

        if (!conflicts.isEmpty()) {
            throw new IllegalArgumentException("No se puede tomar vacaciones: hay " +
                    conflicts.size() + " cita" + (conflicts.size() > 1 ? "s" : "") +
                    " en conflicto.");
        }
    }
    // Un odontólogo puede reagendar una cita si:
    // usuario está activo.
    // La nueva fecha está en su horario laboral
    public void validateReschedule(LocalDateTime start, LocalDateTime end) {
        ensureActiveUser();
        if (!canWorkBetween(start, end)) {
            throw new IllegalArgumentException("La nueva fecha no está dentro del horario laboral del odontólogo.");
        }
    }
    /**
     * Verifica si un intervalo de tiempo está dentro del horario laboral del odontólogo.
     */
    public boolean canWorkBetween(LocalDateTime start, LocalDateTime end) {
        return workingHours != null && workingHours.isWithinRange(start, end);
    }

    // Validar cumplimiento de horas trabajadas
    public boolean isCompliantWithDeclaredWorkingHours() {
        return workingHours.isCompliantWithWorkingHours(schedule.getWeeklyAvailability());
    }
    // para operaciones del agregado)
    private void ensureActiveUser() {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "user=" + user);
        }
    }
    // para factories.
    private static void ensureActiveUser(UserIdentity user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.DENTIST, "user=" + user);
        }
    }


    // Getters inmutables
    //public List<TimeSlot> getTimeSlotList() { return List.copyOf(timeSlotList); }
    public List<WeeklyAvailability> getAvailabilityList() { return List.copyOf(availabilityList); }
    public Schedule getSchedule() { return schedule; }
    public UserIdentity getUser() { return user; }
    public DentistAvailabilityStatus getAvailabilityStatus() {return availabilityStatus;}
    public LocalDateTime getLastUpdate() {return lastUpdate;}
    public Specialties getSpecialties() {return specialties;}
    public List<TimeSlot> getTimeSlotList() {return timeSlotList;}
    public WorkingHours getWorkingHours() {return workingHours;}
    public DentistId getDentistId() {return dentistId;}
    public Person getPersonData() {return personData;}

    // Builder estático
    public static final class Builder {
        private Address address;
        private Age age;
        private BloodType bloodType;
        private DateOfBirth dateOfBirth;
        private String dni;
        private FullName fullname;
        private Long id;
        private PhoneNumber phoneNumber;
        private Specialties specialties;
        private UserIdentity user;
        private WorkingHours workingHours;
        private List<TimeSlot> timeSlotList = new ArrayList<>();
        private List<WeeklyAvailability> availabilityList = new ArrayList<>();
        private Collection<Appointment> appointments = new ArrayList<>();
        private DentistAvailabilityStatus availabilityStatus;
        private LocalDateTime lastUpdate;
        private String documentoEPS;
        private Person personData;
        private DentistId dentistId;

        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullname(FullName f) { this.fullname = f; return this; }
       // public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withSpecialties(Specialties s) { this.specialties = s; return this; }
        public Builder withUser(UserIdentity u) { this.user = u; return this; }
        public Builder withWorkingHours(WorkingHours w) { this.workingHours = w; return this; }
        public Builder withTimeSlots(Collection<TimeSlot> slots) { this.timeSlotList = new ArrayList<>(slots); return this; }
        public Builder withAvailabilityList(Collection<WeeklyAvailability> availability) { this.availabilityList = new ArrayList<>(availability); return this; }
        public Builder withAppointments(Collection<Appointment> apps) { this.appointments = apps == null ? new ArrayList<>() : new ArrayList<>(apps); return this; }
        public Builder withAvailabilityStatus(DentistAvailabilityStatus s) { this.availabilityStatus = s; return this; }
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withDocumentoEPS(String d){this.documentoEPS = d; return this;}
        public Builder withPerson(Person p){this.personData = p; return this;}
        public Builder withDentistId(DentistId d){this.dentistId = d; return this;}
        public Dentist build() {

            return new Dentist(this);
        }
    }

}
