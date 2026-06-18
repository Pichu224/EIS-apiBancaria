package com.unq.eis.apibancaria.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.unq.eis.apibancaria.exception.AliasYaExistenteException;
import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.exception.MontoInvalidoException;
import com.unq.eis.apibancaria.exception.NroCajaYaExistenteException;
import com.unq.eis.apibancaria.exception.SaldoInsuficienteException;
import com.unq.eis.apibancaria.exception.UsuarioInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.TipoCaja;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.UsuarioDAO;
import com.unq.eis.apibancaria.service.impl.CajaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CajaServiceImplTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private CajaDAO cajaDAO;

    @InjectMocks
    private CajaServiceImpl serviceCaja;

    private Usuario usuarioTest1;
    private Usuario usuarioTest2;
    private Caja cajaTest1;
    private Caja cajaTest2;

    @BeforeEach
    void setUp(){
        usuarioTest1 = new Usuario();
        usuarioTest1.setIdUsuario(1L);
        usuarioTest1.setEmail("nico@gmail.com");
        usuarioTest1.setContrasenia("1234");
        usuarioTest1.setNombre("Nicolas");
        usuarioTest1.setApellido("Vaccaro");
        usuarioTest1.setDni("40.123.456");

        usuarioTest2 = new Usuario();
        usuarioTest2.setEmail("mati@gmail.com");
        usuarioTest2.setContrasenia("4567");
        usuarioTest2.setNombre("Matias");
        usuarioTest2.setApellido("Vaccaro");
        usuarioTest2.setDni("40.777.361");

        cajaTest1 = new Caja(100L, "Nico.Caja.bd", usuarioTest1);
        cajaTest2 = new Caja(101L, "Mati.Caja.bd", usuarioTest2);
    }

    @Test
    public void CrearCajaConUnUsuarioPersistido(){
        usuarioTest1.setIdUsuario(1L);
        cajaTest1.setIdCaja(1L);

        when(usuarioDAO.existsById(usuarioTest1.getIdUsuario())).thenReturn(true);
        when(cajaDAO.existsByNroCaja(anyLong())).thenReturn(false);
        when(cajaDAO.existsByAlias(anyString())).thenReturn(false);
        when(cajaDAO.save(any(Caja.class))).thenAnswer(inv -> {
            Caja c = inv.getArgument(0);
            c.setIdCaja(1L);
            return c;
        });
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));

        serviceCaja.crear(cajaTest1);

        Caja cajaTest3 = serviceCaja.recuperar(cajaTest1.getIdCaja());

        assertEquals(cajaTest1.getNroCaja(), cajaTest3.getNroCaja());
        assertEquals(cajaTest1.getAlias(), cajaTest3.getAlias());
        assertEquals(0, cajaTest1.getSaldo().compareTo(cajaTest3.getSaldo()));
        assertEquals(cajaTest1.getTipoCaja(), cajaTest3.getTipoCaja());
        assertEquals(cajaTest1.getUsuario().getEmail(), cajaTest3.getUsuario().getEmail());
    }

    @Test
    public void ErrorCrearCajaConUsuarioNoPersistido(){
        // usuario sin id -> debe lanzar UsuarioInexistenteException
        assertThrows(UsuarioInexistenteException.class, () -> serviceCaja.crear(cajaTest2));
    }

    @Test
    public void ErrorCrearCajaConUsuarioConIdSinPersistir(){
        usuarioTest2.setIdUsuario(1L);
        cajaTest2.setUsuario(usuarioTest2);
        when(usuarioDAO.existsById(1L)).thenReturn(false);
        assertThrows(UsuarioInexistenteException.class, () -> serviceCaja.crear(cajaTest2));
    }

    @Test
    public void ErrorCrearCajaConNroCajaYaExistente(){
        usuarioTest1.setIdUsuario(1L);
        cajaTest1.setIdCaja(1L);
        cajaTest2.setUsuario(usuarioTest1);

        when(usuarioDAO.existsById(1L)).thenReturn(true);
        when(cajaDAO.existsByNroCaja(cajaTest2.getNroCaja())).thenReturn(true);

        assertThrows(NroCajaYaExistenteException.class, () -> serviceCaja.crear(cajaTest2));
    }

    @Test
    public void ErrorCrearCajaConAliasYaExistente(){
        usuarioTest1.setIdUsuario(1L);
        cajaTest1.setIdCaja(1L);
        cajaTest2.setUsuario(usuarioTest1);

        when(usuarioDAO.existsById(1L)).thenReturn(true);
        when(cajaDAO.existsByNroCaja(anyLong())).thenReturn(false);
        when(cajaDAO.existsByAlias(cajaTest2.getAlias())).thenReturn(true);

        assertThrows(AliasYaExistenteException.class, () -> serviceCaja.crear(cajaTest2));
    }

    @Test
    public void ErrorRecuperarCajaSinPersistir(){
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.recuperar(cajaTest1.getIdCaja()));
    }

    @Test
    public void ErrorRecuperarCajaConIdSinPersistir(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.recuperar(1L));
    }

    @Test
    public void ActualizarDatosDeUnaCaja(){
        // Original persisted object
        Usuario u = usuarioTest1;
        Caja original = new Caja(100L, "Nico.Caja.bd", u);
        original.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(original));
        when(cajaDAO.existsByNroCaja(200L)).thenReturn(false);
        when(cajaDAO.existsByAlias("nico.vaqui.api")).thenReturn(false);

        // New values to update
        Caja updated = new Caja(200L, "nico.vaqui.api", u);
        updated.setTipoCaja(TipoCaja.CajaCorriente);

        serviceCaja.actualizar(1L, updated);

        // Assert original has been updated in place
        assertEquals(updated.getNroCaja(), original.getNroCaja());
        assertEquals(updated.getAlias(), original.getAlias());
        assertEquals(updated.getTipoCaja(), original.getTipoCaja());
    }

    @Test
    public void ErrorActualizarCajaSinIdYConIdInexistente(){
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.actualizar(null, cajaTest1));
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.actualizar(1L, cajaTest1));
    }

    @Test
    public void ErrorActualizarCajaConNroCajaExistentePorOtraCaja(){
        Usuario u = usuarioTest1;
        Caja original = new Caja(100L, "Nico.Caja.bd", u);
        original.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(original));
        when(cajaDAO.existsByNroCaja(cajaTest2.getNroCaja())).thenReturn(true);

        Caja updated = new Caja(cajaTest2.getNroCaja(), original.getAlias(), u);

        assertThrows(NroCajaYaExistenteException.class, () -> serviceCaja.actualizar(1L, updated));
    }

    @Test
    public void ErrorActualizarCajaConAliasExistentePorOtraCaja(){
        Usuario u = usuarioTest1;
        Caja original = new Caja(100L, "Nico.Caja.bd", u);
        original.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(original));
        when(cajaDAO.existsByAlias(cajaTest2.getAlias())).thenReturn(true);

        Caja updated = new Caja(original.getNroCaja(), cajaTest2.getAlias(), u);

        assertThrows(AliasYaExistenteException.class, () -> serviceCaja.actualizar(1L, updated));
    }

    @Test
    public void EliminarUnaCajaPersistida(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));
        serviceCaja.eliminar(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.recuperar(1L));
    }

    @Test
    public void ErrorEliminarCajaNoPersistidaYSinId(){
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.eliminar(null));
        cajaTest1.setIdCaja(1L);
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.eliminar(1L));
    }

    @Test
    public void DepositarDineroEnCuentaExitoso(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.ZERO));

        serviceCaja.depositar(1L, BigDecimal.valueOf(100L));

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.valueOf(100L)));
    }

    @Test
    public void ErrorDepositarDineroConMontoMenorEIgualACero(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.ZERO));

        assertThrows(MontoInvalidoException.class, () -> serviceCaja.depositar(1L, BigDecimal.ZERO));
        assertThrows(MontoInvalidoException.class, () -> serviceCaja.depositar(1L, BigDecimal.valueOf(-1L)));
    }

    @Test
    public void ErrorDepositarDineroConCajaSinId(){
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.depositar(null, BigDecimal.ONE));
    }

    @Test
    public void ErrorDepositarDineroConCajaConIdNoPersistido(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CajaInexistenteException.class, () -> serviceCaja.depositar(1L, BigDecimal.ONE));
    }

    @Test
    public void RetirarDineroDeCajaExitoso(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));

        serviceCaja.depositar(1L, BigDecimal.valueOf(100L));
        serviceCaja.retirar(1L, BigDecimal.valueOf(50L));

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.valueOf(50L)));
    }

    @Test
    public void ErrorRetirarDineroDeCajaConMontoMenorEIgualACERO(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));

        assertThrows(MontoInvalidoException.class, () -> serviceCaja.retirar(1L, BigDecimal.ZERO));
        assertThrows(MontoInvalidoException.class, () -> serviceCaja.retirar(1L, BigDecimal.valueOf(-1L)));
    }

    @Test
    public void ErrorRetirarDineroDeCajaConSaldoInsuficiente(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.of(cajaTest1));

        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.ZERO));

        assertThrows(SaldoInsuficienteException.class, () -> serviceCaja.retirar(1L, BigDecimal.ONE));
    }

    @Test
    public void ErrorRetirarDineroConCajaSinId(){
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.retirar(null, BigDecimal.ONE));
    }

    @Test
    public void ErrorRetirarDineroConCajaConIdSinPersistir(){
        cajaTest1.setIdCaja(1L);
        when(cajaDAO.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CajaInexistenteException.class, () -> serviceCaja.retirar(1L, BigDecimal.ONE));
    }

    @Test
    public void recuperarCajasDeUsuarioRetornaLasCajasDelUsuario() {

        Long idUsuario = 1L;

        List<Caja> cajas = List.of(cajaTest1, cajaTest2);

        when(usuarioDAO.existsById(idUsuario))
                .thenReturn(true);

        when(cajaDAO.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(cajas);

        List<Caja> resultado = serviceCaja.recuperarCajasdeUsuario(idUsuario);

        assertEquals(2, resultado.size());
        assertEquals(cajas, resultado);

        verify(usuarioDAO).existsById(idUsuario);
        verify(cajaDAO).findByUsuario_IdUsuario(idUsuario);
    }

    @Test
    public void recuperarCajasDeUsuarioFallaCuandoUsuarioNoExiste() {

        Long idUsuario = 1L;

        when(usuarioDAO.existsById(idUsuario))
                .thenReturn(false);

        UsuarioInexistenteException exception = assertThrows(UsuarioInexistenteException.class, () -> serviceCaja.recuperarCajasdeUsuario(idUsuario));

        assertEquals("El usuario no existe!", exception.getMessage());

        verify(usuarioDAO).existsById(idUsuario);

        verify(cajaDAO, never()).findByUsuario_IdUsuario(anyLong());
    }
}
