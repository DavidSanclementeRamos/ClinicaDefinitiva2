# ADR-29 (Arquitectura) – Lección aprendida: El módulo de servicios odontológicos y el verdadero valor de aprender a aprender

**Estado:** Lección aprendida (retrospectiva)  
**Fecha original:** 2026-01-06  
**Reescrito:** 2026-04-11

---

## El tercer módulo como punto de control

El módulo Actor me enseñó qué es un agregado rico y por qué el dominio no es la base de datos.  
El módulo Schedule me enseñó que los errores de diseño entre agregados se siembran en un módulo y se cosechan en otro.  
El módulo `dental.care.services` no me enseñó ninguna de esas cosas — porque ya las sabía.

Y eso, en sí mismo, es la primera lección.

Llegar a un módulo sin cometer los errores anteriores no es casualidad. Es evidencia de que el aprendizaje se internalizó. Los Value Objects, la separación de responsabilidades, la delegación de validaciones, la localización correcta de lógica cruzada — todo eso ya estaba integrado. El módulo de servicios fue el primero donde el diseño emergió con naturalidad en lugar de por corrección de errores.

La única validación que presentó complejidad fue la **desactivación de un servicio**: verificar que no estuviera siendo usado en citas activas, facturas pendientes o tratamientos en curso. En los módulos anteriores hubiera intentado resolver eso dentro del propio agregado. Esta vez identifiqué de inmediato que requería un Domain Service propio porque involucraba coordinar con tres repositorios distintos. Creé `ServiceDeactivationValidator`, lo conecté cuando `Billing` y `Schedule` estuvieron consolidados, y funcionó sin refactorización.

Tres módulos antes, ese problema me hubiera costado semanas.

---

## La evolución de los archivos de descubrimiento: una práctica que se fue transformando sola

Hay algo que me parece importante documentar aquí porque tiene valor más allá de este módulo específico.

Los archivos de "descubrimiento de reglas de negocio" que hoy viven en `evolution/initial-domain-discoveries/` empezaron con un propósito: forzarme a pensar en las invariantes antes de codificar. En los módulos Actor y Schedule, ese proceso fue caótico — producía documentos enormes con decenas de reglas hipotéticas, muchas de las cuales nunca tuvieron código que las respaldara.

Con `dental.care.services` pasó algo diferente: el archivo de descubrimiento se creó casi en paralelo al código, no antes. Las reglas que documenté eran las que el código ya implementaba o estaba a punto de implementar. Ya no había semanas de documentación previa sobre casos que quizás algún día ocurrirían.

Y con los módulos de contabilidad y facturación, el proceso se invirtió completamente: primero codifiqué las bases, luego documenté lo que había. No porque la documentación perdiera valor, sino porque el proceso de descubrimiento se había interiorizado — ya no necesitaba escribirlo en un archivo para pensar con claridad sobre el dominio.

Hoy esos archivos de descubrimiento ya no se crean. El código es la fuente de verdad. Ese cambio no fue una decisión — fue una consecuencia natural de haber acumulado suficiente criterio para que el análisis ocurra en la cabeza antes de llegar al papel.

---

## La verdadera lección: contabilidad y facturación, los módulos que parecían fáciles

Quiero detenerme en los módulos de contabilidad y facturación porque representan el momento en que el proceso de aprendizaje cambió de naturaleza.

Contabilidad y facturación son los proyectos más vendidos en grupos de Facebook y comunidades de programadores: "sistema de facturación", "contabilidad básica", "CRUD contable". Cuando llegué a ellos asumí que serían los más directos del proyecto. Me equivoqué completamente.

Lo primero que hice fue lo que hubiera hecho dos módulos antes: intentar escribir reglas de negocio desde mi imaginación. Duré poco haciendo eso. La contabilidad tiene una lógica interna propia, normativa colombiana específica (el PUC, la DIAN, las retenciones, la partida doble, los asientos de ajuste), y convenciones que no se inventan. Si modelaba basándome en suposiciones, iba a producir exactamente el tipo de formulario glorificado que veía en esos grupos — datos que entran y salen sin ninguna lógica real que los gobierne.

Tomé una decisión diferente: estudiar el dominio de verdad antes de escribir una línea. Revisé el PUC colombiano, la normativa DIAN, usé sistemas contables de software libre para entender qué necesidades cubren, qué decisiones de diseño toman y por qué. Ese proceso tomó semanas. Fue el período de investigación más largo de todo el proyecto.

El resultado fue que cuando finalmente senté las bases del módulo, no cometí errores de diseño significativos. No porque el dominio fuera fácil — es el más complejo del proyecto, y el módulo de contabilidad es el más grande en términos de agregados y reglas. Sino porque llegué a él con contexto real, no con suposiciones.

La diferencia entre ese módulo y los anteriores no fue técnica. Fue metodológica.

---

## Lecciones aprendidas

**1. La experiencia acumulada se mide en errores que ya no se cometen.**  
Llegar a un módulo sin cometer los errores anteriores no es suerte. Es la señal de que el aprendizaje se volvió criterio. Si tienes que corregir el mismo tipo de error en cada módulo, algo no se internalizó.

**2. Las validaciones cruzadas entre módulos tienen una estructura clara.**  
Cuando una validación requiere coordinar con agregados de otros módulos, la respuesta es siempre un Domain Service especializado. Nunca el agregado raíz. En este módulo esa decisión fue obvia; en Actor y Schedule costó semanas descubrirla.

**3. Los archivos de diseño previo son una muleta, no una metodología.**  
Son útiles cuando no tienes suficiente criterio para analizar un dominio directamente. Cuando ese criterio existe, el análisis ocurre durante la implementación, no antes en un documento. Saber cuándo ya no los necesitas es parte del crecimiento.

**4. Los módulos aparentemente simples esconden los dominios más complejos.**  
"Sistema de facturación" y "contabilidad básica" suenan a proyectos de fin de semana. No lo son. Un sistema contable real requiere entender partida doble, PUC, retenciones, DIAN y ciclos de cierre. La mayoría de los proyectos que se ofrecen en ese rubro son formularios de entrada de datos sin ninguna regla de negocio que los gobierne.

**5. Estudiar el dominio antes de modelarlo no es opcional cuando el dominio tiene normativa real.**  
En contabilidad no puedes inventar las reglas. Existen, están escritas, y tienen consecuencias legales. El tiempo invertido en estudiarlas no es tiempo perdido — es el tiempo que te ahorra reescribir un modelo mal concebido.

**6. Definir límites de alcance es una decisión de diseño, no una excusa.**  
Un sistema contable completo es un ERP. Definir qué se implementa y qué queda fuera no es hacer menos — es evitar que el módulo se vuelva inmanejable. Los límites explícitos son parte de la arquitectura.

---

## Reflexión final

Los tres módulos que tienen documento de lección aprendida — Actor, Schedule y `dental.care.services` — forman un arco que refleja cómo cambia la relación con el error a medida que se acumula criterio.

En Actor, los errores eran sobre conceptos fundamentales que no comprendía. En Schedule, eran sobre cómo los errores se propagan entre módulos. En Services, el error notable no estuvo en el código — estuvo en subestimar lo que significaba modelar un dominio con normativa real.

Eso es lo que quiero decir con "aprender a aprender": no acumular tecnologías ni patrones, sino desarrollar el criterio para saber qué necesita cada problema antes de intentar resolverlo. En los primeros módulos ese criterio era técnico. En contabilidad fue investigativo. La herramienta cambió. El principio fue el mismo.

El ADR-29 original era un inventario de lo que se implementó. Este documento es lo que costó llegar a ese punto.

---

*Ver decisiones vigentes: [ADR-(Arquitectura)-05](../../architecture/decisions/arch/ADR-%28Arquitectura%29-05-Creaci%C3%B3n%20de%20un%20m%C3%B3dulo%20independiente%20para%20Servicios.md), [ADR-(Contabilidad)-03](../../architecture/decisions/domain/accounting/ADR-%28Contabilidad%29-03-Modelado%20de%20plan%20de%20cuenta.md), [ADR-(Contabilidad)-05](../../architecture/decisions/domain/accounting/ADR-%28Contabilidad%29-05-Modelado%20de%20Repostes%20contables.md), [ADR-(Facturación)-01](../../architecture/decisions/domain/billing/ADR-%28Facturaci%C3%B3n%29-01-Validaci%C3%B3n%20de%20Tarifas%20Vigentes%20al%20Momento%20de%20Facturar.md), [ADR-(Facturación)-03](../../architecture/decisions/domain/billing/ADR-%28Facturaci%C3%B3n%29-03-Cumplimiento%20Normativo%20DIAN%20Colombia.md),
[ADR-(Servicio)-07](../../architecture/decisions/domain/dentalServices/ADR-%28Servicio%29-07-Ubicaci%C3%B3n%20de%20Value%20Objects%20de%20composici%C3%B3n%20en%20el%20agregado%20ProvidedService.md), [ADR-(Servicio)-09](../../architecture/decisions/domain/dentalServices/ADR-%28Servicio%29-09-Delegaci%C3%B3n%20de%20Validaci%C3%B3n%20de%20Cambio%20de%20Tarifa.md), [ADR-(Servicio)-10](../../architecture/decisions/domain/dentalServices/ADR-%28Servicio%29-10-Simplificacion-flujo-creacion-InvoiceItem.md), [ADR-(Arquitectura)-06](../../architecture/decisions/arch/ADR-%28Arquitectura%29-06-Separaci%C3%B3n%20de%20Facturaci%C3%B3n%20y%20Pagos%20en%20m%C3%B3dulos%20independientes.md), [ADR-(Arquitectura)-07](../../architecture/decisions/arch/ADR-%28Arquitectura%29-07-Redefinici%C3%B3n%20del%20m%C3%B3dulo%20Administration.md), [ADR-(Arquitectura)-10](../../architecture/decisions/arch/ADR-%28Arquitectura%29-10-dentalService.md),
[ADR-(Arquitectura)-11](../../architecture/decisions/arch/ADR-%28Arquitectura%29-11-Implementaci%C3%B3n-inicial-de-m%C3%B3dulo-contable.md), [ADR-(Arquitectura)-12](../../architecture/decisions/arch/ADR-%28Arquitectura%29-12-Nuevos-agregados-en-modulo-contable.md), [ADR-(Arquitectura)-17](../../architecture/decisions/arch/ADR-%28Arquitectura%29-17-Manejo%20de%20Plan%20de%20Cuentas%20y%20Asientos%20Contables.md)*