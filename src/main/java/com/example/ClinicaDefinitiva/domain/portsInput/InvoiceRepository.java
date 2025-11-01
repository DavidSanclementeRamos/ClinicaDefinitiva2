package com.example.ClinicaDefinitiva.domain.portsInput;

import com.example.ClinicaDefinitiva.domain.billing.doiman.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.doiman.valueObject.InvoiceId;

import java.util.List;
import java.util.Optional;

public interface InvoiceRepository {

    Optional<Invoice> findById(InvoiceId id);
    List<Invoice> findAll();
    void save(Invoice invoice);
    Invoice update(InvoiceId id, Invoice invoice);
    void deleteById(InvoiceId id);
}
