package com.example.ClinicaDefinitiva.util;



public class ErrorCodeResolver {

    /**private static final Map<Class<?>, ErrorCatalogXD> errorMapping = Map.ofEntries(
            Map.entry(CreateOdontologoDto.class, ErrorCatalogXD.INVALID_DENTIST),
            Map.entry(CreatePacienteDto.class, ErrorCatalogXD.INVALID_PATIENT),
            Map.entry(CreateSecretarioDto.class, ErrorCatalogXD.INVALID_SECRETARY),
            Map.entry(CreateEndReadResponsableDto.class, ErrorCatalogXD.INVALID_RESPONSIBLE),
            Map.entry(CreateUsuarioDto.class, ErrorCatalogXD.INVALID_USER),
            Map.entry(HorarioDto.class, ErrorCatalogXD.INVALID_SCHEDULE),
            Map.entry(TurnoDto.class, ErrorCatalogXD.INVALID_SHIFT)
            //  Puedes seguir agregando tus DTO aquí fácilmente
    );

    public static ErrorCatalogXD resolver(Class<?> dtoClass) {
        return errorMapping.getOrDefault(dtoClass, ErrorCatalogXD.GENERIC_ERROR);
    }*/

}
