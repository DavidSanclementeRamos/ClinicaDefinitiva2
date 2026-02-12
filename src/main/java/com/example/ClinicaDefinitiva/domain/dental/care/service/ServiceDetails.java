package com.example.ClinicaDefinitiva.domain.dental.care.service;

import com.example.ClinicaDefinitiva.domain.dental.care.service.num.ServiceType;

public interface ServiceDetails {
    ServiceType serviceType(); // ej. "ORTHODONTIC"
    default boolean isEmpty() { return false; }
}
