package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.Enum.TipoResponsable;
import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.dto.PersonRegistrationData;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.identity.model.UserModel;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import com.example.ClinicaDefinitiva.persistence.entity.Paciente;
import com.example.ClinicaDefinitiva.persistence.entity.Usuario;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Guardian  extends Person{

    private final TypeGuardian typeGuardian;
    private final Schedule schedule;
    private final UserModel user;
    private final List<Patient> patientList;
    private final LocalDateTime lastUpdate;

    public Guardian (Builder b){
        super(b.address, b.age, b.bloodType, b.dateOfBirth, b.dni, b.fullname, b.id, b.phoneNumber);

        this.user = b.user;
        this.typeGuardian = b.typeGuardian;
        this.schedule = b.schedule;
        this.patientList = List.copyOf( b.patientList);
        this.lastUpdate = b.lastUpdate;
    }

    // Un responsable puede ser registrado si:
    // Usuario activo.
    // Cumple con la edad requerida.
    // El responsable no sede el tope de pacientes a cargos.
    public static Guardian registerGuardian(PersonRegistrationData data,
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
                .withAddress(data.getAddress())
                .withAge(data.getAge())
                .withBloodType(data.getBloodType())
                .withDateOfBirth(data.getDateOfBirth())
                .withDni(data.getDni())
                .withFullName(data.getFullname())
                .withId(data.getId())
                .withPhoneNumber(data.getPhoneNumber())
                .withUser(user)
                .withTypeGuardian(typeGuardian)
                .withPatient(patientList)
                .build();
    }
    public static Guardian updateGuardianDataContact(PersonRegistrationData data, UserModel user ){
        ensureActiveUser(user);
        LocalDateTime lastUpdate =  LocalDateTime.now();
        return new Builder()
                .withAddress(data.getAddress())
                .withPhoneNumber(data.getPhoneNumber())
                .withLastUpdate(lastUpdate)
                .build();
    }

    public static Guardian updateGuardianSensitiveData(PersonRegistrationData data, UserModel user, TypeGuardian typeGuardian){
        ensureActiveUser(user);
        LocalDateTime lastUpdate =  LocalDateTime.now();
        return new Builder()

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

        public Guardian build () {
            return new Guardian(this);
        }
    }

}
