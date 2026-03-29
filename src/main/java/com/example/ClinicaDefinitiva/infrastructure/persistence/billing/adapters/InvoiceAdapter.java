package com.example.ClinicaDefinitiva.infrastructure.persistence.billing.adapters;

import com.example.ClinicaDefinitiva.domain.actor.vo.DentistId;
import com.example.ClinicaDefinitiva.domain.actor.vo.PatientId;
import com.example.ClinicaDefinitiva.domain.billing.model.Invoice;
import com.example.ClinicaDefinitiva.domain.billing.output.InvoiceRepository;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceStatus;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.entity.InvoiceEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.DentistJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.actor.jpaRepository.PatientJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.jpaRepository.InvoiceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.administration.accounting.jpaRepository.ContractJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.invoice.InvoiceReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.mapper.invoice.InvoiceWriteEntityMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@Transactional
public class InvoiceAdapter implements InvoiceRepository {

    private final InvoiceJpaRepository invoiceJpaRepository;
    private final PatientJpaRepository patientJpaRepository;
    private final DentistJpaRepository dentistJpaRepository;
    private final ContractJpaRepository contractJpaRepository;
    private final InvoiceReadEntityMapper readMapper;
    private final InvoiceWriteEntityMapper writeMapper;

    public InvoiceAdapter(
            InvoiceJpaRepository invoiceJpaRepository,
            PatientJpaRepository patientJpaRepository,
            DentistJpaRepository dentistJpaRepository,
            ContractJpaRepository contractJpaRepository,
            InvoiceReadEntityMapper readMapper,
            InvoiceWriteEntityMapper writeMapper) {
        this.invoiceJpaRepository = invoiceJpaRepository;
        this.patientJpaRepository = patientJpaRepository;
        this.dentistJpaRepository = dentistJpaRepository;
        this.contractJpaRepository = contractJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findById(InvoiceId id) {
        if (id == null || id.getValue() == null) {
            return Optional.empty();
        }
        return invoiceJpaRepository.findById(id.getValue())
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findAll(Pageable pageable) {
        return invoiceJpaRepository.findAll(pageable)
                .map(readMapper::toDomain);
    }

    @Override
    public Invoice save(Invoice invoice) {
        if (invoice == null) return null;

        InvoiceEntity entity = writeMapper.toEntity(invoice);

        // Establecer relaciones
        if (invoice.getPatientId() != null && invoice.getPatientId().value() != null) {
            patientJpaRepository.findById(invoice.getPatientId().value())
                    .ifPresent(entity::setPatient);
        }

        if (invoice.getDentistId() != null && invoice.getDentistId().value() != null) {
            dentistJpaRepository.findById(invoice.getDentistId().value())
                    .ifPresent(entity::setDentist);
        }

        if (invoice.getContractId() != null && invoice.getContractId().getValue() != null) {
            contractJpaRepository.findById(invoice.getContractId().getValue())
                    .ifPresent(entity::setContract);
        }

        InvoiceEntity savedEntity = invoiceJpaRepository.save(entity);
        return readMapper.toDomain(savedEntity);
    }

    @Override
    public Invoice update(InvoiceId id, Invoice invoice) {
        if (id == null || id.getValue() == null || invoice == null) {
            return null;
        }

        if (!invoiceJpaRepository.existsById(id.getValue())) {
            return null;
        }

        return save(invoice); // save ya maneja insert/update
    }

    @Override
    public void deleteById(InvoiceId id) {
        if (id != null && id.getValue() != null) {
            invoiceJpaRepository.deleteById(id.getValue());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Invoice> findByNumber(String invoiceNumber) {
        if (invoiceNumber == null || invoiceNumber.isBlank()) {
            return Optional.empty();
        }
        return invoiceJpaRepository.findByInvoiceNumber(invoiceNumber)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return invoiceJpaRepository.findByDateRange(start, end, pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findByDentist(DentistId dentistId, Pageable pageable) {
        if (dentistId == null || dentistId.value() == null) {
            return Page.empty();
        }
        return invoiceJpaRepository.findByDentistId(dentistId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findByPatient(PatientId patientId, Pageable pageable) {
        if (patientId == null || patientId.value() == null) {
            return Page.empty();
        }
        return invoiceJpaRepository.findByPatientId(patientId.value(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Invoice> findByStatus(InvoiceStatus.Status status, Pageable pageable) {
        if (status == null) {
            return Page.empty();
        }
        return invoiceJpaRepository.findByStatus(status.name(), pageable)
                .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByServiceId(ServiceId serviceId) {
        if (serviceId == null || serviceId.getId() == null) {
            return false;
        }
        return invoiceJpaRepository.existsByServiceId(serviceId.getId());
    }
}
