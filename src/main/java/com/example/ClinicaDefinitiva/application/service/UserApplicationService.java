package com.example.ClinicaDefinitiva.application.service;

import com.example.ClinicaDefinitiva.domain.exceptionsDomain.AggregateBusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.portsOutput.UserRepository;
import com.example.ClinicaDefinitiva.domain.service.UserDeactivationPolicy;
import com.example.ClinicaDefinitiva.domain.userAccess.model.UserIdentity;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.util.Outcome;

import java.time.Instant;

public class UserApplicationService {
    private final UserRepository userRepository;
    private final UserDeactivationPolicy userDeactivationPolicy;

    public UserApplicationService(UserRepository userRepository,
                                  UserDeactivationPolicy userDeactivationPolicy) {
        this.userRepository = userRepository;
        this.userDeactivationPolicy = userDeactivationPolicy;
    }

    public void deactivateUser(UserId userId) {
        UserIdentity user = userRepository.findIdentity(userId);

        Outcome<Void> outcome = userDeactivationPolicy.validate(user);

        if (outcome.isFailure()) {
            // Lanzamos una excepción compuesta con todos los errores acumulados
            throw new AggregateBusinessRuleViolationException(outcome.getDetalles());
        }
       // user domainUser = userRepository.findById(userId);
        String reason = "Deactivated by system policy";
        user.deactivate(userDeactivationPolicy, Instant.now(), reason);
        userRepository.save(user);
    }
}
