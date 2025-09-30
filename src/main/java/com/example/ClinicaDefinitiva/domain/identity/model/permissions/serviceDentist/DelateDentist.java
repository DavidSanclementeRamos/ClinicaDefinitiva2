package com.example.ClinicaDefinitiva.domain.identity.model.permissions.serviceDentist;

import com.example.ClinicaDefinitiva.domain.identity.model.permissions.ContextoAccion;
import com.example.ClinicaDefinitiva.domain.identity.model.permissions.PermissionModel;
import com.example.ClinicaDefinitiva.domain.identity.model.permissions.Rol;
import com.example.ClinicaDefinitiva.domain.identity.num.RolEnum;

public class DelateDentist  implements PermissionModel{
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
