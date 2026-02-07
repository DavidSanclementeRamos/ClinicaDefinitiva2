package com.example.ClinicaDefinitiva.domain.administration.accounting.output;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import org.springframework.data.domain.Page;

import java.util.Optional;

/**
 * Repositorio para ThirdParties
 */
public interface ThirdPartiesRepository {
    ThirdParties save(ThirdParties thirdParties);
    Optional<ThirdParties> findById(ThirdPartiesId id);
    Optional<ThirdParties> findByDocumentNumber(String documentNumber);
    Page<ThirdParties> findByCompanyId(CompanyId companyId);
    Page<ThirdParties> findByType(TypeThirdParties type);
    Page<ThirdParties> findActiveByType(TypeThirdParties type);
    Page<ThirdParties> findAll();
    boolean existsByDocumentNumber(String documentNumber);
}
