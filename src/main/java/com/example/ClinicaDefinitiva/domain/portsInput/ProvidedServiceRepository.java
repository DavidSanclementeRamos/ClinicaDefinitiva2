package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.dental.care.services.model.ProvidedService;

import java.util.Optional;

public interface ProvidedServiceRepository {
    Optional<ProvidedService> findById(Long id);
}
