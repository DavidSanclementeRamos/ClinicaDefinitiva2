package com.example.ClinicaDefinitiva.domain.billing.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ContractId;
import com.example.ClinicaDefinitiva.domain.billing.model.Rate;
import com.example.ClinicaDefinitiva.domain.billing.vo.RateId;
import com.example.ClinicaDefinitiva.domain.dentalService.vo.ServiceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RateRepository {
    Optional<Rate> findActiveRateForService(ServiceId serviceId, ContractId contractId);
    Page<Rate> findAll(Pageable pageable);
    Rate update(RateId id,Rate rate );
    Rate save(Rate rate);
    void deleteById(RateId id);

    Optional<Rate> findById(RateId id);

    Page<Rate> findCurrentlyValid(LocalDateTime now, Pageable pageable);

    Page<Rate> findByContract(ContractId contractId, Pageable pageable);

    Page<Rate> findByService(ServiceId serviceId, Pageable pageable);

    Page<Rate> findByPayerType(String payerType, Pageable pageable);
}
