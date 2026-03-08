
package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ResourceCatalog;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResourceCatalogTest {

    @Test
    void shouldCreateFromBasicResource() {
        ResourceCatalog resource = ResourceCatalog.of(ResourceCatalog.BasicResource.DENTIST);
        assertEquals("DENTIST", resource.getCode());
    }

    @Test
    void shouldCreateCustomResourceUppercased() {
        ResourceCatalog resource = ResourceCatalog.custom("clinica");
        assertEquals("CLINICA", resource.getCode());
    }

    @Test
    void shouldBeEqualWhenCodesMatch() {
        ResourceCatalog r1 = ResourceCatalog.of(ResourceCatalog.BasicResource.PATIENT);
        ResourceCatalog r2 = ResourceCatalog.custom("patient");
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenCodesDiffer() {
        ResourceCatalog r1 = ResourceCatalog.of(ResourceCatalog.BasicResource.INVOICE);
        ResourceCatalog r2 = ResourceCatalog.of(ResourceCatalog.BasicResource.PAYMENT);
        assertNotEquals(r1, r2);
    }
}

