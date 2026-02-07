package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.domain.actor.vo.PhoneNumber;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberMapper {
    public static PhoneNumberDto toPhone(PhoneNumber p){
      return  new PhoneNumberDto(p.Value());
    }
    public static PhoneNumber fromDto(PhoneNumberDto dto){
        return new PhoneNumber(dto.phone());
    }
}
