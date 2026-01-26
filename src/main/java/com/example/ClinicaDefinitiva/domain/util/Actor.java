package com.example.ClinicaDefinitiva.domain.util;

import com.example.ClinicaDefinitiva.domain.userAccess.valueObjectes.UserId;

public interface Actor {
    Outcome assertCanBeDeactivated(String reason);
     UserId getUserId(U);



}


