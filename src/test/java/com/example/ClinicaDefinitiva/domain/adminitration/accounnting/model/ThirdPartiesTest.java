
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.TypeThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.ThirdParties;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import com.example.ClinicaDefinitiva.domain.vo.Address;
import com.example.ClinicaDefinitiva.domain.vo.Email;
import com.example.ClinicaDefinitiva.domain.vo.PhoneNumber;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ThirdPartiesTest {

    @Test
    void shouldRegisterThirdPartySuccessfully() {
        ThirdParties thirdParty = ThirdParties.registerThirdParties(
                CompanyId.of(1L),
                Name.of("Proveedor XYZ"),
                "NIT",
                "123456789",
                TypeThirdParties.PROVEEDOR,
                Address.of("Calle 123","cali","xd","colombia","3555789"),
                PhoneNumber.of("3001234567"),
                Email.of("proveedor@xyz.com").getValue().get()
        );

        assertEquals("Proveedor XYZ", thirdParty.getName().getValue());
        assertEquals("NIT", thirdParty.getTypeDocument());
        assertEquals("123456789", thirdParty.getDocumentNumber());
        assertTrue(thirdParty.isSupplier());
        assertTrue(thirdParty.isActive());
    }

    @Test
    void shouldUpdateContactInformationWhenActive() {
        ThirdParties thirdParty = ThirdParties.registerThirdParties(
                CompanyId.of(1L),
                Name.of("Cliente ABC"),
                "CC",
                "987654321",
                TypeThirdParties.CLIENTE,
                Address.of("Carrera 45","cali","xd","colombia","3555789"),
                PhoneNumber.of("3109876543"),
                Email.of("cliente@abc.com").getValue().get()
        );

        thirdParty.updateContactInformation(
                Name.of("Cliente Actualizado"),
                Address.of("Nueva dirección","cali","xd","colombia","3555789"),
                PhoneNumber.of("3111111111"),
                Email.of("nuevo@abc.com").getValue().get()
        );

        assertEquals("Cliente Actualizado", thirdParty.getName().getValue());
        assertEquals("Nueva dirección", thirdParty.getAddress().Street());
        assertEquals("3111111111", thirdParty.getPhoneNumber().Value());
        assertEquals("nuevo@abc.com", thirdParty.getEmail().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingInactiveThirdParty() {
        ThirdParties thirdParty = ThirdParties.registerThirdParties(
                CompanyId.of(1L),
                Name.of("Empleado DEF"),
                "CC",
                "55555555",
                TypeThirdParties.EMPLEADO,
                Address.of("Calle 50","cali","xd","colombia","3555789"),
                PhoneNumber.of("3205555555"),
                Email.of("empleado@def.com").getValue().get()
        );

        thirdParty.inactivate("Despido");

        assertThrows(BusinessRuleViolationException.class, () ->
                thirdParty.updateContactInformation(
                        Name.of("Empleado Nuevo"),
                        Address.of("Otra dirección","cali","xd","colombia","3555789"),
                        PhoneNumber.of("3200000000"),
                        Email.of("nuevo@def.com").getValue().get()
                )
        );
    }

    @Test
    void shouldActivateAndInactivateThirdParty() {
        ThirdParties thirdParty = ThirdParties.registerThirdParties(
                CompanyId.of(1L),
                Name.of("Proveedor GHI"),
                "NIT",
                "11111111",
                TypeThirdParties.PROVEEDOR,
                Address.of("Calle 60","cali","xd","colombia","3555789"),
                PhoneNumber.of("3001111111"),
                Email.of("proveedor@ghi.com").getValue().get()
        );

        thirdParty.inactivate("Suspensión temporal");
        assertFalse(thirdParty.isActive());

        thirdParty.activate();
        assertTrue(thirdParty.isActive());
    }

    @Test
    void shouldThrowExceptionForInvalidDocumentLength() {
        assertThrows(BusinessRuleViolationException.class, () ->
                ThirdParties.registerThirdParties(
                        CompanyId.of(1L),
                        Name.of("Proveedor Inválido"),
                        "NIT",
                        "12", // demasiado corto
                        TypeThirdParties.PROVEEDOR,
                        Address.of("Calle 70","cali","xd","colombia","3555789"),
                        PhoneNumber.of("3002222222"),
                        Email.of("invalido@ghi.com").getValue().get()
                )
        );
    }

    @Test
    void shouldThrowExceptionForMissingDocumentType() {
        assertThrows(DomainAggregateException.class, () ->
                ThirdParties.registerThirdParties(
                        CompanyId.of(1L),
                        Name.of("Cliente Sin Tipo"),
                        null,
                        "123456789",
                        TypeThirdParties.CLIENTE,
                        Address.of("Calle 80","cali","xd","colombia","3555789"),
                        PhoneNumber.of("3003333333"),
                        Email.of("cliente@sin.com").getValue().get()
                )
        );
    }

    @Test
    void shouldThrowExceptionForMissingDocumentNumber() {
        assertThrows(DomainAggregateException.class, () ->
                ThirdParties.registerThirdParties(
                        CompanyId.of(1L),
                        Name.of("Cliente Sin Documento"),
                        "CC",
                        null,
                        TypeThirdParties.CLIENTE,
                        Address.of("Calle 90","cali","xd","colombia","3555789"),
                        PhoneNumber.of("3004444444"),
                        Email.of("cliente@sin.com").getValue().get()
                )
        );
    }

    @Test
    void shouldThrowExceptionForMissingTypeThirdParty() {
        assertThrows(DomainAggregateException.class, () ->
                ThirdParties.registerThirdParties(
                        CompanyId.of(1L),
                        Name.of("Cliente Sin Tipo"),
                        "CC",
                        "123456789",
                        null,
                        Address.of("Calle 100","cali","xd","colombia","3555789"),
                        PhoneNumber.of("3005555555"),
                        Email.of("cliente@sin.com").getValue().get()
                )
        );
    }
}

