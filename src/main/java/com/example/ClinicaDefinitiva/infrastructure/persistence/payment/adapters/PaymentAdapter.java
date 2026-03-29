package com.example.ClinicaDefinitiva.infrastructure.persistence.payment.adapters;

import com.example.ClinicaDefinitiva.domain.billing.vo.InvoiceId;
import com.example.ClinicaDefinitiva.domain.payment.model.Payment;
import com.example.ClinicaDefinitiva.domain.payment.output.PaymentRepository;
import com.example.ClinicaDefinitiva.domain.payment.vo.PaymentId;
import com.example.ClinicaDefinitiva.infrastructure.persistence.billing.jpaRepository.InvoiceJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.entity.PaymentEntity;
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.jpaRepository.PaymentJpaRepository;
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.mapper.PaymentReadEntityMapper;
import com.example.ClinicaDefinitiva.infrastructure.persistence.payment.mapper.PaymentWriteEntityMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional
public class PaymentAdapter implements PaymentRepository {

    private final PaymentJpaRepository springDataRepository;
    private final InvoiceJpaRepository invoiceJpaRepository;
    private final PaymentReadEntityMapper readMapper;
    private final PaymentWriteEntityMapper writeMapper;

    public PaymentAdapter(
            PaymentJpaRepository springDataRepository,
            InvoiceJpaRepository invoiceJpaRepository,
            PaymentReadEntityMapper readMapper,
            PaymentWriteEntityMapper writeMapper) {
        this.springDataRepository = springDataRepository;
        this.invoiceJpaRepository = invoiceJpaRepository;
        this.readMapper = readMapper;
        this.writeMapper = writeMapper;
    }

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = writeMapper.toEntity(payment);
        // Establecer relación con Invoice
        if (payment.getInvoiceId() != null) {
            invoiceJpaRepository.findById(payment.getInvoiceId().getValue())
                .ifPresent(entity::setInvoice);
        }
        PaymentEntity saved = springDataRepository.save(entity);
        return readMapper.toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(PaymentId id) {
        return springDataRepository.findById(id.value())
            .map(readMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByInvoiceId(InvoiceId invoiceId) {
        return springDataRepository.findByInvoiceId(invoiceId.getValue())
            .stream()
            .map(readMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findAll() {
        return springDataRepository.findAll()
            .stream()
            .map(readMapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(PaymentId id) {
        springDataRepository.deleteById(id.value());
    }

    @Override
    public List<Payment> findConfirmedByInvoiceId(InvoiceId invoiceId) {
       return springDataRepository.findConfirmedPaymentsByInvoice(invoiceId.getValue())
                .stream()
            .map(readMapper::toDomain)
            .collect(Collectors.toList());
               
    }

    @Override
    public List<Payment> findByStatus(String statusEnum) {
        return springDataRepository.findByStatus(statusEnum)
                .stream()
            .map(readMapper::toDomain)
            .collect(Collectors.toList());
       }
}