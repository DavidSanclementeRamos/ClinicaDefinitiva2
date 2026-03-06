package com.example.ClinicaDefinitiva.domain.administration.accounting.vo;

import com.example.ClinicaDefinitiva.domain.errors.catalog.adminitration.accounting.VoAccountingError;
import com.example.ClinicaDefinitiva.domain.errors.context.VOContext;
import com.example.ClinicaDefinitiva.domain.exceptionsDomain.ValueObjectValidationException;

public final class Document {

    private static final int MAX_NAME_LENGTH = 255;
    private static final int MAX_URL_LENGTH = 500;
    private static final long MAX_FILE_SIZE = 10_485_760; // 10MB

    private final String name;
    private final String url;
    private final String type;
    private final long size;

    private Document(String name, String url, String type, long size) {
        if (name == null || name.isBlank()) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_DOCUMENT_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (url == null || url.isBlank()) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_DOCUMENT_NULL,
                    VOContext.ACCOUNTING
            );
        }
        if (url.length() > MAX_URL_LENGTH) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_DOCUMENT_INVALID_FORMAT,
                    VOContext.ACCOUNTING
            );
        }
        if (size <= 0 || size > MAX_FILE_SIZE) {
            throw new ValueObjectValidationException(
                    VoAccountingError.ERR_DOCUMENT_INVALID_FORMAT,
                    VOContext.ACCOUNTING
            );
        }
        this.name = name.trim();
        this.url = url.trim();
        this.type = type.trim().toUpperCase();
        this.size = size;
    }

    public static Document of(String name, String url, String type, long size) {
        return new Document(name, url, type, size);
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public String getType() { return type; }
    public long getSize() { return size; }
}
