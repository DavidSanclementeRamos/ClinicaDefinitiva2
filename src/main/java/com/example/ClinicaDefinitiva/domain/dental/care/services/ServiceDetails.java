package com.example.ClinicaDefinitiva.domain.dental.care.services;

import com.example.ClinicaDefinitiva.domain.dental.care.services.num.ServiceType;

public interface ServiceDetails {
    ServiceType serviceType(); // ej. "ORTHODONTIC"
    default boolean isEmpty() { return false; }
}
