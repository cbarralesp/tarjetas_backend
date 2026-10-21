package cl.ipss.tarjetas.dto;

import cl.ipss.tarjetas.enums.EstadoTarjeta;
import cl.ipss.tarjetas.enums.TipoTarjeta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** DTO de salida: lo que viaja en el JSON hacia Angular (la "View" de MVC en una API REST). */
public record TarjetaResponse(
        Long id,
        String numero,
        String titular,
        String alias,
        TipoTarjeta tipo,
        BigDecimal saldo,
        EstadoTarjeta estado,
        LocalDateTime creadaEn,
        LocalDateTime actualizadaEn
) {
}
