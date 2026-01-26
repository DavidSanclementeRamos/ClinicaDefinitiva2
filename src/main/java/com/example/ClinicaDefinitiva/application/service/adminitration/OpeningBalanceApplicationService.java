package com.example.ClinicaDefinitiva.application.service.adminitration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.CreateOpeningBalanceRequest;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.OpeningBalanceListResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.OpeningBalanceResponse;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.OpeningBalanceUseCase;
import org.springframework.data.domain.Page;

public class OpeningBalanceApplicationService implements OpeningBalanceUseCase {
    @Override
    public OpeningBalanceResponse registerOpeningBalance(CreateOpeningBalanceRequest request) {
        return null;
    }

    @Override
    public OpeningBalanceResponse findOpeningBalanceById(String openingBalanceId) {
        return null;
    }

    @Override
    public Page<OpeningBalanceListResponse> listOpeningBalancesByCompany(String companyId) {
        return null;
    }

    @Override
    public Page<OpeningBalanceListResponse> listOpeningBalancesByAccount(String accountId) {
        return null;
    }
}
