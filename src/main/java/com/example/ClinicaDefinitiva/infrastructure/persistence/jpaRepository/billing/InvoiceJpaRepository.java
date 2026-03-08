package com.example.ClinicaDefinitiva.infrastructure.persistence.jpaRepository.billing;

import com.example.ClinicaDefinitiva.infrastructure.persistence.entity.billing.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceJpaRepository extends JpaRepository<InvoiceEntity, String> {}


