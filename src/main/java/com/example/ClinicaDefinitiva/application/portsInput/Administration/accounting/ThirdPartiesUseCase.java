package com.example.ClinicaDefinitiva.application.portsInput.Administration.accounting;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.*;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.RolId;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThirdPartiesUseCase {

    ReadThirdPartyDto findById(ThirdPartiesId id, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageThirdPartyDto> findAll(Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageThirdPartyDto> findByType(String type, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadThirdPartyDto findByDocumentNumber(String documentNumber, UserIdentityId requesterId, RolId requesterRolId);

    Page<PageThirdPartyDto> findByCompany(CompanyId companyId, Pageable pageable, UserIdentityId requesterId, RolId requesterRolId);

    ReadThirdPartyDto createThirdParty(CreateThirdPartyDto dto, UserIdentityId requesterId, RolId requesterRolId);

    ReadThirdPartyDto updateContactInformation(ThirdPartiesId id, UpdateThirdPartyDto dto, UserIdentityId requesterId, RolId requesterRolId);

    void activate(ThirdPartiesId id, UserIdentityId requesterId, RolId requesterRolId);

    void inactivate(ThirdPartiesId id, String reason, UserIdentityId requesterId, RolId requesterRolId);
}
