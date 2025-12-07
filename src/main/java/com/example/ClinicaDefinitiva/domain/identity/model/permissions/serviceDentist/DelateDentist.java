package com.example.ClinicaDefinitiva.domain.identity.model.permissions.serviceDentist;

import com.example.ClinicaDefinitiva.domain.administration.permisos.ContextoAccion;
import com.example.ClinicaDefinitiva.domain.administration.permisos.PermissionPolicy;
import com.example.ClinicaDefinitiva.domain.administration.permisos.Rol;
import com.example.ClinicaDefinitiva.domain.administration.permisos.num.RolEnum;

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
