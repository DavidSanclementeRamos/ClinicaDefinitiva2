package com.example.ClinicaDefinitiva.domain.administration.accounting.valueObject;


/**
 * Value Object que representa un documento adjunto.
 * Inmutable y con validaciones de negocio.
 */
public final class Document {

    private static final int MAX_NAME_LENGTH = 255;
    private static final int MAX_URL_LENGTH = 500;
    private static final long MAX_FILE_SIZE = 10_485_760; // 10MB en bytes

    private final Name name;
    private final String url;
    private final String type;
    private final long size;

    public Document(Name name, String url, String type, long size) {
        validateUrl(url);
        validateType(type);
        validateSize(size);

        this.name = name;
        this.url = url.trim();
        this.type = type.trim().toUpperCase();
        this.size = size;
    }

    public static Document of(Name name, String url, String type, long size) {
        return new Document(name, url, type, size);
    }

    public static Document pdf(Name name, String url, long size) {
        return new Document(name, url, "PDF", size);
    }

    public static Document excel(Name name, String url, long size) {
        return new Document(name, url, "EXCEL", size);
    }

    public static Document image(Name name, String url, long size) {
        return new Document(name, url, "IMAGE", size);
    }

    public boolean isPdf() {
        return "PDF".equals(this.type);
    }

    public boolean isExcel() {
        return "EXCEL".equals(this.type);
    }

    public boolean isImage() {
        return "IMAGE".equals(this.type);
    }


    public String getSizeInMB() {
        return String.format("%.2f MB", size / 1_048_576.0);
    }


    private void validateUrl(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("La URL del documento es obligatoria");
        }
        if (url.trim().length() > MAX_URL_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("La URL no puede exceder %d caracteres", MAX_URL_LENGTH)
            );
        }
    }

    private void validateType(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio");
        }
    }

    private void validateSize(long size) {
        if (size <= 0) {
            throw new IllegalArgumentException("El tamaño debe ser mayor a cero");
        }
        if (size > MAX_FILE_SIZE) {
            throw new IllegalArgumentException(
                    String.format("El archivo no puede exceder %s", getSizeInMB())
            );
        }
    }

    // Getters
    public Name getName() { return name; }
    public String getUrl() { return url; }
    public String getType() { return type; }
    public long getSize() { return size; }


}
