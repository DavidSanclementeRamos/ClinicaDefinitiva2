package com.example.ClinicaDefinitiva.domain.exceptions;

import com.example.ClinicaDefinitiva.domain.errors.catalog.errorUserAcces.UserIdentityError;
import com.example.ClinicaDefinitiva.domain.errors.context.EntityContext;
import com.example.ClinicaDefinitiva.domain.authentication.vo.UserIdentityId;
import com.example.ClinicaDefinitiva.domain.util.OutcomeDetail;

import java.util.List;


 /**
 * Excepción de dominio cuando un usuario no cumple requisitos
 * para realizar una acción sensible.
 */
public class UserNotEligibleException extends DomainAggregateException {

    private final UserIdentityId userIdentityId;
    private final String reason;
    private final List<OutcomeDetail> details;

    public UserNotEligibleException(
            UserIdentityId userIdentityId,
            String reason,
            EntityContext context,
            List<OutcomeDetail> details
    ) {
        super(
            UserIdentityError.ERR_USER_NOT_ELIGIBLE,
            context
        );
        this.userIdentityId = userIdentityId;
        this.reason = reason;
        this.details = List.copyOf(details);
    }

    public UserIdentityId getUserIdentityId() {
        return userIdentityId;
    }

    public String getReason() {
        return reason;
    }

    public List<OutcomeDetail> getDetails() {
        return details;
    }
}
