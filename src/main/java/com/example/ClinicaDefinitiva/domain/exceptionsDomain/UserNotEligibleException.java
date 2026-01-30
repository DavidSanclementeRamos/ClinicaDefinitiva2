package com.example.ClinicaDefinitiva.domain.exceptionsDomain;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;

import java.util.List;

/**
 * Excepción de dominio cuando un usuario no cumple requisitos
 * para realizar una acción sensible.
 */
public class UserNotEligibleException extends DomainAggregateException {


    private final UserId userId;
    private final String reason;
    private final List<OutcomeDetail> details;

    public UserNotEligibleException(
            UserId userId,
            String reason,
            EntityContext context,
            List<OutcomeDetail> details
    ) {
        super(
                UserIdentityError.ERR_USER_NOT_ELIGIBLE,
                context

        );
        this.userId = userId;
        this.reason = reason;
        this.details = details;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }

    public List<OutcomeDetail> getDetails() {
        return details;
    }
}
