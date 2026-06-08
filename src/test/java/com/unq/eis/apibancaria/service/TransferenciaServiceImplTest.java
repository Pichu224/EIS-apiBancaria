package com.unq.eis.apibancaria.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.unq.eis.apibancaria.exception.CajaInexistenteException;
import com.unq.eis.apibancaria.modelo.Caja;
import com.unq.eis.apibancaria.modelo.Transferencia;
import com.unq.eis.apibancaria.modelo.Usuario;
import com.unq.eis.apibancaria.persistence.CajaDAO;
import com.unq.eis.apibancaria.persistence.MovimientoDAO;
import com.unq.eis.apibancaria.persistence.TransferenciaDAO;
import com.unq.eis.apibancaria.service.impl.TransferenciaServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TransferenciaServiceImplTest {

    @Mock
    private TransferenciaDAO transferenciaDAO;

    @Mock
    private CajaDAO cajaDAO;

    @Mock
    private MovimientoDAO movimientoDAO;

    @InjectMocks
    private TransferenciaServiceImpl serviceTransferir;
    private Caja cajaTest1;
    private Caja cajaTest2;

    @BeforeEach
    void setUp(){
        Usuario usuarioTest1 = new Usuario();
        usuarioTest1.setEmail("nico@gmail.com");
        usuarioTest1.setContrasenia("1234");
        usuarioTest1.setNombre("Nicolas");
        usuarioTest1.setApellido("Vaccaro");
        usuarioTest1.setDni("40.123.456");

        Usuario usuarioTest2 = new Usuario();
        usuarioTest2.setEmail("mati@gmail.com");
        usuarioTest2.setContrasenia("456");
        usuarioTest2.setNombre("Matias");
        usuarioTest2.setApellido("Boldo");
        usuarioTest2.setDni("40.777.361");

        cajaTest1 = new Caja(100L, "Nico.Caja.bd", usuarioTest1);
        cajaTest1.setIdCaja(1L);
        cajaTest2 = new Caja(101L, "Mati.Caja.bd", usuarioTest2);
        cajaTest2.setIdCaja(2L);
    }

    @Test
    public void TransferirEntreCajasExitoso(){
        // Set initial balance for origin
        cajaTest1.depositar(BigDecimal.valueOf(1000L));

        // Mock DAOs: return the cajas when searched by id
        org.mockito.Mockito.when(cajaDAO.findById(cajaTest1.getIdCaja())).thenReturn(Optional.of(cajaTest1));
        org.mockito.Mockito.when(cajaDAO.findByAlias(cajaTest2.getAlias())).thenReturn(Optional.of(cajaTest2));

        // Mock save to assign an id and return the transferencia
        org.mockito.Mockito.when(transferenciaDAO.save(org.mockito.ArgumentMatchers.any(Transferencia.class)))
                .thenAnswer(invocation -> {
                    Transferencia t = invocation.getArgument(0);
                    t.setIdTransferencia(1L);
                    return t;
                });

        Transferencia transferencia = serviceTransferir.tranferir(cajaTest1.getIdCaja(), cajaTest2.getAlias(), BigDecimal.valueOf(500L));

        // Se valida como se "persistio" la transferencia (mock)
        assertNotNull(transferencia.getIdTransferencia());
        assertEquals(cajaTest1.getIdCaja(), transferencia.getCajaOrigen().getIdCaja());
        assertEquals(cajaTest2.getAlias(), transferencia.getCajaDestino().getAlias());
        assertEquals(0, transferencia.getMontoTotal().compareTo(BigDecimal.valueOf(500L)));
        assertNotNull(transferencia.getFechaRealizado());
        assertFalse(transferencia.getFechaRealizado().isAfter(LocalDateTime.now()));

        // Valido los valores de las cajas luego de la transferencia (objetos en memoria)
        assertEquals(0, cajaTest1.getSaldo().compareTo(BigDecimal.valueOf(500L)));
        assertEquals(0, cajaTest2.getSaldo().compareTo(BigDecimal.valueOf(500L)));

    }
    @Test
    public void ErrorTransferirConCajasSinId(){

        assertThrows(CajaInexistenteException.class, () -> serviceTransferir.tranferir(null, cajaTest1.getAlias(), BigDecimal.ONE));
        assertThrows(CajaInexistenteException.class, () -> serviceTransferir.tranferir(1L,null,BigDecimal.ONE));
    }
    @Test
    public void ErrorTransferirConCajasSinPersistir(){
        // Simular que las cajas no existen en la BD
        org.mockito.Mockito.when(cajaDAO.findById(org.mockito.ArgumentMatchers.anyLong())).thenReturn(Optional.empty());
        assertThrows(CajaInexistenteException.class, () -> serviceTransferir.tranferir(1L, cajaTest1.getAlias(), BigDecimal.ONE));
    }

}
