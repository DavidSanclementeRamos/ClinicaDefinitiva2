
package com.example.ClinicaDefinitiva.domain.adminitration.accounnting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.ContractStatus;
import com.example.ClinicaDefinitiva.domain.administration.accounting.model.Contract;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.CompanyId;
import com.example.ClinicaDefinitiva.domain.vo.Name;
import com.example.ClinicaDefinitiva.domain.administration.accounting.vo.ThirdPartiesId;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ContractTest {

    @Test
    void shouldRegisterContractWithDefaults() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato de Servicios"),
                "Cobertura médica",
                "Interno",
                LocalDate.now().plusDays(60),
                "Salud",
                new BigDecimal(0.85)
        );

        assertEquals("Contrato de Servicios", contract.getName().getValue());
        assertEquals("SALUD", contract.getCoverageType());
        assertEquals(ContractStatus.ACTIVE, contract.getStatus());
        assertTrue(contract.isActiveAndValid());
    }

    @Test
    void shouldUpdateInformation() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato Inicial"),
                "Cobertura básica",
                "Externo",
                LocalDate.now().plusDays(30),
                "Salud",
                new BigDecimal(0.75)
        );

        contract.updateInformation(Name.of("Contrato Actualizado"), "Cobertura extendida", "Interno", "Dental");

        assertEquals("Contrato Actualizado", contract.getName().getValue());
        assertEquals("Cobertura extendida", contract.getDescription());
        assertEquals("INTERNO", contract.getOrigin().toUpperCase());
        assertEquals("DENTAL", contract.getCoverageType());
    }

    @Test
    void shouldExtendContract() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato Extensible"),
                "Cobertura",
                "Interno",
                LocalDate.now().plusDays(30),
                "Salud",
                new BigDecimal(0.85)
        );

        LocalDate newEndDate = LocalDate.now().plusDays(90);
        contract.extendContract(newEndDate);

        assertEquals(newEndDate, contract.getEndDate());
    }

    @Test
    void shouldSuspendAndReactivateContract() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato Suspendible"),
                "Cobertura",
                "Interno",
                LocalDate.now().plusDays(30),
                "Salud",
                new BigDecimal(0.85)
        );

        contract.suspend("Falta de pago");
        assertEquals(ContractStatus.SUSPENDED, contract.getStatus());

        contract.reactivate();
        assertEquals(ContractStatus.ACTIVE, contract.getStatus());
    }

    @Test
    void shouldTerminateContractWithReason() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato Terminable"),
                "Cobertura",
                "Interno",
                LocalDate.now().plusDays(30),
                "Salud",
                new BigDecimal(0.85)
        );

        contract.terminate("Incumplimiento de cláusulas");
        assertEquals(ContractStatus.TERMINATED, contract.getStatus());
    }

    @Test
    void shouldDetectExpiredAtSpecificDate() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato Expirable"),
                "Cobertura",
                "Interno",
                LocalDate.now().plusDays(5),
                "Salud",
                new BigDecimal(0.85)
        );

        LocalDateTime afterExpiration = LocalDateTime.now().plusDays(10);
        assertTrue(contract.isExpiredAt(afterExpiration));

        LocalDateTime beforeExpiration = LocalDateTime.now().plusDays(2);
        assertFalse(contract.isExpiredAt(beforeExpiration));
    }

    @Test
    void shouldDetectNearExpiration() {
        Contract contract = Contract.registerContract(
                CompanyId.of(1L),
                ThirdPartiesId.of(2L),
                Name.of("Contrato Próximo a Vencer"),
                "Cobertura",
                "Interno",
                LocalDate.now().plusDays(10),
                "Salud",
                new BigDecimal(0.85)
        );

        assertTrue(contract.isNearExpiration());
        assertTrue(contract.getDaysRemaining() <= 30);
    }

   @Test
void shouldReturnZeroDaysRemainingWhenExpired() {
    Contract contract = Contract.builder()
            .withCompanyId(CompanyId.of(1L))
            .withThirdPartiesId(ThirdPartiesId.of(2L))
            .withName(Name.of("Contrato Vencido"))
            .withStartDate(LocalDate.now().minusDays(10)) // inicio hace 10 días
            .withEndDate(LocalDate.now().minusDays(1))    // fin ayer
            .withCoverageType("Salud")
            .withCoverageRate(new BigDecimal(0.85))
            .build();

    assertTrue(contract.isExpired());
    assertEquals(0, contract.getDaysRemaining());
}

}
