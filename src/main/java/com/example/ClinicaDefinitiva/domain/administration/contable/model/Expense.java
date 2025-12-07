package com.example.ClinicaDefinitiva.domain.administration.contable.model;

import com.example.ClinicaDefinitiva.domain.administration.contable.valueObject.*;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.ExpenseCategory;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.ExpenseStatus;
import com.example.ClinicaDefinitiva.domain.administration.contable.enu.PaymentMethod;
import com.example.ClinicaDefinitiva.domain.Money;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Expense {
    // gastos sera eliminado
    private ExpenseId id;
    private CompanyId empresaId;
    private  LocalDate dateIncurred;
    private  LocalDateTime recordedAt;
    private  Money amount;
    private  Money taxAmount;
    private  ExpenseCategory category;
    private  PaymentMethod paymentMethod;
    private ThirdPartiesId terceroId; // proveedor
    private  ContractId contractId; // opcional
    private JournalEntryId cuentaContableId; // imputación contable
    private  String description;
    private  ExpenseStatus status;
    private AuditoriaInfo audit;

    public Expense() {
    }

    public Expense(ExpenseId id, CompanyId empresaId, LocalDate dateIncurred, LocalDateTime recordedAt, Money amount, Money taxAmount, ExpenseCategory category, PaymentMethod paymentMethod, ThirdPartiesId terceroId, ContractId contractId, JournalEntryId cuentaContableId, String description, ExpenseStatus status, AuditoriaInfo audit) {
        this.id = id;
        this.empresaId = empresaId;
        this.dateIncurred = dateIncurred;
        this.recordedAt = recordedAt;
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.terceroId = terceroId;
        this.contractId = contractId;
        this.cuentaContableId = cuentaContableId;
        this.description = description;
        this.status = status;
        this.audit = audit;
    }
    public static Expense registerExpense(
            CompanyId empresaId,
            LocalDate dateIncurred,
            LocalDateTime recordedAt,
            Money amount,
            Money taxAmount,
            ExpenseCategory category,
            PaymentMethod paymentMethod,
            ThirdPartiesId terceroId,
            ContractId contractId,
            JournalEntryId cuentaContableId,
            String description
           // ExpenseStatus status
            //AuditoriaInfo audit
    ) {

        Expense expense = new Expense();

        expense.setEmpresaId(empresaId);
        expense.setDateIncurred(dateIncurred);
        expense.setRecordedAt(recordedAt);
        expense.setAmount(amount);
        expense.setTaxAmount(taxAmount);
        expense.setCategory(category);
        expense.setPaymentMethod(PaymentMethod.valueOf(String.valueOf(paymentMethod)));
        expense.setTerceroId(terceroId);
        expense.setContractId(contractId);
        expense.setCuentaContableId(cuentaContableId);
        expense.setDescription(description);
        expense.setStatus(ExpenseStatus.APPROVED);

       return expense;
    }




    public void Update(
            String description,
            ExpenseStatus status
            ){
        this.description= description;
        this.status = status;


    }
    public void UpdateSensitive(
            ExpenseCategory category,
            PaymentMethod paymentMethod

            ){

    }

    public ThirdPartiesId getTerceroId() {
        return terceroId;
    }

    public Money getTaxAmount() {
        return taxAmount;
    }

    public ExpenseStatus getStatus() {
        return status;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public ExpenseId getId() {
        return id;
    }

    public CompanyId getEmpresaId() {
        return empresaId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateIncurred() {
        return dateIncurred;
    }

    public void setTerceroId(ThirdPartiesId terceroId) {
        this.terceroId = terceroId;
    }

    public void setTaxAmount(Money taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setStatus(ExpenseStatus status) {
        this.status = status;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setId(ExpenseId id) {
        this.id = id;
    }

    public void setEmpresaId(CompanyId empresaId) {
        this.empresaId = empresaId;
    }

    public void setDescription(String descripcion) {
        this.description = descripcion;
    }

    public void setDateIncurred(LocalDate dateIncurred) {
        this.dateIncurred = dateIncurred;
    }

    public void setCuentaContableId(JournalEntryId cuentaContableId) {
        this.cuentaContableId = cuentaContableId;
    }

    public void setContractId(ContractId contractId) {
        this.contractId = contractId;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    public void setAudit(AuditoriaInfo audit) {
        this.audit = audit;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public JournalEntryId getCuentaContableId() {
        return cuentaContableId;
    }

    public ContractId getContractId() {
        return contractId;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    public AuditoriaInfo getAudit() {
        return audit;
    }

    public Money getAmount() {
        return amount;
    }
}
