```
+-------------------+        +---------------------------+        +--------------------------+
| DentistValidator  |        | PatientValidator          |        | GuardianValidator        |
|-------------------|        |---------------------------|        |--------------------------|
| validate(dentist) |        | validate(patient)         |        | validate(guardian)       |
| -> Outcome        |        | -> Outcome                |        | -> Outcome               |
+-------------------+        +---------------------------+        +--------------------------+
          \                          |                                /
           \                         |                               /
            \                        |                              /
             \                       |                             /
              \                      |                            /
               \                     |                           /
                \                    |                          /
                 \                   |                         /
                  \                  |                        /
                   \                 |                       /
                    \                |                      /
                     v               v                     v
                 +---------------------------------------------------+
                 |           UserDeactivationPolicy                  |
                 |---------------------------------------------------|
                 | merge Outcomes from all validators                |
                 | -> Outcome global (acumulado)                     |
                 +---------------------------------------------------+
                                |
                                v
                 +-----------------------------------+
                 |       UserApplicationService      |
                 |-----------------------------------|
                 | outcome = policy.validate(user)   |
                 | if outcome.isFailure():           |
                 |    throw AggregateBusinessRule... |
                 | else:                             |
                 |    user.deactivate()              |
                 +-----------------------------------+
                                |
                                v
                 +-----------------------------------+
                 |        UserRepository             |
                 |-----------------------------------|
                 | save(user)                        |
                 +-----------------------------------+

```

Explicación del flujo
Validadores especializados (DentistDeactivationValidator, PatientValidator, etc.)

Cada uno devuelve un Outcome con éxito o con errores específicos.

UserDeactivationPolicy (orquestador)

Recibe los outcomes de cada validador.

Usa merge y addDetail para acumular progresivamente todos los errores en un Outcome global.

Application Service

Invoca UserDeactivationPolicy.validate(user).

Si el outcome global es fallo, lanza una excepción compuesta con todos los errores acumulados.

Si es éxito, procede a desactivar el usuario y guardar cambios.

Infraestructura (UserRepository)

Persiste el estado final del usuario.

🎯 Beneficio de este diseño
El Outcome fluye de manera consistente por todas las capas.

Los errores se acumulan progresivamente y se reportan juntos.

El Application Service no necesita conocer reglas específicas de cada agregado, solo interpreta el Outcome global.

Se mantiene la exhibibilidad, trazabilidad y claridad semántica del modelo.