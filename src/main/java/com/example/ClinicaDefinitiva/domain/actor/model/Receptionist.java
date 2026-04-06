package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.vo.*;

import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.errors.catalog.actor.ReceptionistError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.exceptions.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;

import java.time.LocalDateTime;
import java.util.Optional;

public class Receptionist   {

    private final ReceptionId id;
    private  Person person;
    private Sector sector;
    private final UserIdentityId userIdentityId;
    private LocalDateTime lastUpdate;

    private Receptionist(UserIdentityId userIdentityId, Sector sector, Person person, ReceptionId id) {
        this.lastUpdate = lastUpdate = LocalDateTime.now();
        this.userIdentityId = userIdentityId;
        this.sector = sector;
        this.person = person;
        this.id = id;
    }

    public static Receptionist registerReceptionist(
            Person data,
            UserIdentityId userIdentityId,
            Sector sector
             ) {
        if (!data.getAge().isBetween(20, 130)) {
            throw new BusinessRuleViolationException(ReceptionistError.ERR_RECEPTIONIST_AGE_INSUFFICIENT, EntityContext.RECEPTIONIST);
        }
        return new Receptionist( userIdentityId, sector, data, null);
    }



public void updateContactData(Optional<Address> newAddress, Optional<PhoneNumber> newPhoneNumber) {
    Address finalAddress = newAddress.orElse(this.person.getAddress());
    PhoneNumber finalPhoneNumber = newPhoneNumber.orElse(this.person.getPhoneNumber());
    
    if (newAddress.isPresent() || newPhoneNumber.isPresent()) {
        this.person = this.person.withContactData(finalAddress, finalPhoneNumber);
        this.lastUpdate = LocalDateTime.now();
    }
}

public void updateSensitiveData(
        Optional<BloodType> newBloodType,
        Optional<DateOfBirth> newDateOfBirth,
        Optional<Document> newDni,
        Optional<String> newDocumentoEPS,
        Optional<FullName> newFullName,
        Optional<Sector> newSector) {
    
    if (newBloodType.isPresent() || newDateOfBirth.isPresent() || newDni.isPresent()
        || newDocumentoEPS.isPresent() || newFullName.isPresent()) {
        
        BloodType finalBloodType = newBloodType.orElse(this.person.getBloodType());
        DateOfBirth finalDateOfBirth = newDateOfBirth.orElse(this.person.getDateOfBirth());
        Document finalDni = newDni.orElse(this.person.getDni());
        String finalDocumentoEPS = newDocumentoEPS.orElse(this.person.getDocumentoEPS());
        FullName finalFullName = newFullName.orElse(this.person.getFullname());
        
        this.person = this.person.withSensitiveData(
            finalBloodType, finalDateOfBirth, finalDni, finalDocumentoEPS, finalFullName
        );
    }
    
    newSector.ifPresent(s -> this.sector = s);
    
    this.lastUpdate = LocalDateTime.now();
}



    public UserIdentityId getUserIdentityId() {
        return userIdentityId;
    }

    public ReceptionId getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public Sector getSector() {
        return sector;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
    
    // Para persistencia
    public static Receptionist reconstruct(
            ReceptionId id,
            Person person,
            Sector sector,
            UserIdentityId userIdentityId,
            LocalDateTime lastUpdate) {

        return new Receptionist( userIdentityId, sector, person, id);
}


}