package com.example.ClinicaDefinitiva.application.dto.actor.dentist;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.Person;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.DentistAvailabilityStatus;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.Specialties;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.WorkingHours;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.domain.schedule.model.TimeSlot;
import com.example.ClinicaDefinitiva.domain.schedule.valueObject.WeeklyAvailability;

import java.time.LocalDateTime;
import java.util.List;

public class ReadDentistDto {
    private  String dentistId;
    private Person personData;
    private  Specialties specialties;
    private  DentistAvailabilityStatus availabilityStatus;
    private  WorkingHours workingHours;
    private  String user;
    private  List<TimeSlot> timeSlotList;
    private  List<WeeklyAvailability> availabilityList;
    private  Schedule schedule;
    private  LocalDateTime lastUpdate;

    public ReadDentistDto(String dentistId, Person personData, Specialties specialties, DentistAvailabilityStatus availabilityStatus, WorkingHours workingHours, String user, List<TimeSlot> timeSlotList, List<WeeklyAvailability> availabilityList, Schedule schedule, LocalDateTime lastUpdate) {
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

    public ReadDentistDto() {

    }

    public void setAvailabilityList(List<WeeklyAvailability> availabilityList) {
        this.availabilityList = availabilityList;
    }

    public void setAvailabilityStatus(DentistAvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public void setDentistId(String dentistId) {
        this.dentistId = dentistId;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public void setPersonData(Person personData) {
        this.personData = personData;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public void setSpecialties(Specialties specialties) {
        this.specialties = specialties;
    }

    public void setTimeSlotList(List<TimeSlot> timeSlotList) {
        this.timeSlotList = timeSlotList;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setWorkingHours(WorkingHours workingHours) {
        this.workingHours = workingHours;
    }

    public List<WeeklyAvailability> getAvailabilityList() {
        return availabilityList;
    }

    public DentistAvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public String getDentistId() {
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


