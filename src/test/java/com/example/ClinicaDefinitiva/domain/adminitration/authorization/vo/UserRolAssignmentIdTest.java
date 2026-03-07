
package com.example.ClinicaDefinitiva.domain.adminitration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.UserRolAssignmentId;
import com.example.ClinicaDefinitiva.domain.exceptions.ValueObjectValidationException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRolAssignmentIdTest {

    @Test
    void shouldCreateUserRolAssignmentIdSuccessfully() {
        UserRolAssignmentId id = UserRolAssignmentId.of(10L);
        assertEquals(10L, id.getValue());
    }

    @Test
    void shouldThrowExceptionForInvalidValues() {
        assertThrows(ValueObjectValidationException.class, () -> UserRolAssignmentId.of(null));
    }

    @Test
    void shouldRespectEquality() {
        UserRolAssignmentId id1 = UserRolAssignmentId.of(5L);
        UserRolAssignmentId id2 = UserRolAssignmentId.of(5L);
        UserRolAssignmentId id3 = UserRolAssignmentId.of(6L);

        assertEquals(id1, id2);
        assertNotEquals(id1, id3);
    }
}

