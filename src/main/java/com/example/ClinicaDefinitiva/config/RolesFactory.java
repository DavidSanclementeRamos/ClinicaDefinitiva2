package com.example.ClinicaDefinitiva.config;

import com.example.ClinicaDefinitiva.Enum.Estado;
import com.example.ClinicaDefinitiva.Enum.Permisos;
import com.example.ClinicaDefinitiva.Enum.Roles;
import com.example.ClinicaDefinitiva.persistence.entity.RolesEntity;

import java.util.*;

public final class RolesFactory {

    private static final Map<Roles, RolesEntity> roles = new HashMap<>();

    static {
        roles.put(Roles.ADMINISTRADOR, new RolesEntity(
                "Administrador con acceso completo",
                true,
                true,
                true,
                List.of(  // PERMISOS HORARIO SERVICE
                        Permisos.GET_HORARIO_POR_FECHA,
                        Permisos.GET_HORARIOS_DISPONIBLE,
                        Permisos.GET_HORARIO_POR_ODONTOLOGO_ID,
                        Permisos.GET_HORARIOS_LIST,
                        Permisos.GET_HORARIO_ID,
                        Permisos.POST_HORARIOS,
                        Permisos.PUT_HORARIOS,
                        Permisos.DELETE_HORARIO,

                        // PERMISOS USUARIO SERVICE

                        Permisos.GET_USUARIO_ID,
                        Permisos.GET_USUARIO_POR_NOMBRE,
                        Permisos.GET_USUARIO_POR_EMAIL,
                        Permisos.GET_USUARIOS_LIST,
                        Permisos.POST_USUARIOS,
                        Permisos.PUT_USUARIOS,
                        Permisos.DELETE_USUARIO,

                        // PERMISOS ODONTOLOGO SERVICE

                        Permisos.GET_ODONTOLOGO_ID,
                        Permisos.GET_ODONTOLOGO_POR_TURNO,
                        Permisos.GET_ODONTOLOGO_POR_ESPECIALIDAD,
                        Permisos.GET_ODONTOLOGOS_LIST,
                        Permisos.GET_ODONTOLOGO_POR_USUARIO_ID,
                        Permisos.POST_ODONTOLOGOS,
                        Permisos.PUT_ODONTOLOGOS,
                        Permisos.DELETE_ODONTOLOGOS,

                        // PERMISOS PACIENTE SERVICE

                        Permisos.GET_PACIENTE_ID,
                        Permisos.GET_PACIENTE_POR_DOCUMENTO,
                        Permisos.GET_PACIENTE_POR_NOMBRE,
                        Permisos.GET_PACIENTE_TURNO,
                        Permisos.GET_PACIENTE_POR_USUARIO,
                        Permisos.GET_PACIENTES_LIST,
                        Permisos.POST_PACIENTES,
                        Permisos.PUT_PACIENTES,
                        Permisos.DELETE_PACIENTES,

                        // PERMISOS RESPONSABLE SERVICE

                        Permisos.GET_RESPONSABLE_ID,
                        Permisos.GET_RESPONSABLE_POR_DOCUMENTO,
                        Permisos.GET_RESPONSABLE_POR_PACIENTE,
                        Permisos.GET_RESPONSABLE_POR_TELEFONO,
                        Permisos.GET_RESPONSABLE_POR_PARENTESCO,
                        Permisos.POST_RESPONSABLES,
                        Permisos.PUT_RESPONSABLES,
                        Permisos.DELETE_RESPONSABLES,

                        // PERMISOS SECRETARIO SERVICE

                        Permisos.GET_SECRETARIO_ID,
                        Permisos.GET_SECRETARIO_POR_NOMBRE,
                        Permisos.GET_SECRETARIO_SECTOR,
                        Permisos.GET_SECRETARIO_POR_USUARIO,
                        Permisos.GET_SECRETARIOS_LIST,
                        Permisos.POST_SECRETARIOS,
                        Permisos.PUT_SECRETARIOS,
                        Permisos.DELETE_SECRETARIOS,

                        // PERMISOS TURNOS SERVICE

                        Permisos.GET_TURNO_ID,
                        Permisos.GET_TURNO_POR_DISPONIBILIDAD,
                        Permisos.GET_TURNO_POR_ESTADO,
                        Permisos.GET_TURNO_POR_FECHA,
                        Permisos.GET_TURNO_POR_ODONTOLOGO,
                        Permisos.GET_TURNOS_LIST,
                        Permisos.POST_TURNOS,
                        Permisos.PUT_TURNOS,
                        permisos.DELETE_TURNOS),
                Roles.ADMINISTRADOR,
                Estado.ACTIVO


                ));
                roles.put(Roles.ODONTOLOGO, new RolesEntity(
                        " con acceso a turnos, get de responsable, ge de paciente",
                        true,
                        true,
                        true,
                        List.of(  // PERMISOS TUNO SERVICE
                                Permisos.GET_TURNO_POR_DISPONIBILIDAD,
                                Permisos.GET_TURNO_POR_FECHA,

                                // PERMISOS RESPONSABLE SERVICE
                                Permisos.GET_RESPONSABLE_POR_DOCUMENTO,
                                Permisos.GET_RESPONSABLE_POR_PACIENTE,
                                Permisos.GET_RESPONSABLE_POR_TELEFONO,
                                Permisos.GET_RESPONSABLE_POR_PARENTESCO,

                                // PERMISOS PACIENTE SERVICE
                                Permisos.GET_PACIENTE_POR_DOCUMENTO,
                                Permisos.GET_PACIENTE_POR_NOMBRE,
                                Permisos.GET_PACIENTE_TURNO,

                                // PERMISOS ODONTOLOGO SERVICE
                                Permisos.GET_ODONTOLOGO_POR_TURNO,
                                Permisos.PUT_ODONTOLOGOS,

                                // PERMISOS USUARIOS SERVICE
                                Permisos.PUT_USUARIOS,

                                // PERMISOS HORARIO SERVICE
                                Permisos.GET_HORARIO_POR_FECHA,
                                Permisos.GET_HORARIOS_DISPONIBLE,
                                Permisos.GET_HORARIO_POR_ODONTOLOGO_ID,
                                Permisos.GET_HORARIOS_LIST


                        ),
                        Roles.ODONTOLOGO,
                        Estado.ACTIVO


                ));
                roles.put(Roles.PACIENTE, new RolesEntity(
                        "con acceso ilimitado",
                        true,
                        true,
                        true,
                        List.of( // PERMISOS USUARIOS SERVICE
                                Permisos.PUT_USUARIOS,

                                // PERMISOS TURNOS SERVICE
                                Permisos.GET_TURNO_POR_DISPONIBILIDAD,
                                Permisos.GET_TURNO_POR_ODONTOLOGO,
                                Permisos.PUT_TURNOS,
                                Permisos.GET_TURNO_POR_FECHA,

                                // PERMISO PACIENTE SERVICE
                                Permisos.PUT_PACIENTES),
                        Roles.PACIENTE,
                        Estado.ACTIVO


                ));
        roles.put(Roles.RESPONSABLE, new RolesEntity(
                " con acceso ilimitado",
                true,
                true,
                true,
                List.of(  // PERMISOS USUARIOS SERVICE
                        Permisos.PUT_USUARIOS,

                        // PERMISOS TURNOS SERVICE
                        Permisos.GET_TURNO_POR_DISPONIBILIDAD,
                        Permisos.GET_TURNO_POR_ODONTOLOGO,
                        Permisos.PUT_TURNOS,
                        Permisos.GET_TURNO_POR_FECHA,

                        // PERMISO PACIENTE SERVICE
                        Permisos.PUT_PACIENTES,

                        // PERMISOS RESPONSABLE SERVICE
                        Permisos.PUT_RESPONSABLES),
                Roles.RESPONSABLE,
                Estado.ACTIVO


        ));
        roles.put(Roles.SECRETARIO, new RolesEntity(
                "Secretario con acceso completo",
                true,
                true,
                true,
                List.of(Permisos.GET_HORARIO_POR_FECHA,
                        Permisos.GET_HORARIOS_DISPONIBLE,
                        Permisos.GET_HORARIO_POR_ODONTOLOGO_ID,
                        Permisos.GET_HORARIOS_LIST,
                        Permisos.GET_HORARIO_ID,
                        Permisos.POST_HORARIOS,
                        Permisos.PUT_HORARIOS,
                        Permisos.DELETE_HORARIO,

                        // PERMISOS USUARIO SERVICE

                        Permisos.GET_USUARIO_ID,
                        Permisos.GET_USUARIO_POR_NOMBRE,
                        Permisos.GET_USUARIO_POR_EMAIL,
                        Permisos.GET_USUARIOS_LIST,
                        Permisos.POST_USUARIOS,
                        Permisos.PUT_USUARIOS,
                        Permisos.DELETE_USUARIO,

                        // PERMISOS ODONTOLOGO SERVICE

                        Permisos.GET_ODONTOLOGO_ID,
                        Permisos.GET_ODONTOLOGO_POR_TURNO,
                        Permisos.GET_ODONTOLOGO_POR_ESPECIALIDAD,
                        Permisos.GET_ODONTOLOGOS_LIST,
                        Permisos.GET_ODONTOLOGO_POR_USUARIO_ID,
                        Permisos.POST_ODONTOLOGOS,
                        Permisos.PUT_ODONTOLOGOS,
                        Permisos.DELETE_ODONTOLOGOS,

                        // PERMISOS PACIENTE SERVICE

                        Permisos.GET_PACIENTE_ID,
                        Permisos.GET_PACIENTE_POR_DOCUMENTO,
                        Permisos.GET_PACIENTE_POR_NOMBRE,
                        Permisos.GET_PACIENTE_TURNO,
                        Permisos.GET_PACIENTE_POR_USUARIO,
                        Permisos.GET_PACIENTES_LIST,
                        Permisos.POST_PACIENTES,
                        Permisos.PUT_PACIENTES,
                        Permisos.DELETE_PACIENTES,

                        // PERMISOS RESPONSABLE SERVICE

                        Permisos.GET_RESPONSABLE_ID,
                        Permisos.GET_RESPONSABLE_POR_DOCUMENTO,
                        Permisos.GET_RESPONSABLE_POR_PACIENTE,
                        Permisos.GET_RESPONSABLE_POR_TELEFONO,
                        Permisos.GET_RESPONSABLE_POR_PARENTESCO,
                        Permisos.POST_RESPONSABLES,
                        Permisos.PUT_RESPONSABLES,
                        Permisos.DELETE_RESPONSABLES,

                        // PERMISOS SECRETARIO SERVICE

                        Permisos.GET_SECRETARIO_ID,
                        Permisos.GET_SECRETARIO_POR_NOMBRE,
                        Permisos.GET_SECRETARIO_SECTOR,
                        Permisos.GET_SECRETARIO_POR_USUARIO,
                        Permisos.GET_SECRETARIOS_LIST,
                        Permisos.POST_SECRETARIOS,
                        Permisos.PUT_SECRETARIOS,
                        Permisos.DELETE_SECRETARIOS,

                        // PERMISOS TURNOS SERVICE

                        Permisos.GET_TURNO_ID,
                        Permisos.GET_TURNO_POR_DISPONIBILIDAD,
                        Permisos.GET_TURNO_POR_ESTADO,
                        Permisos.GET_TURNO_POR_FECHA,
                        Permisos.GET_TURNO_POR_ODONTOLOGO,
                        Permisos.GET_TURNOS_LIST,
                        Permisos.POST_TURNOS,
                        Permisos.PUT_TURNOS,
                        Permisos.DELETE_TURNOS),
                Roles.SECRETARIO,
                Estado.ACTIVO


        ));



    }

    public static RolesEntity createRoleEntity(Roles rol) {
    RolesEntity entity = roles.get(rol);
    if (entity == null) throw new IllegalArgumentException("RolEnum no definido: " + rol);
    return entity;

}
    }

   /* private RolesFactory() {}

    private static final Map<Roles, Set<Permisos>> M = Map.of(
            Roles.ADMINISTRADOR, EnumSet.of(



            ),
            Roles.ODONTOLOGO, EnumSet.of(




                    ),
            Roles.PACIENTE, EnumSet.of(


                    ),

            Roles.RESPONSABLE, EnumSet.of(

            )
    );

    public static RolesEntityRepository createRoleEntity(Roles r) {
        RolesEntityRepository e = new RolesEntityRepository();
        e.setRoleEnum(r);
        e.setPermissionList(new HashSet<>(M.getOrDefault(r, EnumSet.noneOf(Permisos.class))));
        return e;
    }

    }
    */

