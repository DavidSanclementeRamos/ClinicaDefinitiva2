
package com.example.ClinicaDefinitiva.domain.administration.authorization.vo;

import com.example.ClinicaDefinitiva.domain.administration.authorization.vo.ActionCatalog;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionCatalogTest {

    @Test
    void shouldCreateFromBasicAction() {
        ActionCatalog action = ActionCatalog.of(ActionCatalog.BasicAction.CREATE);
        assertEquals("CREATE", action.getCode());
    }

    @Test
    void shouldCreateCustomActionUppercased() {
        ActionCatalog action = ActionCatalog.custom("miAccion");
        assertEquals("MIACCION", action.getCode());
    }

    @Test
    void shouldBeEqualWhenCodesMatch() {
        ActionCatalog a1 = ActionCatalog.of(ActionCatalog.BasicAction.DELETE);
        ActionCatalog a2 = ActionCatalog.custom("delete");
        assertEquals(a1, a2);
        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenCodesDiffer() {
        ActionCatalog a1 = ActionCatalog.of(ActionCatalog.BasicAction.READ);
        ActionCatalog a2 = ActionCatalog.of(ActionCatalog.BasicAction.UPDATE);
        assertNotEquals(a1, a2);
    }
}

