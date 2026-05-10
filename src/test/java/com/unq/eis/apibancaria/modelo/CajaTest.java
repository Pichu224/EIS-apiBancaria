package com.unq.eis.apibancaria.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CajaTest {

    private Usuario usuario1;
    private Caja caja;

    @BeforeEach
    void setUp(){
        usuario1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        caja = new Caja(1L,"testCaja",usuario1);
    }

}
