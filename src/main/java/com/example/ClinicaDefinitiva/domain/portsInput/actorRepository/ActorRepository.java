package com.example.ClinicaDefinitiva.domain.portsInput.actorRepository;

import com.example.ClinicaDefinitiva.domain.actor.valueObject.PatientId;
import com.example.ClinicaDefinitiva.domain.util.Actor;
import java.util.ArrayList;
import java.util.List;

public class ActorRepository {
    private final PatientRepository patientRepository;
    private final DentistRepository dentistRepository;
    private final GuardianRepository guardianRepository;
    private final ReceptionRepository receptionRepository;

    public ActorRepository(PatientRepository patientRepository, DentistRepository dentistRepository, GuardianRepository guardianRepository, ReceptionRepository receptionRepository) {
        this.patientRepository = patientRepository;
        this.dentistRepository = dentistRepository;
        this.guardianRepository = guardianRepository;
        this.receptionRepository = receptionRepository;
    }

    public List<Actor> findByUserId(String userId) {
        List<Actor> actors = new ArrayList<>();
        patientRepository.findById(PatientId.fromString(userId)).ifPresent(actors::add);
        dentistRepository.findByUserId(userId).ifPresent(actors::add);
        // ...
        return actors;
    }
}
