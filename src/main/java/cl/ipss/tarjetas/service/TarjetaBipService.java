package cl.ipss.tarjetas.service;

import cl.ipss.tarjetas.dto.ActualizarTarjetaRequest;
import cl.ipss.tarjetas.dto.CrearTarjetaRequest;
import cl.ipss.tarjetas.dto.TarjetaResponse;

import java.util.List;

/**
 * SERVICE (interfaz): define QUÉ operaciones de negocio existen.
 * El controller depende de esta interfaz; la lógica está en TarjetaBipServiceImpl.
 */
public interface TarjetaBipService {

    List<TarjetaResponse> listar();

    TarjetaResponse obtener(Long id);

    TarjetaResponse crear(CrearTarjetaRequest request);

    TarjetaResponse actualizar(Long id, ActualizarTarjetaRequest request);

    void eliminar(Long id);
}
