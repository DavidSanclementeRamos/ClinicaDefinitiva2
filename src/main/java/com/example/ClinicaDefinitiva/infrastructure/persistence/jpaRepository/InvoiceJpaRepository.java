package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, String> {}


