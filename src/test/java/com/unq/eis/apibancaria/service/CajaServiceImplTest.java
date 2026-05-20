package com.unq.eis.apibancaria.service;

import com.unq.eis.apibancaria.ApibancariaApplication;
import com.unq.eis.apibancaria.exception.*;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.TipoCaja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;
import com.unq.eis.apibancaria.service.impl.UsuarioServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = ApibancariaApplication.class)
public class CajaServiceImplTest {

    @Autowired
    private UsuarioServiceImpl serviceUsuario;

    @Autowired
    private CajaServiceImpl serviceCaja;

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private CajaDAO cajaDAO;

    private Usuario usuarioTest1;
    private Usuario usuarioTest2;
    private Caja cajaTest1;
    private Caja cajaTest2;
    private Caja cajaTest3;

    @BeforeEach
    void setUp(){

        usuarioTest1 = new Usuario("nico@gmail.com","123","Nicolas","Vaccaro","40.123.456");
        usuarioTest2 = new Usuario("mati@gmail.com","456","Matias","Vaccaro","40.777.361");

        cajaTest1 = new Caja(100L, "Nico.Caja.bd", usuarioTest1);
        cajaTest2 = new Caja(101L, "Mati.Caja.bd", usuarioTest2);
    }

    @Test
    @Transactional // Esta etiqueta solo usarla cuando queramos manipular los objetos relacionados a la clase, en este caso el usuario de la caja.
    public void CrearCajaConUnUsuarioPersistido(){

        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        cajaTest3 = serviceCaja.recuperar(cajaTest1.getIdCaja());

        assertEquals(cajaTest1.getNroCaja(), cajaTest3.getNroCaja());
        assertEquals(cajaTest1.getAlias(), cajaTest3.getAlias());
        assertEquals(0, cajaTest1.getSaldo().compareTo(cajaTest3.getSaldo()));
        assertEquals(cajaTest1.getTipoCaja(), cajaTest3.getTipoCaja());
        assertEquals(cajaTest1.getUsuario().getEmail(), cajaTest3.getUsuario().getEmail());

    }
    @Test
    public void ErrorCrearCajaConUsuarioNoPersistido(){

        assertThrows(UsuarioInexistenteException.class, () -> {serviceCaja.crear(cajaTest2);});

    }
    @Test
    public void ErrorCrearCajaConUsuarioConIdSinPersistir(){

        usuarioTest2.setIdUsuario(1L);

        assertThrows(UsuarioInexistenteException.class, () -> {serviceCaja.crear(cajaTest2);});
    }

    @Test
    public void ErrorCrearCajaConNroCajaYaExistente(){

        serviceUsuario.crear(usuarioTest1);
        serviceUsuario.crear(usuarioTest2);
        serviceCaja.crear(cajaTest1);

        cajaTest2.setNroCaja(cajaTest1.getNroCaja());

        assertThrows(NroCajaYaExistenteException.class, ()->{serviceCaja.crear(cajaTest2);});
    }

    @Test
    public void ErrorCrearCajaConAliasYaExistente(){

        serviceUsuario.crear(usuarioTest1);
        serviceUsuario.crear(usuarioTest2);
        serviceCaja.crear(cajaTest1);

        cajaTest2.setAlias(cajaTest1.getAlias());

        assertThrows(AliasYaExistenteException.class, ()->{serviceCaja.crear(cajaTest2);});
    }

    @Test
    public void ErrorRecuperarCajaSinPersistir(){

        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.recuperar(cajaTest1.getIdCaja());});
    }

    @Test
    public void ErrorRecuperarCajaConIdSinPersistir(){
        cajaTest1.setIdCaja(1L);
        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.recuperar(cajaTest1.getIdCaja());});
    }

    @Test
    public void ActualizarDatosDeUnaCaja(){

        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        cajaTest1.setNroCaja(200L);
        cajaTest1.setAlias("nico.vaqui.api");
        cajaTest1.setTipoCaja(TipoCaja.CajaCorriente);

        serviceCaja.actualizar(cajaTest1.getIdCaja(),cajaTest1);

        cajaTest3 = serviceCaja.recuperar(cajaTest1.getIdCaja());

        assertEquals(cajaTest1.getNroCaja(), cajaTest3.getNroCaja());
        assertEquals(cajaTest1.getAlias(), cajaTest3.getAlias());
        assertEquals(cajaTest1.getTipoCaja(), cajaTest3.getTipoCaja());

    }

    @Test
    public void ErrorActualizarCajaSinIdYConIdInexistente(){

        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.actualizar(null,cajaTest1);});
        cajaTest1.setIdCaja(1L);
        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.actualizar(1L,cajaTest1);});
    }

    @Test
    public void ErrorActualizarCajaConNroCajaExistentePorOtraCaja(){

        serviceUsuario.crear(usuarioTest1);
        serviceUsuario.crear(usuarioTest2);
        serviceCaja.crear(cajaTest1);
        serviceCaja.crear(cajaTest2);

        cajaTest1.setNroCaja(cajaTest2.getNroCaja());

        assertThrows(NroCajaYaExistenteException.class, () -> {serviceCaja.actualizar(cajaTest1.getIdCaja(),cajaTest1);});

    }

    @Test
    public void ErrorActualizarCajaConAliasExistentePorOtraCaja(){

        serviceUsuario.crear(usuarioTest1);
        serviceUsuario.crear(usuarioTest2);
        serviceCaja.crear(cajaTest1);
        serviceCaja.crear(cajaTest2);

        cajaTest1.setAlias(cajaTest2.getAlias());

        assertThrows(AliasYaExistenteException.class, () -> {serviceCaja.actualizar(cajaTest1.getIdCaja(),cajaTest1);});
    }

    @Test
    public void EliminarUnaCajaPersistida(){
        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        serviceCaja.eliminar(cajaTest1.getIdCaja());

        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.recuperar(cajaTest1.getIdCaja());});
    }

    @Test
    public void ErrorEliminarCajaNoPersistidaYSinId(){

        assertThrows(CajaInexistenteException.class, () ->{serviceCaja.eliminar(null);});
        cajaTest1.setIdCaja(1L);
        assertThrows(CajaInexistenteException.class, () ->{serviceCaja.eliminar(cajaTest1.getIdCaja());});
    }

    @Test
    public void DepositarDineroEnCuentaExitoso(){
        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.ZERO));

        serviceCaja.depositar(cajaTest1.getIdCaja(), BigDecimal.valueOf(100L));

        cajaTest2 = serviceCaja.recuperar(cajaTest1.getIdCaja());

        assertEquals(0, cajaTest2.getSaldo().compareTo(BigDecimal.valueOf(100L)));

    }

    @Test
    public void ErrorDepositarDineroConMontoMenorEIgualACero(){
        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.ZERO));

        assertThrows(MontoInvalidoException.class, () -> {serviceCaja.depositar(cajaTest1.getIdCaja(),BigDecimal.ZERO);});
        assertThrows(MontoInvalidoException.class, () -> {serviceCaja.depositar(cajaTest1.getIdCaja(),BigDecimal.valueOf(-1L));});

    }

    @Test
    public void ErrorDepositarDineroConCajaSinId(){

        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.depositar(null, BigDecimal.ONE);});
    }

    @Test
    public void ErrorDepositarDineroConCajaConIdNoPersistido(){

        cajaTest1.setNroCaja(1L);

        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.depositar(null, BigDecimal.ONE);});
    }

    @Test
    public void RetirarDineroDeCajaExitoso(){

        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        serviceCaja.depositar(cajaTest1.getIdCaja(), BigDecimal.valueOf(100L));

        serviceCaja.retirar(cajaTest1.getIdCaja(), BigDecimal.valueOf(50L));

        cajaTest2 = serviceCaja.recuperar(cajaTest1.getIdCaja());

        assertEquals(0, cajaTest2.getSaldo().compareTo(BigDecimal.valueOf(50L)));
    }

    @Test
    public void ErrorRetirarDineroDeCajaConMontoMenorEIgualACero(){
        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        assertThrows(MontoInvalidoException.class, () -> {serviceCaja.retirar(cajaTest1.getIdCaja(),BigDecimal.ZERO);});
        assertThrows(MontoInvalidoException.class, () -> {serviceCaja.retirar(cajaTest1.getIdCaja(),BigDecimal.valueOf(-1L));});
    }

    @Test
    public void ErrorRetirarDineroDeCajaConSaldoInsuficiente(){

        serviceUsuario.crear(usuarioTest1);
        serviceCaja.crear(cajaTest1);

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.ZERO));

        assertThrows(SaldoInsuficienteException.class, () -> {serviceCaja.retirar(cajaTest1.getIdCaja(),BigDecimal.ONE);});
    }

    @Test
    public void ErrorRetirarDineroConCajaSinId(){

        assertThrows(CajaInexistenteException.class, () -> {serviceCaja.retirar(null,BigDecimal.ONE);});
    }

    @Test
    public void ErrorRetirarDineroConCajaConIdSinPersistir(){

        cajaTest1.setIdCaja(1L);

        assertThrows(CajaInexistenteException.class, () ->{serviceCaja.retirar(cajaTest1.getIdCaja(), BigDecimal.ONE);});
    }
    @AfterEach
    void cleanup() {
        cajaDAO.deleteAll();
        usuarioDAO.deleteAll();
    }

}
