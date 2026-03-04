package com.example.ClinicaDefinitiva.domain.billing.output;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {

    Optional<Invoice> findById(InvoiceId id);
    Page<Invoice> findAll(Pageable pageable);
    Invoice save(Invoice invoice);
    Invoice update(InvoiceId id, Invoice invoice);
    void deleteById(InvoiceId id);

    Optional<Invoice> findByNumber(String invoiceNumber);

    Page<Invoice> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Invoice> findByDentist(DentistId dentistId, Pageable pageable);
    Page<Invoice> findByPatient(PatientId patientId, Pageable pageable);

    Page<Invoice> findByStatus(InvoiceStatus.Status status, Pageable pageable);
}
