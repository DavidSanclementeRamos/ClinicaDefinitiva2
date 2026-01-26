package com.example.ClinicaDefinitiva.application.portsInput.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.CreateOpeningBalanceRequest;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.OpeningBalanceListResponse;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.openingBalance.OpeningBalanceResponse;
import org.springframework.data.domain.Page;

public interface OpeningBalanceUseCase {
    // Registrar un nuevo saldo inicial
    OpeningBalanceResponse registerOpeningBalance(CreateOpeningBalanceRequest request);

    // Buscar un saldo inicial por su ID
    OpeningBalanceResponse findOpeningBalanceById(String openingBalanceId);

    // Listar saldos iniciales de una compañía
    Page<OpeningBalanceListResponse> listOpeningBalancesByCompany(String companyId);

    // Listar saldos iniciales de una cuenta
    Page<OpeningBalanceListResponse> listOpeningBalancesByAccount(String accountId);


}
