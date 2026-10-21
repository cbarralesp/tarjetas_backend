package cl.ipss.tarjetas.service.impl;

import cl.ipss.tarjetas.dto.ActualizarTarjetaRequest;
import cl.ipss.tarjetas.dto.CrearTarjetaRequest;
import cl.ipss.tarjetas.dto.TarjetaResponse;
import cl.ipss.tarjetas.entity.TarjetaBip;
import cl.ipss.tarjetas.exception.NumeroTarjetaDuplicadoException;
import cl.ipss.tarjetas.exception.TarjetaNoEncontradaException;
import cl.ipss.tarjetas.mapper.TarjetaBipMapper;
import cl.ipss.tarjetas.repository.TarjetaBipRepository;
import cl.ipss.tarjetas.service.TarjetaBipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * SERVICE (implementación): aquí vive la lógica de negocio del CRUD.
 * Recibe el repository y el mapper por el constructor (inyección de dependencias).
 */
@Service
@Transactional
public class TarjetaBipServiceImpl implements TarjetaBipService {

    private static final Logger log = LoggerFactory.getLogger(TarjetaBipServiceImpl.class);

    private final TarjetaBipRepository repository;
    private final TarjetaBipMapper mapper;

    public TarjetaBipServiceImpl(TarjetaBipRepository repository, TarjetaBipMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TarjetaResponse> listar() {
        log.info("--- command receive to list cards ---");
        List<TarjetaResponse> tarjetas = repository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .map(mapper::aRespuesta)
                .toList();
        log.info("--- finish list cards: {} found ---", tarjetas.size());
        return tarjetas;
    }

    @Override
    @Transactional(readOnly = true)
    public TarjetaResponse obtener(Long id) {
        log.info("--- command receive to get card: {} ---", id);
        TarjetaResponse tarjeta = mapper.aRespuesta(buscar(id));
        log.info("--- finish get card: {} - {} ---", id, tarjeta.numero());
        return tarjeta;
    }

    @Override
    public TarjetaResponse crear(CrearTarjetaRequest request) {
        String numero = request.numero().trim();
        log.info("--- command receive to create card: {} ---", numero);
        if (repository.existsByNumero(numero)) {
            log.warn("--- card number already registered: {} ---", numero);
            throw new NumeroTarjetaDuplicadoException(numero);
        }
        TarjetaBip guardada = repository.save(mapper.aEntidad(request));
        log.info("--- finish create card: {} - id {} ---", numero, guardada.getId());
        return mapper.aRespuesta(guardada);
    }

    @Override
    public TarjetaResponse actualizar(Long id, ActualizarTarjetaRequest request) {
        log.info("--- command receive to update card: {} ---", id);
        TarjetaBip tarjeta = buscar(id);
        mapper.actualizarEntidad(tarjeta, request);
        TarjetaBip guardada = repository.save(tarjeta);
        log.info("--- finish update card: {} - {} ---", id, guardada.getNumero());
        return mapper.aRespuesta(guardada);
    }

    @Override
    public void eliminar(Long id) {
        log.info("--- command receive to delete card: {} ---", id);
        if (!repository.existsById(id)) {
            log.warn("--- card not found: {} ---", id);
            throw new TarjetaNoEncontradaException(id);
        }
        repository.deleteById(id);
        log.info("--- finish delete card: {} ---", id);
    }

    private TarjetaBip buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> {
            log.warn("--- card not found: {} ---", id);
            return new TarjetaNoEncontradaException(id);
        });
    }
}
