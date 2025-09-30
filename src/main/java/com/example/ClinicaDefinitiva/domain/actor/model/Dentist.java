package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.dto.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;
import com.example.ClinicaDefinitiva.domain.schedule.model.Appointment;
import com.example.ClinicaDefinitiva.domain.schedule.model.Availability;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public  class Dentist  extends Person {


    private final Specialties specialties;
    private final DentistAvailabilityStatus availabilityStatus;
    private final WorkingHours workingHours;
    private final UserModel user;
    private final List<TimeSlot> timeSlotList;
    private final List<Availability> availabilityList;
    private final Schedule schedule;

    private Dentist(Builder b) {
        super(b.address, b.age, b.bloodType, b.dateOfBirth, b.dni, b.fullname, b.id, b.phoneNumber);
        this.specialties = b.specialties;
        this.user = b.user;
        this.workingHours = b.workingHours;
        this.timeSlotList = List.copyOf(b.timeSlotList);
        this.availabilityList = List.copyOf(b.availabilityList);
        this.availabilityStatus = b.availabilityStatus;
        this.schedule = new Schedule(b.appointments, new WeeklyAvailability(b.timeSlotList));
    }

    // Factory method (fábrica semántica)
    public static Dentist register(PersonRegistrationData data,
                                   Specialties specialties,
                                   UserModel user,
                                   WorkingHours workingHours,
                                   WeeklyAvailability weeklyAvailability,
                                   Collection<Appointment> initialAppointments) {
        if (!data.getAge().isBetween(25, 130)) {
            throw new BusinessRuleViolationException("Dentist must be at least 25 years old.");
        }
        if (!user.isActive()) {
            throw new BusinessRuleViolationException("El usuario no puede estar inactivo");
        }
        if (!weeklyAvailability.cumpleMinimoHoras(10)) {
            throw new BusinessRuleViolationException("Debe registrar al menos 10 horas semanales");
        }
        // build
        return new Builder()
                .withAddress(data.getAddress())
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullname(data.getFullname())
                .withId(data.getId())
                .withPhoneNumber(data.getPhoneNumber())
                .withSpecialties(specialties)
                .withUser(user)
                .withWorkingHours(workingHours)
                .withTimeSlots(weeklyAvailability.getSlots())
                .withAppointments(initialAppointments)
                .withAvailabilityStatus(DentistAvailabilityStatus.from(DentistAvailabilityStatus.Status.AVAILABLE))
                .build();
        }


    // Operaciones de instancia
    public List<Appointment> citasActivasEnLasProximas24Horas() {
        return schedule.upcomingWithinHours(24);
    }

    public boolean tieneCitasActivasEnLasProximas24Horas() {
        return schedule.hasAppointmentsWithinHours(24);
    }

    public boolean canScheduleAt(LocalDateTime dateTime) {
        return user.isActive() && workingHours.isWithin(dateTime) && schedule.canScheduleAt(dateTime);
    }


    public void deactivate() {
        if (schedule.hasAppointmentsWithinHours(24)) {
            throw new BusinessRuleViolationException("Tiene citas pendientes en las proximas 24 horas");
        }
        if (!schedule.getAppointments().stream().filter(Appointment::esFutura).collect(Collectors.toList()).isEmpty()) {
            throw new BusinessRuleViolationException("No se puede desactivar: tiene citas futuras");
        }
        // mutación controlada: delega a user o status
        // ejemplo: marcar availabilityStatus como INACTIVE o similar
        // aquí deberías invocar la lógica que cambia el estado del usuario/dentist
    }

    // Getters inmutables
    public List<TimeSlot> getTimeSlotList() { return List.copyOf(timeSlotList); }
    public List<Availability> getAvailabilityList() { return List.copyOf(availabilityList); }
    public Schedule getSchedule() { return schedule; }
    public UserModel getUser() { return user; }


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
        private UserModel user;
        private WorkingHours workingHours;
        private List<TimeSlot> timeSlotList = new ArrayList<>();
        private List<Availability> availabilityList = new ArrayList<>();
        private Collection<Appointment> appointments = new ArrayList<>();
        private DentistAvailabilityStatus availabilityStatus;

        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullname(FullName f) { this.fullname = f; return this; }
        public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withSpecialties(Specialties s) { this.specialties = s; return this; }
        public Builder withUser(UserModel u) { this.user = u; return this; }
        public Builder withWorkingHours(WorkingHours w) { this.workingHours = w; return this; }
        public Builder withTimeSlots(Collection<TimeSlot> slots) { this.timeSlotList = new ArrayList<>(slots); return this; }
        public Builder withAvailabilityList(Collection<Availability> availability) { this.availabilityList = new ArrayList<>(availability); return this; }
        public Builder withAppointments(Collection<Appointment> apps) { this.appointments = apps == null ? new ArrayList<>() : new ArrayList<>(apps); return this; }
      //  public Builder withAppointments(Collection<Appointment> apps) { this.appointments = apps == null ? new ArrayList<>() : new ArrayList<>(apps); return this; }
        public Builder withAvailabilityStatus(DentistAvailabilityStatus s) { this.availabilityStatus = s; return this; }

        public Dentist build() {
            // invariantes mínimas
            if (address == null) throw new BusinessRuleViolationException("Address is required");
            if (age == null) throw new BusinessRuleViolationException("Age is required");
            if (user == null) throw new BusinessRuleViolationException("User is required");
            if (!user.isActive()) throw new BusinessRuleViolationException("User must be active");
            return new Dentist(this);
        }
    }

}
