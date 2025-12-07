package com.example.ClinicaDefinitiva.application.service.adminitration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.ledgerAccount.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.LedgerAccountNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.LedgerAccountMapper;
import com.example.ClinicaDefinitiva.application.mapper.Administration.NameMapper;
import com.example.ClinicaDefinitiva.application.usecase.Administration.LedgerAccountUseCase;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.contable.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.portsInput.Administration.LedgerAccountRepository;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.stream.Collectors;

public class LedgerAccountApplicationService implements LedgerAccountUseCase {
    private final LedgerAccountMapper mapper;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;

    public LedgerAccountApplicationService(LedgerAccountMapper mapper, LedgerAccountRepository ledgerAccountRepository, CompanyRepository companyRepository) {
        this.mapper = mapper;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public LedgerAccountResponse registerLedgerAccount(CreateLedgerAccountRequest request) {
        CompanyId companyId = CompanyId.fromString(request.companyId());
        Page<LedgerAccount> ledgerAccountPage = ledgerAccountRepository.findByCompanyId(companyId);
        if(ledgerAccountPage.isEmpty()){
            throw new LedgerAccountNotFoundException("");
        }

        LedgerAccount ledgerAccount = LedgerAccount.registerLedgerAccount(
                companyId,
                request.code(),
                NameMapper.fromDto(request.name()),
                NaturalezaCuenta.valueOf(request.nature()),
                request.requiresThirdParty(),
                request.requiresThirdParty()
        );
           ledgerAccountRepository.save(ledgerAccount);
        return mapper.toResponse(ledgerAccount);
    }

    @Override
    public LedgerAccountResponse updateLedgerAccount(String accountId, UpdateLedgerAccountRequest request) {
        LedgerAccountId ledgerAccountId = LedgerAccountId.fromString(accountId);
        LedgerAccount ledgerAccount = ledgerAccountRepository.findById(ledgerAccountId)
                .orElseThrow(()-> new LedgerAccountNotFoundException(""));

        ledgerAccount.updateAccountInformation(
                NameMapper.fromDto(request.name()),
                request.requiresThirdParty(),
                request.requiresDocument()
        );
        ledgerAccountRepository.save(ledgerAccount);

        return mapper.toResponse(ledgerAccount);
    }

    @Override
    public LedgerAccountResponse activateLedgerAccount(String accountId) {
        LedgerAccountId ledgerAccountId = LedgerAccountId.fromString(accountId);
        LedgerAccount ledgerAccount = ledgerAccountRepository.findById(ledgerAccountId)
                .orElseThrow(()-> new LedgerAccountNotFoundException(""));

        ledgerAccount.activate();

        ledgerAccountRepository.save(ledgerAccount);

        return mapper.toResponse(ledgerAccount);    }

    @Override
    public LedgerAccountResponse inactivateLedgerAccount(String accountId, InactivateLedgerAccountRequest request) {
        LedgerAccountId ledgerAccountId = LedgerAccountId.fromString(accountId);
        LedgerAccount ledgerAccount = ledgerAccountRepository.findById(ledgerAccountId)
                .orElseThrow(()-> new LedgerAccountNotFoundException(""));

        ledgerAccount.inactivate(request.reason());

        ledgerAccountRepository.save(ledgerAccount);

        return mapper.toResponse(ledgerAccount);
    }

    @Override
    public LedgerAccountResponse findLedgerAccountByI(String accountId) {
        Optional<LedgerAccount> ledgerAccount = ledgerAccountRepository
                .findChildAccounts(accountId).stream().findFirst();


        return mapper.toResponse(ledgerAccount);
    }

    @Override
    public LedgerAccountResponse findLedgerAccountByCode(String code) {
        return null;
    }

    @Override
    public Page<LedgerAccountListResponse> listLedgerAccountsByCompany(String companyId) {
        return null;
    }

    @Override
    public Page<LedgerAccountListResponse> listActiveLedgerAccounts() {
        return null;
    }

    @Override
    public Page<LedgerAccountListResponse> listLedgerAccountsByLevelU(int level) {
        return null;
    }
}
