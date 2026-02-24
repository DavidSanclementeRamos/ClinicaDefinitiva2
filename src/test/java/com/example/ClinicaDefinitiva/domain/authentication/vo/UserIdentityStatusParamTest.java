package com.example.ClinicaDefinitiva.domain.authentication.vo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class UserIdentityStatusParamTest {

    @ParameterizedTest
    @CsvSource({
            "PENDING_VERIFICATION, ACTIVE, true",
            "PENDING_VERIFICATION, SUSPENDED, false",
            "ACTIVE, INACTIVE, true",
            "ACTIVE, SUSPENDED, true",
            "ACTIVE, ACTIVE, false",
            "INACTIVE, ACTIVE, true",
            "SUSPENDED, ACTIVE, true",
            "INACTIVE, SUSPENDED, false"
    })
    void testTransitions(UserIdentityStatus.Status from, UserIdentityStatus.Status to, boolean expected) {
        UserIdentityStatus status = UserIdentityStatus.of(from);
        assertEquals(expected, status.canTransitionTo(to));
    }
}
