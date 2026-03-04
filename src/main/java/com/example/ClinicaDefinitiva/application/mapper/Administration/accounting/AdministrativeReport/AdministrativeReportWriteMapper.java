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
  
    public Name toName(CreateAdministrativeReportDto dto) {
        return Name.of(dto.title());
    }

    public Period toPeriod(CreateAdministrativeReportDto dto) {
        return Period.of(dto.period().star(), dto.period().end());
    }

    public UserIdentityId toUserIdentityId(CreateAdministrativeReportDto dto) {
        return UserIdentityId.from(dto.createdBy());
    }



     // Actualización: el mapper devuelve objetos de dominio, no invoca métodos del agregado
    public Name toName(UpdateAdministrativeReportDto dto) {
        return Name.of(dto.title());
    }

    public String toNotes(UpdateAdministrativeReportDto dto) {
        return dto.notes();
    }


    public Document toDocument(DocumentDto dto) {
    return Document.of(dto.name(), dto.url(), dto.type(), dto.size());
}
   
   public Indicator toIndicator(IndicatorDto dto  ){
        return Indicator.of(dto.name(), dto.value(), dto.unit());
   }
}
