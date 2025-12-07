package com.example.ClinicaDefinitiva.domain.administration.accounting.model;

import com.example.ClinicaDefinitiva.domain.administration.accounting.enu.NaturalezaCuenta;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.CompanyId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.LedgerAccountId;
import com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject.Name;
import java.util.regex.Pattern;

/**
 * Representa una cuenta del plan accounting (PUC - Plan Único de Cuentas).
 * Esta clase es vital ya que el plan de cuentas se persiste mediante JSON.
 * Define la estructura accounting y las reglas para el registro de transacciones.
 */
public final class LedgerAccount {

    private static final Pattern ACCOUNT_CODE_PATTERN = Pattern.compile("^[0-9]{1,8}$");
    private static final int MIN_CODE_LENGTH = 1;
    private static final int MAX_CODE_LENGTH = 8;

    private LedgerAccountId id;
    private CompanyId companyId;
    private String code;
    private Name name;
    private NaturalezaCuenta nature;
    private boolean requiresThirdParty;
    private boolean requiresDocument;
    private boolean active;

    private LedgerAccount(
            LedgerAccountId id,
            CompanyId companyId,
            String code,
            Name name,
            NaturalezaCuenta nature,
            boolean requiresThirdParty,
            boolean requiresDocument,
            boolean active) {

        validateMandatoryFields(code, nature);
        validateAccountCode(code);

        this.id = id;
        this.companyId = companyId;
        this.code = code.trim();
        this.name = name;
        this.nature = nature;
        this.requiresThirdParty = requiresThirdParty;
        this.requiresDocument = requiresDocument;
        this.active = active;
    }

    /**
     * Factory method para registrar una nueva cuenta accounting.
     */
    public static LedgerAccount registerLedgerAccount(
            CompanyId companyId,
            String code,
            Name name,
            NaturalezaCuenta nature,
            boolean requiresThirdParty,
            boolean requiresDocument) {

        return new LedgerAccount(
                null,
                companyId,
                code,
                name,
                nature,
                requiresThirdParty,
                requiresDocument,
                true
        );
    }

    /**
     * Actualiza la información de la cuenta accounting.
     * Solo permite actualización si la cuenta está activa.
     */
    public void updateAccountInformation(
            Name name,
            boolean requiresThirdParty,
            boolean requiresDocument) {

        ensureActive();

        this.name = name;
        this.requiresThirdParty = requiresThirdParty;
        this.requiresDocument = requiresDocument;
    }

    /**
     * Activa la cuenta para permitir transacciones.
     */
    public void activate() {
        if (this.active) {
            throw new InvalidLedgerAccountException("La cuenta ya está activa");
        }
        this.active = true;
    }

    /**
     * Inactiva la cuenta. Las cuentas inactivas no pueden recibir movimientos.
     */
    public void inactivate(String reason) {
        if (!this.active) {
            throw new InvalidLedgerAccountException("La cuenta ya está inactiva");
        }
        if (reason == null || reason.isBlank()) {
            throw new InvalidLedgerAccountException("Se requiere una razón para inactivar la cuenta");
        }
        this.active = false;
    }


    /**
     * Obtiene el nivel de la cuenta en la jerarquía del plan accounting.
     * Nivel 1: 1 dígito (Clase)
     * Nivel 2: 2 dígitos (Grupo)
     * Nivel 3: 4 dígitos (Cuenta)
     * Nivel 4: 6 dígitos (Subcuenta)
     * Nivel 5: 8 dígitos (Auxiliar)
     */
    public int getAccountLevel() {
        int length = this.code.length();
        if (length == 1) return 1;
        if (length == 2) return 2;
        if (length == 4) return 3;
        if (length == 6) return 4;
        if (length == 8) return 5;
        return 0;
    }

    /**
     * Verifica si es una cuenta de activo.
     */
    public boolean isAssetAccount() {
        return this.code.startsWith("1");
    }

    /**
     * Verifica si es una cuenta de pasivo.
     */
    public boolean isLiabilityAccount() {
        return this.code.startsWith("2");
    }

    /**
     * Verifica si es una cuenta de patrimonio.
     */
    public boolean isEquityAccount() {
        return this.code.startsWith("3");
    }

    /**
     * Verifica si es una cuenta de ingreso.
     */
    public boolean isIncomeAccount() {
        return this.code.startsWith("4");
    }

    /**
     * Verifica si es una cuenta de gasto.
     */
    public boolean isExpenseAccount() {
        return this.code.startsWith("5");
    }

    /**
     * Verifica si es una cuenta de costos.
     */
    public boolean isCostAccount() {
        return this.code.startsWith("6");
    }

    /**
     * Verifica si es una cuenta de costo de ventas.
     */
    public boolean isCostOfSalesAccount() {
        return this.code.startsWith("7");
    }


    /**
     * Valida si se puede registrar un movimiento según las reglas de la cuenta.
     */
    public void validateMovementRequirements(boolean hasThirdParty, boolean hasDocument) {
        if (this.requiresThirdParty && !hasThirdParty) {
            throw new InvalidLedgerAccountException(
                    String.format("La cuenta %s - %s requiere un tercero", this.code, this.name)
            );
        }
        if (this.requiresDocument && !hasDocument) {
            throw new InvalidLedgerAccountException(
                    String.format("La cuenta %s - %s requiere un documento", this.code, this.name)
            );
        }
    }

    /**
     * Obtiene el código padre de esta cuenta en la jerarquía.
     */
    public String getParentCode() {
        if (this.code.length() <= 1) {
            return null; // Cuenta de nivel 1 no tiene padre
        }

        if (this.code.length() == 2) return this.code.substring(0, 1);
        if (this.code.length() == 4) return this.code.substring(0, 2);
        if (this.code.length() == 6) return this.code.substring(0, 4);
        if (this.code.length() == 8) return this.code.substring(0, 6);

        return null;
    }

    /**
     * Obtiene la descripción completa de la cuenta.
     */
    public String getFullDescription() {
        return String.format("%s - %s (%s)", this.code, this.name, this.nature);
    }

    private void ensureActive() {
        if (!this.active) {
            throw new InvalidLedgerAccountException(
                    "No se puede modificar una cuenta inactiva"
            );
        }
    }

    private void validateMandatoryFields(
            String code,
            NaturalezaCuenta nature) {


        if (code == null || code.isBlank()) {
            throw new InvalidLedgerAccountException("El código de la cuenta es obligatorio");
        }
        if (nature == null) {
            throw new InvalidLedgerAccountException("La naturaleza de la cuenta es obligatoria");
        }
    }


    private void validateAccountCode(String code) {
        String cleanCode = code.trim();

        if (cleanCode.isEmpty() || cleanCode.length() > MAX_CODE_LENGTH) {
            throw new InvalidLedgerAccountException(
                    String.format("El código debe tener entre %d y %d dígitos", MIN_CODE_LENGTH, MAX_CODE_LENGTH)
            );
        }

        if (!ACCOUNT_CODE_PATTERN.matcher(cleanCode).matches()) {
            throw new InvalidLedgerAccountException("El código debe contener solo dígitos numéricos");
        }

        // Validar niveles permitidos (1, 2, 4, 6, 8 dígitos)
        int length = cleanCode.length();
        if (length != 1 && length != 2 && length != 4 && length != 6 && length != 8) {
            throw new InvalidLedgerAccountException(
                    "El código debe tener 1, 2, 4, 6 u 8 dígitos según el nivel de la cuenta"
            );
        }
    }

    // Getters
    public LedgerAccountId getId() { return id; }
    public CompanyId getCompanyId() { return companyId; }
    public String getCode() { return code; }
    public Name getName() { return name; }
    public NaturalezaCuenta getNature() { return nature; }
    public boolean isRequiresThirdParty() { return requiresThirdParty; }
    public boolean isRequiresDocument() { return requiresDocument; }
    public boolean isActive() { return active; }

    // Setters para infraestructura
    public void setId(LedgerAccountId id) { this.id = id; }

}