package cl.ipss.tarjetas.controller;

import cl.ipss.tarjetas.dto.TarjetaResponse;
import cl.ipss.tarjetas.enums.EstadoTarjeta;
import cl.ipss.tarjetas.enums.TipoTarjeta;
import cl.ipss.tarjetas.exception.NumeroTarjetaDuplicadoException;
import cl.ipss.tarjetas.exception.TarjetaNoEncontradaException;
import cl.ipss.tarjetas.service.TarjetaBipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/** Prueba solo la capa web (rutas, validaciones y códigos HTTP) con el service simulado. */
@WebMvcTest(TarjetaBipController.class)
class TarjetaBipControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TarjetaBipService service;

    private TarjetaResponse ejemplo() {
        LocalDateTime ahora = LocalDateTime.now();
        return new TarjetaResponse(1L, "12345678", "Ana", null, TipoTarjeta.NORMAL,
                new BigDecimal("1000"), EstadoTarjeta.ACTIVA, ahora, ahora);
    }

    private static final String JSON_VALIDO =
            "{\"numero\":\"12345678\",\"titular\":\"Ana\",\"tipo\":\"NORMAL\",\"saldo\":1000}";

    @Test
    void listaTarjetas() throws Exception {
        when(service.listar()).thenReturn(List.of(ejemplo()));

        mvc.perform(get("/api/tarjetas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value("12345678"));
    }

    @Test
    void creaTarjetaDevuelve201() throws Exception {
        when(service.crear(any())).thenReturn(ejemplo());

        mvc.perform(post("/api/tarjetas").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void numeroInvalidoDevuelve400ConDetalle() throws Exception {
        String json = "{\"numero\":\"12\",\"titular\":\"Ana\",\"tipo\":\"NORMAL\",\"saldo\":1000}";

        mvc.perform(post("/api/tarjetas").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detalles[0]").value("numero: El número debe tener entre 8 y 10 dígitos"));
    }

    @Test
    void numeroDuplicadoDevuelve409() throws Exception {
        when(service.crear(any())).thenThrow(new NumeroTarjetaDuplicadoException("12345678"));

        mvc.perform(post("/api/tarjetas").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.mensaje").value("Ya existe una tarjeta registrada con el número 12345678"));
    }

    @Test
    void tarjetaInexistenteDevuelve404() throws Exception {
        when(service.obtener(99L)).thenThrow(new TarjetaNoEncontradaException(99L));

        mvc.perform(get("/api/tarjetas/99")).andExpect(status().isNotFound());
    }
}
