package com.example.ClinicaDefinitiva.domain.userAccess.model.permissions.serviceDentist;

import com.example.ClinicaDefinitiva.domain.administration.accessControl.ContextoAccion;
import com.example.ClinicaDefinitiva.domain.administration.accessControl.PermissionPolicy;
import com.example.ClinicaDefinitiva.domain.administration.accessControl.Rol;
import com.example.ClinicaDefinitiva.domain.administration.accessControl.num.RolEnum;

public class DelateDentist  implements PermissionPolicy {
    @Override
    public String getCodigo() {
        return "DELATE DENTIST";
    }


    @Override
    public boolean estaPermitido(Rol rol, ContextoAccion contexto) {
        return rol.getRolEnum(RolEnum.RECEPTIONIST) && contexto.getSectorDestino().equals();
    }
    // eliminar odontólogo
}
