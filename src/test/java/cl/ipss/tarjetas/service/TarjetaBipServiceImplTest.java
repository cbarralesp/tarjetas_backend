package cl.ipss.tarjetas.service;

import cl.ipss.tarjetas.dto.ActualizarTarjetaRequest;
import cl.ipss.tarjetas.dto.CrearTarjetaRequest;
import cl.ipss.tarjetas.dto.TarjetaResponse;
import cl.ipss.tarjetas.entity.TarjetaBip;
import cl.ipss.tarjetas.enums.EstadoTarjeta;
import cl.ipss.tarjetas.enums.TipoTarjeta;
import cl.ipss.tarjetas.exception.NumeroTarjetaDuplicadoException;
import cl.ipss.tarjetas.exception.TarjetaNoEncontradaException;
import cl.ipss.tarjetas.mapper.TarjetaBipMapper;
import cl.ipss.tarjetas.repository.TarjetaBipRepository;
import cl.ipss.tarjetas.service.impl.TarjetaBipServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test unitario del service: el repository se "simula" con Mockito,
 * así no hace falta levantar Spring ni PostgreSQL.
 */
@ExtendWith(MockitoExtension.class)
class TarjetaBipServiceImplTest {

    @Mock
    private TarjetaBipRepository repository;

    private TarjetaBipServiceImpl service;

    @BeforeEach
    void preparar() {
        service = new TarjetaBipServiceImpl(repository, new TarjetaBipMapper());
    }

    private CrearTarjetaRequest crearRequest() {
        return new CrearTarjetaRequest("12345678", " Ana ", "  ", TipoTarjeta.NORMAL, new BigDecimal("2000"));
    }

    @Test
    void creaTarjetaLimpiandoDatosYQuedaActiva() {
        when(repository.existsByNumero("12345678")).thenReturn(false);
        when(repository.save(any(TarjetaBip.class))).thenAnswer(inv -> {
            TarjetaBip t = inv.getArgument(0);
            ReflectionTestUtils.setField(t, "id", 1L); // la BD real asigna el id
            t.setEstado(EstadoTarjeta.ACTIVA); // lo hace @PrePersist en la BD real
            return t;
        });

        TarjetaResponse r = service.crear(crearRequest());

        assertEquals(1L, r.id());
        assertEquals("Ana", r.titular());
        assertNull(r.alias());
        assertEquals(EstadoTarjeta.ACTIVA, r.estado());
    }

    @Test
    void noPermiteNumeroDuplicado() {
        when(repository.existsByNumero("12345678")).thenReturn(true);

        assertThrows(NumeroTarjetaDuplicadoException.class, () -> service.crear(crearRequest()));
        verify(repository, never()).save(any());
    }

    @Test
    void obtenerLanzaErrorSiNoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TarjetaNoEncontradaException.class, () -> service.obtener(99L));
    }

    @Test
    void actualizaSinCambiarElNumero() {
        TarjetaBip existente = new TarjetaBip();
        existente.setNumero("12345678");
        existente.setTitular("Ana");
        existente.setTipo(TipoTarjeta.NORMAL);
        existente.setSaldo(BigDecimal.TEN);
        existente.setEstado(EstadoTarjeta.ACTIVA);
        when(repository.findById(5L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        TarjetaResponse r = service.actualizar(5L, new ActualizarTarjetaRequest(
                "Ana Díaz", "Casa", TipoTarjeta.TNE_SUPERIOR, new BigDecimal("500"), EstadoTarjeta.BLOQUEADA));

        assertEquals("12345678", r.numero());
        assertEquals("Ana Díaz", r.titular());
        assertEquals(EstadoTarjeta.BLOQUEADA, r.estado());
    }

    @Test
    void eliminaSoloSiExiste() {
        when(repository.existsById(7L)).thenReturn(true);
        when(repository.existsById(8L)).thenReturn(false);

        service.eliminar(7L);

        verify(repository).deleteById(7L);
        assertThrows(TarjetaNoEncontradaException.class, () -> service.eliminar(8L));
    }
}
