package cl.ipss.tarjetas.mapper;

import cl.ipss.tarjetas.dto.ActualizarTarjetaRequest;
import cl.ipss.tarjetas.dto.CrearTarjetaRequest;
import cl.ipss.tarjetas.dto.TarjetaResponse;
import cl.ipss.tarjetas.entity.TarjetaBip;
import org.springframework.stereotype.Component;

/** Patrón Mapper: convierte entre DTOs (lo que viaja por HTTP) y la Entity (lo que se guarda). */
@Component
public class TarjetaBipMapper {

    public TarjetaBip aEntidad(CrearTarjetaRequest r) {
        return TarjetaBip.builder()
                .numero(r.numero().trim())
                .titular(r.titular().trim())
                .alias(limpiar(r.alias()))
                .tipo(r.tipo())
                .saldo(r.saldo())
                .build();
    }

    /** Copia los datos editables sobre una entidad existente (el número no se toca). */
    public void actualizarEntidad(TarjetaBip entidad, ActualizarTarjetaRequest r) {
        entidad.setTitular(r.titular().trim());
        entidad.setAlias(limpiar(r.alias()));
        entidad.setTipo(r.tipo());
        entidad.setSaldo(r.saldo());
        entidad.setEstado(r.estado());
    }

    public TarjetaResponse aRespuesta(TarjetaBip t) {
        return new TarjetaResponse(t.getId(), t.getNumero(), t.getTitular(), t.getAlias(), t.getTipo(),
                t.getSaldo(), t.getEstado(), t.getCreadaEn(), t.getActualizadaEn());
    }

    private String limpiar(String texto) {
        return (texto == null || texto.isBlank()) ? null : texto.trim();
    }
}
