package com.example.ClinicaDefinitiva.application.dto.actor.dentist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistAvailabilityStatus;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.WorkingHours;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;

import java.time.LocalDateTime;
import java.util.List;

public class CreateDentistDto {
    private final DentistId dentistId;
    private final Person personData;
    private final Specialties specialties;
    private final DentistAvailabilityStatus availabilityStatus;
    private final WorkingHours workingHours;
    private final String user;
    private final List<TimeSlot> timeSlotList;
    private final WeeklyAvailability availabilityList;
    private final Schedule schedule;
    private final LocalDateTime lastUpdate;

    public CreateDentistDto(DentistId dentistId,
                            Person personData,
                            Specialties specialties,
                            DentistAvailabilityStatus availabilityStatus,
                            WorkingHours workingHours,
                            String user,
                            List<TimeSlot> timeSlotList, WeeklyAvailability availabilityList, Schedule schedule, LocalDateTime lastUpdate) {
        this.dentistId = dentistId;
        this.personData = personData;
        this.specialties = specialties;
        this.availabilityStatus = availabilityStatus;
        this.workingHours = workingHours;
        this.user = user;
        this.timeSlotList = timeSlotList;
        this.availabilityList = availabilityList;
        this.schedule = schedule;
        this.lastUpdate = lastUpdate;
    }

    public WeeklyAvailability getAvailability() {
        return availabilityList;
    }

    public DentistAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public DentistId getDentistId() {
        return dentistId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Person getPersonData() {
        return personData;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Specialties getSpecialties() {
        return specialties;
    }

    public List<TimeSlot> getTimeSlotList() {
        return timeSlotList;
    }

    public String getUser() {
        return user;
    }

    public WorkingHours getWorkingHours() {
        return workingHours;
    }
}
