package com.example.ClinicaDefinitiva.domain.identity.model.permissions;


public interface PermissionModel {
    String getCodigo();

     boolean estaPermitido(Rol rol, ContextoAccion contexto);


}
