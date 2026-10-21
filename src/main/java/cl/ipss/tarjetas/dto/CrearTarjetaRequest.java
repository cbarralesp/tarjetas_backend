package cl.ipss.tarjetas.dto;

import cl.ipss.tarjetas.enums.TipoTarjeta;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/** DTO de entrada para POST /api/tarjetas. Las anotaciones validan los datos antes de llegar al service. */
public record CrearTarjetaRequest(
        @NotBlank(message = "El número es obligatorio")
        @Pattern(regexp = "\\d{8,10}", message = "El número debe tener entre 8 y 10 dígitos")
        String numero,

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
        BigDecimal saldo
) {
}
