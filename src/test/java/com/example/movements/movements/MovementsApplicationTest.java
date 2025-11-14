package com.example.movements.movements;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MovementsApplicationTest {

    //carga el contexto de spring, carga todos los beans
    //Llama a MovementsApplication.main(...) para cubrir el arranque de la app.
    //Que el metodo main se ejecuta sin lanzar errores.
    @Test
    void main_runs() {
        assertDoesNotThrow(() -> MovementsApplication.main(new String[]{})); // cubre MovementsApplication.java líneas 7–11
    }
}