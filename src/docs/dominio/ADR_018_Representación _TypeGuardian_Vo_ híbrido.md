## ADR-14 (Dominio): Representación de TypeGuardian como Value Object híbrido

- Estado: Aceptado
- Fecha: 2025-10-07
- Autor: David

## Contexto
Inicialmente, el atributo typeGuardian del agregado Guardian fue modelado como un enum fijo (MAMA, PAPA, HERMANO).  
Esto resolvía los casos de familia nuclear, pero presentaba limitaciones:
- Dificultad para extender con nuevos roles (ej. Tutor Legal, Acudiente Institucional).
- Problemas de internacionalización (mostrar “MOTHER” vs “MAMÁ”).
- Falta de trazabilidad semántica: el enum no encapsula reglas ni validaciones.

## Decisión
Se adopta un Value Object híbrido (TypeGuardian) con las siguientes características:
- Instancias estáticas predefinidas para familia nuclear (MAMA, PAPA, HERMANO).
- Método de fábrica of(String codigo, String descripcion) para crear variantes dinámicas.
- Igualdad semántica basada en codigo.
- Preparado para internacionalización y catálogos externos.

## Alternativas consideradas
- Mantener enum puro: simple, pero inflexible y poco internacionalizable.
- VO sin instancias estáticas: flexible, pero perdería la claridad de roles familiares comunes.
- Tabla de base de datos: demasiado costoso para este nivel de modelado.

## Consecuencias
- Mayor coherencia semántica en el dominio.
- Posibilidad de extender sin recompilar.
- Internacionalización soportada de forma natural.
- Ligero aumento de complejidad en comparación con enum.

## Plan de implementación
1. Crear clase TypeGuardian en com.clinica.domain.vo.guardian.
2. Definir instancias estáticas (MAMA, PAPA, HERMANO).
3. Implementar método de fábrica of(String codigo, String descripcion).
4. Implementar equals y hashCode basados en codigo.
5. Integrar con catálogos externos para internacionalización.
6. Documentar reglas en docs/dominio/reglas-de-negocio/guardian.md.

## Ejemplo
```java
public final class TypeGuardian {
private final String codigo;
private final String descripcion;

    public static final TypeGuardian MAMA = new TypeGuardian("MAMA", "Madre");
    public static final TypeGuardian PAPA = new TypeGuardian("PAPA", "Padre");
    public static final TypeGuardian HERMANO = new TypeGuardian("HERMANO", "Hermano");

    private TypeGuardian(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public static TypeGuardian of(String codigo, String descripcion) {
        return new TypeGuardian(codigo, descripcion);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TypeGuardian)) return false;
        TypeGuardian that = (TypeGuardian) o;
        return codigo.equals(that.codigo);
    }

    @Override
    public int hashCode() {
        return codigo.hashCode();
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
}
```

## Relación con otros ADR
- ADR-02 (Dominio): Uso sistemático de Value Objects.
- ADR-09 (Dominio): Separación de Value Objects para el estado de User y Dentist.
- ADR-11 (Dominio): Uso meticuloso de excepciones personalizadas.
- ADR-01 (Arquitectura): Migración a arquitectura hexagonal.  
  

