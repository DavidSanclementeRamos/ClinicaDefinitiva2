
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.LedgerAccount;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.Name;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.BusinessRuleViolationException;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.DomainAggregateException;
import org.junit.jupiter.api.Test;
import java.util.Currency;
import static org.junit.jupiter.api.Assertions.*;

class LedgerAccountTest {

    @Test
    void shouldRegisterLedgerAccountSuccessfully() {
        LedgerAccount account = LedgerAccount.registerLedgerAccount(
                CompanyId.of(1L),
                "1105",
                Name.of("Caja"),
                NaturalezaCuenta.DEBITO,
                true,
                false
        );

        assertEquals("1105", account.getCode());
        assertEquals("Caja", account.getName().getValue());
        assertTrue(account.isActive());
        assertTrue(account.isAssetAccount());
        assertEquals(3, account.getAccountLevel()); // 4 dígitos = nivel 3
    }

    @Test
    void shouldUpdateAccountInformationWhenActive() {
        LedgerAccount account = LedgerAccount.registerLedgerAccount(
                CompanyId.of(1L),
                "2205",
                Name.of("Proveedores"),
                NaturalezaCuenta.CREDITO,
                false,
                true
        );

        account.updateAccountInformation(Name.of("Proveedores Nacionales"), true, true);

        assertEquals("Proveedores Nacionales", account.getName().getValue());
        assertTrue(account.isRequiresThirdParty());
        assertTrue(account.isRequiresDocument());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingInactiveAccount() {
        LedgerAccount account = LedgerAccount.registerLedgerAccount(
                CompanyId.of(1L),
                "2205",
                Name.of("Proveedores"),
                NaturalezaCuenta.CREDITO,
                false,
                true
        );

        account.inactivate("Cuenta cerrada");

        assertThrows(BusinessRuleViolationException.class, () ->
                account.updateAccountInformation(Name.of("Proveedores Nuevos"), true, false)
        );
    }

    @Test
    void shouldActivateAndInactivateAccount() {
        LedgerAccount account = LedgerAccount.registerLedgerAccount(
                CompanyId.of(1L),
                "3100",
                Name.of("Capital"),
                NaturalezaCuenta.CREDITO,
                false,
                false
        );

        account.inactivate("Reestructuración");
        assertFalse(account.isActive());

        account.activate();
        assertTrue(account.isActive());
    }

    @Test
    void shouldThrowExceptionForInvalidCodeFormat() {
        assertThrows(DomainAggregateException.class, () ->
                LedgerAccount.registerLedgerAccount(
                        CompanyId.of(1L),
                        "ABC123",
                        Name.of("Cuenta Inválida"),
                        NaturalezaCuenta.DEBITO,
                        false,
                        false
                )
        );
    }

    @Test
    void shouldReturnParentCodeCorrectly() {
        LedgerAccount account = LedgerAccount.registerLedgerAccount(
                CompanyId.of(1L),
                "110501",
                Name.of("Caja General"),
                NaturalezaCuenta.DEBITO,
                false,
                false
        );

        assertEquals("1105", account.getParentCode()); // nivel 4 → padre nivel 3
    }

    @Test
    void shouldReturnFullDescription() {
        LedgerAccount account = LedgerAccount.registerLedgerAccount(
                CompanyId.of(1L),
                "4100",
                Name.of("Ingresos Operacionales"),
                NaturalezaCuenta.CREDITO,
                false,
                false
        );

        String description = account.getFullDescription();
        assertTrue(description.contains("4100"));
        assertTrue(description.contains("Ingresos Operacionales"));
        assertTrue(description.contains("CREDITO"));
    }
}

