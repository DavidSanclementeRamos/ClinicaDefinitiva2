package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.dto.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Guardian {

    private final GuardianId guardianId;
    private final Person person;
    private final TypeGuardian typeGuardian;
    private final Schedule schedule;
    private final UserModel user;
    private final List<Patient> patientList;
    private final LocalDateTime lastUpdate;

    public Guardian (Builder b){

        this.user = b.user;
        this.typeGuardian = b.typeGuardian;
        this.schedule = b.schedule;
        this.patientList = List.copyOf( b.patientList);
        this.lastUpdate = b.lastUpdate;
        this.person = b.personData;
        this.guardianId = b.guardianId;
    }

    // Un responsable puede ser registrado si:
    // Usuario activo.
    // Cumple con la edad requerida.
    // El responsable no sede el tope de pacientes a cargos.
    public static Guardian registerGuardian(GuardianId id, Person data,
                                            UserModel user,
                                            List<Patient> patientList,
                                            TypeGuardian typeGuardian){
        ensureActiveUser(user);
        if (data.getAge().isBetween(22,60)){
            throw new IllegalArgumentException("El responsable no cuenta con la edad requerida para hacerse cargo de un responsable");
        }
        if(patientList.size() <= 6){
            throw new IllegalArgumentException("El responsable no puede tener mas de 6 pacientes ");
        }
        return new Builder()
                .withGuardianId(id)
                .withDocumentoEPS("")
                .withAddress(data.getAddress())
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withPhoneNumber(data.getPhoneNumber())
                .withUser(user)
                .withTypeGuardian(typeGuardian)
                .withPatient(patientList)
                .withDocumentoEPS(data.getDocumentoEPS())
                .build();
    }
    public static Guardian updateGuardianDataContact(GuardianId id ,Person data, UserModel user ){
        ensureActiveUser(user);
        LocalDateTime lastUpdate =  LocalDateTime.now();
        return new Builder()
                .withGuardianId(id)
                .withDocumentoEPS("")
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .build();
    }

    public static Guardian updateGuardianSensitiveData(GuardianId id ,Person data, UserModel user, TypeGuardian typeGuardian){
        ensureActiveUser(user);
        LocalDateTime lastUpdate =  LocalDateTime.now();
        return new Builder()
                .withGuardianId(id)
                .withDocumentoEPS("")
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withUser(user)
                .withTypeGuardian(typeGuardian)
                .withLastUpdate(lastUpdate)

                .build();
    }

    public void deactivateGuardian(){
        ensureActiveUser(user);
        if(patientList != null){
           throw new IllegalArgumentException("No se puede eliminar si tine pacientes asignados ");
        }
    }



    private static void ensureActiveUser(UserModel user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.PATIENT, "user=" + user);
        }
    }
    public UserModel getUser() {
        return user;
    }
    public TypeGuardian getTypeGuardian() {
        return typeGuardian;
    }
    public Schedule getSchedule() {
        return schedule;
    }
    public List<Patient> getPatientList() {
        return patientList;
    }
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
        private UserModel user;
        private TypeGuardian typeGuardian;
        private Schedule schedule;
        private List<Patient> patientList;
        private LocalDateTime lastUpdate;
        private String documentoEPS;
        private GuardianId guardianId;
        private Person personData;


        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullName(FullName f) { this.fullname = f; return this; }
        public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withUser(UserModel u){this.user = u; return this;}
        public Builder withTypeGuardian(TypeGuardian t){this.typeGuardian = t; return this;}
        public Builder withSchedule(Schedule s){this.schedule = s; return  this;}
        public Builder withPatient(Collection<Patient> p){this.patientList = new ArrayList<>(p); return this;}
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withDocumentoEPS(String d){this.documentoEPS = d; return this;}
        public Builder withPerson(Person p){this.personData = p; return this;}
        public Builder withGuardianId(GuardianId d){this.guardianId = d; return this;}
        public Guardian build () {
            return new Guardian(this);
        }
    }

}
