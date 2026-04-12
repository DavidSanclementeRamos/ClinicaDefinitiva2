#  La historia detrás del proyecto

## Origen: el estancamiento y la búsqueda del proyecto definitivo

Este proyecto nació el **13 de marzo de 2025** como una iniciativa personal para salir de un periodo de estancamiento profesional. Había completado varios cursos de Spring Boot y entendía que el aprendizaje teórico no era suficiente. Necesitaba construir algo real.

Pero había un problema común a muchos desarrolladores que empiezan: **no sabía qué construir**. Pasaba horas buscando "el proyecto definitivo" que me ayudara a conseguir trabajo. Consumía videos sobre qué proyectos hacer en lugar de empezar a construir uno. La búsqueda del proyecto perfecto se había convertido en una forma elegante de no hacer nada.

Cuando se habla de proyectos en YouTube, nadie explica el tecnicismo que hay detrás. Nadie te dice que el verdadero valor está en los **requerimientos y el análisis**. Así que te aferras a los proyectos típicos: CRUDs básicos que todo el mundo tiene.

## El consejo que no entendí al principio

La salida llegó gracias a la respuesta a una pregunta que le hice al amigo de un amigo, alguien que ya trabajaba en el rubro:

> "¿Qué tipo de proyectos debería hacer? ¿Cómo puedo destacar entre tantos competidores?"

Su respuesta no fue la fórmula mágica que esperaba:

> "Parce, cualquier proyecto sirve, siempre y cuando se usen bien las librerías, haya código limpio, buena arquitectura, buena documentación, principios SOLID, patrones de diseño y buen diseño de base de datos. Desde que eso se pueda evidenciar en el repo, suma a un buen portafolio."

En ese momento me pareció la respuesta genérica de siempre. Pero ese mismo día encontré el valor real de ese consejo.

## El canal que cambió todo

A las 11:30 p.m., buscando otro curso, encontré **Dev Dominio**: un canal pequeño pero con contenido diferente al que estaba acostumbrado.

No era el típico "construye una API en 2 horas". Se enfocaba en el **por qué** de las cosas: por qué se hace de una manera, qué alternativas existen, cuándo usar cada una. La API de ejemplo tenía solo dos entidades —producto y categoría— pero construida aplicando principios reales, evitando los anti-patrones habituales de los cursos. Era, en esencia, el tipo de código que se exige en una empresa.

Ahí entendí el consejo que me habían dado. Lo que en palabras sonaba simple, ese curso lo hacía concreto y complejo.

## La primera refactorización: de Java EE a Spring Boot

Para aplicar lo aprendido, tomé el mejor proyecto que tenía: una clínica odontológica construida en **Java EE**, del curso de la ingeniera Lucina en **TodoCode**. Migrar a Spring Boot no fue difícil. Aplicar lo del curso de Dev Dominio tampoco fue difícil... tardé aproximadamente **5 meses** aplicando lo aprendido de un curso de 3 horas.

No estaba haciendo copia y pega. Estaba migrando un proyecto a una nueva tecnología, corrigiendo anti-patrones, y sin ningún manual que seguir al pie de la letra. Cada decisión requería entender por qué, no solo cómo.

## El golpe de realidad: el hackathon

Cuando terminé esa primera refactorización, creía que ya tenía el nivel para competir profesionalmente. Quise unirme a un hackathon y el líder del equipo fue directo: mi proyecto no tenía el nivel que necesitaban.

Me lo tomé personal, porque genuinamente creía en lo que había construido. Pero esa respuesta fue útil. En lugar de abandonar, decidí entender por qué el proyecto no era suficiente.

## El segundo obstáculo: la arquitectura como cuello de botella

Al intentar agregar nuevas funcionalidades descubrí el problema real: **no podía escalar**. Por mucho que había mejorado el código, tenía demasiado acoplamiento y baja cohesión. Agregar cosas nuevas requería tocar demasiados lugares a la vez.

Esto se debe a algo que nadie explica en los cursos: **la arquitectura**. La mayoría enseña MVC como si fuera el estándar universal. Hoy ese enfoque dificulta la escalabilidad, el testing y la separación de responsabilidades. Así comenzó la segunda refactorización: hacia **arquitectura hexagonal y DDD**.

## El aprendizaje más difícil: entender el dominio

Implementar arquitectura hexagonal fue un reto. Da la sensación de ser complejidad innecesaria cuando no se entiende para qué sirve.

Lo que más me costó fue comprender el significado del **dominio**, las **reglas de negocio** y los **servicios de dominio**. En los cursos de YouTube, las clases se tratan como POJOs sin lógica, cuya única función es persistir en base de datos. Incluso en cursos sobre arquitectura hexagonal encontré ese mismo error. Eso hacía que toda la abstracción pareciera innecesaria.

La realidad es que esos cursos omiten lo que es verdaderamente complejo: **un dominio rico en reglas de negocio**. Y construir eso requiere invertir tiempo en levantar requerimientos reales.

Eso me llevó a cometer el error más costoso del proyecto: usar IA de forma irreflexiva, copiando código que no entendía, hasta el punto de perder completamente el hilo. No sabía si lo que tenía estaba bien o era basura funcional.

## El reinicio: volver a los principios

Tuve que tomar la decisión más difícil: dejar el código a un lado y volver a lo básico. Lógica de programación, diseño, arquitectura. Entender antes de construir.

Después de un mes, retomé el proyecto con otro enfoque. Me dediqué a analizar los módulos reales de una clínica: qué requerimientos tendría, qué validaciones cruzadas existirían, qué ciclos de estado tendría cada clase. Ese análisis me mostró cuánto código incorrecto había acumulado, y por qué el proyecto iba en mal rumbo.

Aunque la IA fue un atraso en esa etapa por mi inexperiencia, esos errores me dieron algo valioso: **criterio para identificar cuándo la IA comete errores**. Hoy uso IA como herramienta de contraste, no como sustituto del razonamiento.

## El módulo más desafiante: autorización y autenticación

Cada módulo requirió semanas de investigación antes de escribir una sola línea de código. Pero si tuviera que elegir el más difícil, es la **autorización y autenticación**.

Razón: está completamente alejado de lo que se enseña en YouTube.

1. Se suele tratar seguridad y autorización como conceptos equivalentes.
2. Se da a entender que el framework resuelve ambas cosas.

En arquitectura hexagonal, ese acoplamiento no existe. Seguridad y autorización se convierten en responsabilidades del dominio, que deben resolverse sin depender del framework. Entender eso, modelarlo correctamente y documentar las decisiones tomadas ocupó semanas de iteración.

## Un año y un mes después

Al día en que escribo esto —10 de abril de 2026— llevo **un año y un mes** desde que comencé a refactorizar este proyecto.

La migración a hexagonal fue un éxito: el proyecto corre, los tests pasan. El dominio está modelado con criterio real. Las decisiones arquitectónicas están documentadas en más de 90 ADRs. Lo que empezó como un ejercicio de portafolio terminó siendo un sistema con profundidad técnica real.

No está terminado. Quedan módulos sin pruebas de integración, endpoints por validar, funcionalidades planificadas que no se implementaron. Eso es parte honesta de cualquier proyecto.

**Lo importante es lo que sí está**: una base de arquitectura hexagonal madura, un catálogo de decisiones trazables, y una historia de cómo se construye algo real partiendo desde cero y sin atajos.

## Por qué es open source

No quiero que el trabajo acumulado aquí quede encerrado. Siento que puede ser útil para personas que están donde yo estaba hace un año: con ganas de construir algo serio, pero sin un referente concreto de cómo se ve un proyecto con criterio arquitectónico real.

Si eres estudiante: estudia las decisiones, no el código terminado.  
Si eres desarrollador: toma lo que sirva, mejora lo que falta.  
Si eres profesor: úsalo como material de discusión sobre DDD y arquitectura hexagonal.

Los ADRs documentan el estado actual y el razonamiento detrás de cada decisión. Los archivos en `evolution/` muestran los errores cometidos y las correcciones tomadas. Los commits reflejan el proceso de migración a hexagonal, aunque los primeros no son tan granulares —la experiencia con git también se construyó durante el proyecto.

## Agradecimientos

A quienes contribuyeron en este aprendizaje:

- **[Dev Dominio](https://youtube.com/@devdominio?si=mXfCLc6KwuGY6B27)** — por un curso que enseña el por qué, no solo el cómo.
- **[TodoCode / Ingeniera Lucina](https://youtube.com/playlist?list=PLQxX2eiEaqbzhvlMJZkyFoZpyo33T6rm7&si=9jdErvbPsPUYOHec)** — por el proyecto original del que esto evolucionó.
- **[Píldoras Informáticas](https://youtube.com/playlist?list=PLU8oAlHdN5BktAXdEVCLUYzvDyqRQJ2lk&si=Yb9NolQZVehFP8Tb)** — y a todos los que comparten conocimiento sin pedir nada a cambio.
- A quien me rechazó en ese hackathon — esa respuesta fue más útil que cualquier validación.

---

*"La arquitectura no es sobre frameworks, es sobre decisiones justificables."*