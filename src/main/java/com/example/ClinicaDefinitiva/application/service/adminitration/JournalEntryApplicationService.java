package com.example.ClinicaDefinitiva.application.service.adminitration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.JournalEntryLineRequest;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.journalEntry.*;
import com.example.ClinicaDefinitiva.application.exceptions.Admistration.contavilidad.JournalEntryNotFoundException;
import com.example.ClinicaDefinitiva.application.mapper.Administration.JournalEntryMapper;
import com.example.ClinicaDefinitiva.application.portsInput.Administration.JournalEntryUseCase;
import com.example.ClinicaDefinitiva.domain.dental.care.services.valueObject.Price;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.JournalEntryId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.portsOutput.Administration.CompanyRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.Administration.JournalEntryRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.Administration.LedgerAccountRepository;
import com.example.ClinicaDefinitiva.domain.portsOutput.Administration.ThirdPartiesRepository;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JournalEntryApplicationService implements JournalEntryUseCase {
// complejo xd
    private final JournalEntryRepository journalEntryRepository;
    private final ThirdPartiesRepository thirdPartiesRepository;
    private final LedgerAccountRepository ledgerAccountRepository;
    private final CompanyRepository companyRepository;
    private final JournalEntryMapper mapper;


    public JournalEntryApplicationService(JournalEntryRepository journalEntryRepository, ThirdPartiesRepository thirdPartiesRepository, LedgerAccountRepository ledgerAccountRepository, CompanyRepository companyRepository, JournalEntryMapper mapper) {
        this.journalEntryRepository = journalEntryRepository;
        this.thirdPartiesRepository = thirdPartiesRepository;
        this.ledgerAccountRepository = ledgerAccountRepository;
        this.companyRepository = companyRepository;
        this.mapper = mapper;
    }

    @Override
    public JournalEntryResponse findJournalEntryById(String id) {
        JournalEntryId journalEntryId = JournalEntryId.fromString(id);
        JournalEntry journalEntry = journalEntryRepository.findById(journalEntryId)
                .orElseThrow(()-> new JournalEntryNotFoundException(""));

        return mapper.toResponse(journalEntry);
    }

    @Override
    public Page<JournalEntryListResponse> listJournalEntriesByCompany(String companyId) {
        return null;
    }

    @Override
    public Page<JournalEntryListResponse> listJournalEntriesByDateRange(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Page<JournalEntryListResponse> listPostedJournalEntries() {
        return null;
    }

    @Override
    public Page<BalanceReportResponse> GenerateBalanceReport(String companyId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public JournalEntryResponse registerJournalEntry(CreateJournalEntryRequest request) {
        List<JournalEntryLine> lines = request.lines().stream()
                .map(toJournalEntryLine())
                .collect(Collectors.toList());

        JournalEntry journalEntry = JournalEntry.registerJournalEntry(
                CompanyId.fromString(request.companyId()),
                request.date(),
                request.documentNumber(),
                request.description(),
                lines
        );

        return toResponse(journalEntry);
    }

    private JournalEntryLine toJournalEntryLine(JournalEntryLineRequest request) {
        LedgerAccountId accountId = LedgerAccountId.fromString(request.ledgerAccountId());
        ThirdPartiesId thirdPartiesId = request.thirdPartiesId() != null ?
                ThirdPartiesId.fromString(request.thirdPartiesId()) : null;
        Price amount = Price.of(request.amount(), request.currency());

        if (request.isDebit()) {
            return thirdPartiesId != null ?
                    JournalEntryLine.debitWithThirdParty(accountId, thirdPartiesId, request.description(), amount, request.documentReference()) :
                    JournalEntryLine.debit(accountId, request.description(), amount);
        } else {
            return thirdPartiesId != null ?
                    JournalEntryLine.creditWithThirdParty(accountId, thirdPartiesId, request.description(), amount, request.documentReference()) :
                    JournalEntryLine.credit(accountId, request.description(), amount);
        }
    }

    private JournalEntryResponse toResponse(JournalEntry entry) {
        Map<LedgerAccountId, LedgerAccount> accounts = entry.getLines().stream()
                .map(JournalEntryLine::getLedgerAccountId)
                .distinct()
                .map(id -> ledgerAccountRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(LedgerAccount::getId, account -> account));

        Map<ThirdPartiesId, ThirdParties> thirdParties = entry.getLines().stream()
                .map(JournalEntryLine::getThirdPartiesId)
                .filter(Objects::nonNull)
                .distinct()
                .map(id -> thirdPartiesRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ThirdParties::getPartiesId, tp -> tp));

        return mapper.toResponse(entry, accounts, thirdParties);

    }

    @Override
    public JournalEntryResponse updateJournalEntry(UpdateJournalEntryRequest request) {
        return null;
    }

    @Override
    public JournalEntryResponse addJournalEntryLine(String journalEntryId, JournalEntryLineRequest request) {
        return null;
    }

    @Override
    public JournalEntryResponse postJournalEntry(String id) {
        JournalEntryId journalEntryId = JournalEntryId.fromString(id);
        JournalEntry journalEntry = journalEntryRepository.findById(journalEntryId)
                .orElseThrow(()-> new JournalEntryNotFoundException(""));
        journalEntry.post();

        journalEntryRepository.save(journalEntry);

        return mapper.toResponse(journalEntry);
    }

    @Override
    public JournalEntryResponse reverseJournalEntry(String journalEntryId, ReverseJournalEntryRequest request) {
        return null;
    }
}
