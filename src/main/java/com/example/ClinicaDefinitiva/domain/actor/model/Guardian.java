package com.example.ClinicaDefinitiva.domain.actor.model;

import com.example.ClinicaDefinitiva.domain.actor.Enum.BloodType;
import com.example.ClinicaDefinitiva.domain.actor.valueObject.*;
import com.example.ClinicaDefinitiva.domain.errors.ContextoEntidad;
import com.example.ClinicaDefinitiva.domain.exceptions.user.exception.UserInactiveException;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserStatus;
import com.example.ClinicaDefinitiva.domain.schedule.model.Schedule;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Guardian {

    private  final GuardianId guardianId;
    private  Person person;
    private  TypeGuardian typeGuardian;
    private  Schedule schedule;
    private UserIdentity user;
    private  List<Patient> patientList;
    private  LocalDateTime lastUpdate;

    public Guardian (Builder b){

        this.user = b.user;
        this.typeGuardian = b.typeGuardian;
        this.schedule = b.schedule;
        this.patientList = List.copyOf( b.patientList);
        this.lastUpdate = b.lastUpdate;
        this.person = b.personData;
        this.guardianId = b.guardianId;
    }

    public Guardian(GuardianId guardianId) {
        this.guardianId = guardianId;
    }

    // Un responsable puede ser registrado si:
    // Usuario activo.
    // Cumple con la edad requerida.
    // El responsable no sede el tope de pacientes a cargos.
    public static Guardian registerGuardian(GuardianId id,
                                            Person data,
                                            UserIdentity user,
                                           // List<Patient> patientList,
                                            TypeGuardian typeGuardian){
        ensureActiveUser(user);
        if (data.getAge().isBetween(22,60)){
            throw new IllegalArgumentException("El responsable no cuenta con la edad requerida para hacerse cargo de un responsable");
        }
        /**
         * ESTA VALIDATION NO ES VALIDA EN REGISTRO, CAMBIAR
         * NO es coherente validar cuantas personas tiene a cargo
         * al momento de crear, tampoco es valido crear el mismo responsable nuevamente, cuando es reasignado a otro paciente**
        if(patientList.size() <= 6){
            throw new IllegalArgumentException("El responsable no puede tener mas de 6 pacientes ");
        }*/
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
                //.withPatient(patientList)
                .withDocumentoEPS(data.getDocumentoEPS())
                .build();
    }

   public void updateContactData(Person data,
                                 UserIdentity user) {

       ensureActiveUser(user);
       this.person= this.person.updateContact(data.getAddress(), data.getPhoneNumber());
       this.lastUpdate = LocalDateTime.now();
   }

    public void updateSensitiveData(Person data, UserIdentity user, TypeGuardian typeGuardian) {
        ensureActiveUser(user);

        if (!data.getAge().isBetween(18, 130)) {
            throw new GuardianMinimumAgeException(ContextoEntidad.GUARDIAN, "El acudiente debe tener al menos 18 años.");
        }

        this.person = this.person.updateSensitive(
                data.getAge(),
                data.getBloodType(),
                data.getDateOfBirth(),
                data.getDni(),
                data.getDocumentoEPS(),
                data.getFullname()
        );
        this.user = user;
        this.typeGuardian = typeGuardian;
        this.lastUpdate = LocalDateTime.now();
    }

    public void deactivateGuardian(GuardianId id, UserIdentity user, List<Patient> patientList) {

        if (user == null) throw new IllegalArgumentException("user no puede ser null");

        // Verificar que el id coincide con el del agregado (evita desactivar otro guardian)
        if (!id.equals(this.guardianId)) {
            throw new IllegalArgumentException("El guardianId proporcionado no coincide con este Guardian");
        }

        // Verificar pacientes asignados: no permitir desactivar si hay pacientes
        boolean hasAssignedPatients = (patientList != null && !patientList.isEmpty()) || (this.patientList != null && !this.patientList.isEmpty());
        if (hasAssignedPatients) {
            throw new IllegalStateException("No se puede desactivar: hay pacientes asignados");
        }

        // Asegurar que el usuario proporcionado sea el mismo que el asociado (opcional, pero recomendable)
        if (!user.equals(this.user)) {
            throw new IllegalArgumentException("El usuario proporcionado no coincide con el usuario asociado al Guardian");
        }

        // Realizar la desactivación in-place
        // Asumo que UserIdentity expone un método para cambiar el estado; ajusta según tu API.
        this.user.setStatus(UserStatus.of(UserStatus.Status.INACTIVE));



        ensureActiveUser(user);
        // Actualizar metadatos de auditoría
        this.lastUpdate = LocalDateTime.now();


    }
    private static void ensureActiveUser(UserIdentity user) {
        if (!user.isActive()) {
            throw new UserInactiveException(ContextoEntidad.PATIENT, "user=" + user);
        }
    }

    public GuardianId getGuardianId() {
        return guardianId;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public Person getPerson() {
        return person;
    }

    public UserIdentity getUser() {
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
        private UserIdentity user;
        private TypeGuardian typeGuardian;
        private Schedule schedule;
        private List<Patient> patientList;
        private LocalDateTime lastUpdate;
        private String documentoEPS;
        private GuardianId guardianId;
        private Person personData;
        private UserStatus userStatus;


        public Builder withAddress(Address a) { this.address = a; return this; }
        public Builder withAge(Age a) { this.age = a; return this; }
        public Builder withBloodType(BloodType b) { this.bloodType = b; return this; }
        public Builder withDateOfBirth(DateOfBirth d) { this.dateOfBirth = d; return this; }
        public Builder withDni(String d) { this.dni = d; return this; }
        public Builder withFullName(FullName f) { this.fullname = f; return this; }
        public Builder withId(Long id) { this.id = id; return this; }
        public Builder withPhoneNumber(PhoneNumber p) { this.phoneNumber = p; return this; }
        public Builder withUser(UserIdentity u){this.user = u; return this;}
        public Builder withTypeGuardian(TypeGuardian t){this.typeGuardian = t; return this;}
        public Builder withSchedule(Schedule s){this.schedule = s; return  this;}
        public Builder withPatient(Collection<Patient> p){this.patientList = new ArrayList<>(p); return this;}
        public Builder withLastUpdate(LocalDateTime l){this.lastUpdate = l; return this;}
        public Builder withDocumentoEPS(String d){this.documentoEPS = d; return this;}
        public Builder withPerson(Person p){this.personData = p; return this;}
        public Builder withGuardianId(GuardianId d){this.guardianId = d; return this;}
        public Builder withUserStatus(UserStatus s){this.userStatus = s; return  this;}
        public Guardian build () {
            return new Guardian(this);
        }
    }

}
