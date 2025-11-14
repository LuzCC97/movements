package com.example.movements.movements;

import org.junit.jupiter.api.Test;

class MovementsApplicationTest {

    //carga el contexto de spring, carga todos los beans
    @Test
    void main_runs() {
        MovementsApplication.main(new String[]{}); // cubre MovementsApplication.java líneas 7–11
    }
}