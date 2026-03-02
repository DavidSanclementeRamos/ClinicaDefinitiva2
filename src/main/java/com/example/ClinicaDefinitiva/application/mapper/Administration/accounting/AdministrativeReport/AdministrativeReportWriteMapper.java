package com.example.ClinicaDefinitiva.application.mapper.Administration.accounting.AdministrativeReport;

import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.CreateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.DocumentDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.IndicatorDto;
import com.example.ClinicaDefinitiva.application.dto.administration.contabilidad.administrativeReport.UpdateAdministrativeReportDto;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.AdministrativeReport;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Document;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Indicator;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Period;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class AdministrativeReportWriteMapper {
    public AdministrativeReport fromCreateDto(CreateAdministrativeReportDto dto){
        return AdministrativeReport.create(
                Name.of( dto.title()),
                Period.of( dto.period().star(),dto.period().end()),
                UserIdentityId.from(dto.createdBy())
        );
    }

    public void toUpdateDto(UpdateAdministrativeReportDto dto, AdministrativeReport report){
         report.updateInformation(
                Name.of(dto.title()),
                dto.notes()
        );
    }


    public Document toDocument(DocumentDto dto) {
    return Document.of(dto.name(), dto.url(), dto.type(), dto.size());
}
   
   public Indicator toIndicator(IndicatorDto dto  ){
        return Indicator.of(dto.name(), dto.value(), dto.unit());
   }
}
