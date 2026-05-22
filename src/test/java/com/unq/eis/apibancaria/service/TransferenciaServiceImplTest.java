package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.ApibancariaApplication;
import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.IdNuloException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;
import com.unq.eis.apibancaria.service.impl.TransferenciaServiceImpl;
import com.unq.eis.apibancaria.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApibancariaApplication.class)
public class TransferenciaServiceImplTest {

    @Autowired
    private UsuarioServiceImpl serviceUsuario;

    @Autowired
    private CajaServiceImpl serviceCaja;

    @Autowired
    private TransferenciaServiceImpl serviceTransferir;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private CajaDAO cajaDAO;

    @Autowired
    private TransferenciaDAO transferenciaDAO;

    private Usuario usuarioTest1;
    private Usuario usuarioTest2;
    private Caja cajaTest1;
    private Caja cajaTest2;
    private Transferencia transferencia;

    @BeforeEach
    void setUp(){

        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        usuarioTest2 = new Usuario("mati@gmail.com","456","Matias","Boldo","40.777.361");

        cajaTest1 = new Caja(100L, "Nico.Caja.bd", usuarioTest1);
        cajaTest2 = new Caja(101L, "Mati.Caja.bd", usuarioTest2);
    }

    @Test
    public void TransferirEntreCajasExitoso(){
        serviceUsuario.crear(usuarioTest1);
        serviceUsuario.crear(usuarioTest2);
        serviceCaja.crear(cajaTest1);
        serviceCaja.crear(cajaTest2);

        serviceCaja.depositar(cajaTest1.getIdCaja(), BigDecimal.valueOf(1000L));

        transferencia = serviceTransferir.tranferir(cajaTest1.getIdCaja(), cajaTest2.getIdCaja(), BigDecimal.valueOf(500L));

        //Se valida como se persistio la tranferencia
        assertNotNull(transferencia.getIdTransferencia());
        assertEquals(cajaTest1.getIdCaja(), transferencia.getCajaOrigen().getIdCaja());
        assertEquals(cajaTest2.getIdCaja(), transferencia.getCajaDestino().getIdCaja());
        assertEquals(0, transferencia.getMontoTotal().compareTo(BigDecimal.valueOf(500L)));
        assertTrue(transferencia.getFechaRealizado().isBefore(LocalDateTime.now()));

        //Valido los valores de las cajas luego de la transferencia
        cajaTest1 = serviceCaja.recuperar(cajaTest1.getIdCaja());
        cajaTest2 = serviceCaja.recuperar(cajaTest2.getIdCaja());

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.valueOf(500L)));
        assertEquals(0, cajaTest2.getSaldo().compareTo(BigDecimal.valueOf(500L)));

    }
    @Test
    public void ErrorTransferirConCajasSinId(){

        assertThrows(IdNuloException.class, () ->{serviceTransferir.tranferir(null, 1L, BigDecimal.ONE);});
        assertThrows(IdNuloException.class, () ->{serviceTransferir.tranferir(1L,null,BigDecimal.ONE);});
    }
    @Test
    public void ErrorTransferirConCajasSinPersistir(){
        //No es necesario en ambos lados, ya que en ambos llama al mimso metodo para validar.
        assertThrows(CajaInexistenteException.class, () ->{serviceTransferir.tranferir(1L, 2L, BigDecimal.ONE);});
    }


    @AfterEach
    void cleanup() {
        transferenciaDAO.deleteAll();
        cajaDAO.deleteAll();
        usuarioDAO.deleteAll();
    }

}
