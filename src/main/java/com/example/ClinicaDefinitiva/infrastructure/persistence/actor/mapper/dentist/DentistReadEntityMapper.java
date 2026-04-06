package com.example.ClinicaDefinitiva.infrastructure.persistence.actor.mapper.dentist;

import com.example.ClinicaDefinitiva.domain.actor.model.Dentist;
import com.example.ClinicaDefinitiva.domain.actor.vo.*;
import com.example.ClinicaDefinitiva.domain.administration.operations.vo.ShiftId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.clinicalTreatments.vo.TreatmentId;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.DentistEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.entity.PersonEmbeddable;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DentistReadEntityMapper {

    public Dentist toDomain(DentistEntity entity) {
        Person person = mapToPerson(entity.getPerson());
        
        // Mapear Specialties desde String a Set<Specialty>
        Specialties specialties = mapToSpecialties(entity.getSpecialties());
        
        // Mapear WorkingHours desde JSON
        WorkingHours workingHours = mapToWorkingHours(entity.getWorkHoursJson());
        
        // Crear IDs
        DentistId dentistId = DentistId.of(entity.getId());
        UserIdentityId userIdentityId = UserIdentityId.from(entity.getUserIdentity().getId());
        ShiftId shiftId = entity.getShift() != null ? ShiftId.from(entity.getShift().getId()) : null;
        
        // Mapear AvailabilityStatus
        DentistAvailabilityStatus availabilityStatus = 
            DentistAvailabilityStatus.of(DentistAvailabilityStatus.Status.valueOf( entity.getAvailabilityStatus()));
        
        // Lista vacía de tratamientos (por ahora)
        List<TreatmentId> treatmentIds = new ArrayList<>();
        
        // Reconstruir Dentist
        return Dentist.reconstruct(
            dentistId,
            shiftId,
            person,
            specialties,
            userIdentityId,
            workingHours,
            entity.getLastUpdate(),
            treatmentIds,
            availabilityStatus,
            entity.getVacationStart(),
            entity.getVacationEnd(),
            entity.getDisabilityStart(),
            entity.getDisabilityEnd(),
            entity.getDisabilityNote()
        );
    }
    
    private Specialties mapToSpecialties(String specialtiesStr) {
        if (specialtiesStr == null || specialtiesStr.isBlank()) {
            return null;
        }
        
        // Asumiendo que viene como "ORTHODONTICS,ENDODONTICS,SURGERY"
        Set<Specialty> specialtySet = Arrays.stream(specialtiesStr.split(","))
                .map(String::trim)
                .map(Specialty::of)
                .collect(Collectors.toSet());
        
        return Specialties.of(specialtySet);
    }
    
    private WorkingHours mapToWorkingHours(String workHoursJson) {
        if (workHoursJson == null || workHoursJson.isBlank()) {
            return null;
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(workHoursJson);
            
            LocalTime startTime = LocalTime.parse(node.get("start").asText());
            LocalTime endTime = LocalTime.parse(node.get("end").asText());
            DayOfWeek dayOfWeek = DayOfWeek.valueOf(node.get("dayOfWeek").asText());
            int declaredHours = node.get("declaredHoursPerWeek").asInt();
            
            return WorkingHours.of(startTime, endTime, dayOfWeek, declaredHours);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing working hours JSON", e);
        }
    }
    

    private Person mapToPerson(PersonEmbeddable embeddable) {
    if (embeddable == null) return null;

    String addressStr = embeddable.getAddress();
     String[] parts = addressStr.split("\\|");
   
    Address address = Address.of(
        parts[0].trim(),
        parts[1].trim(),
        parts[2].trim(),
        parts[3].trim(),
        parts[4].trim()
    );

    PhoneNumber phoneNumber = PhoneNumber.of(embeddable.getPhoneNumber());
    DateOfBirth dateOfBirth = DateOfBirth.of(embeddable.getBirthDate());
    Age age = Age.of(dateOfBirth);
    BloodType bloodType = BloodType.fromLabel(embeddable.getBloodType());
    Document document = Document.of(embeddable.getDocumentNumber());

    // --- CORRECCIÓN DEL NOMBRE COMPLETO ---
    String fullNameStr = embeddable.getFullName();
    String firstName, lastName;
    int spaceIdx = fullNameStr.indexOf(' ');
    if (spaceIdx > 0) {
        firstName = fullNameStr.substring(0, spaceIdx);
        lastName = fullNameStr.substring(spaceIdx + 1);
    } else {
        firstName = fullNameStr;
        lastName = "";
    }
    FullName fullName = FullName.of(firstName, lastName);

    return Person.of(
        address,
        age,
        bloodType,
        dateOfBirth,
        document,
        embeddable.getEpsDocument(),
        fullName,
        phoneNumber
    );
}

}