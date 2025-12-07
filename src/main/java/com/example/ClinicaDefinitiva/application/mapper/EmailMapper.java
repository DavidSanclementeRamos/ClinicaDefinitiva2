package com.example.ClinicaDefinitiva.application.mapper;

import com.example.ClinicaDefinitiva.application.dto.EmailDto;
import com.example.ClinicaDefinitiva.domain.Email;

public class EmailMapper {
    public static EmailDto toEmail(Email a){return new EmailDto(a.value());}
    public static Email fromDto(EmailDto dto){return new Email(dto.email());}
}
