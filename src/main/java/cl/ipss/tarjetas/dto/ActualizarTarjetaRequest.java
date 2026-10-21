package cl.ipss.tarjetas.dto;

import cl.ipss.tarjetas.enums.EstadoTarjeta;
import cl.ipss.tarjetas.enums.TipoTarjeta;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** DTO de entrada para PUT /api/tarjetas/{id}. El número de tarjeta no se puede modificar. */
public record ActualizarTarjetaRequest(
        @NotBlank(message = "El titular es obligatorio")
        @Size(max = 100, message = "El titular no puede superar los 100 caracteres")
        String titular,

        @Size(max = 50, message = "El alias no puede superar los 50 caracteres")
        String alias,

        @NotNull(message = "El tipo es obligatorio")
        TipoTarjeta tipo,

        @NotNull(message = "El saldo es obligatorio")
        @DecimalMin(value = "0", message = "El saldo no puede ser negativo")
        @DecimalMax(value = "100000", message = "El saldo no puede superar 100000")
        BigDecimal saldo,

        @NotNull(message = "El estado es obligatorio")
        EstadoTarjeta estado
) {
}
