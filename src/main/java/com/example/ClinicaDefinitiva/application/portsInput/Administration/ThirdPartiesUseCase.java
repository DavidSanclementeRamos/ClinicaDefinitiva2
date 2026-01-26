package com.example.ClinicaDefinitiva.application.portsInput.Administration;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.thirdParties.*;
import org.springframework.data.domain.Page;

public interface ThirdPartiesUseCase {

    ThirdPartiesResponse registerThirdParty(CreateThirdPartiesRequest request);
    ThirdPartiesResponse updateThirdPartyContact(String thirdPartiesId, UpdateThirdPartiesContactRequest request);
    ThirdPartiesResponse activateThirdParty(String thirdPartiesId);
    ThirdPartiesResponse inactivateThirdParty(String thirdPartiesId, InactivateThirdPartiesRequest request);
    ThirdPartiesResponse findThirdPartyById(String thirdPartiesId);
    ThirdPartiesResponse findThirdPartyByDocument(String documentNumber);
    Page<ThirdPartiesListResponse> listThirdPartiesByCompany(String companyId);
    Page<ThirdPartiesListResponse> listThirdPartiesByType(String type);
    Page<ThirdPartiesListResponse> listActiveThirdPartiesByType(String type);


}
