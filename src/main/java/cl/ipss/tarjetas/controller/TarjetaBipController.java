package cl.ipss.tarjetas.controller;

import cl.ipss.tarjetas.dto.ActualizarTarjetaRequest;
import cl.ipss.tarjetas.dto.CrearTarjetaRequest;
import cl.ipss.tarjetas.dto.TarjetaResponse;
import cl.ipss.tarjetas.service.TarjetaBipService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * CONTROLLER (la "C" de MVC): recibe la petición HTTP, llama al service y devuelve la respuesta.
 * No contiene lógica de negocio. Las anotaciones @Tag/@Operation documentan la API en Swagger.
 * Cada endpoint registra en info cuándo empieza, la respuesta y cuándo termina (igual que CDA).
 */
@RestController
@RequestMapping("/api/tarjetas")
@Tag(name = "Tarjetas Bip!", description = "CRUD de tarjetas Bip!")
public class TarjetaBipController {

    private static final Logger log = LoggerFactory.getLogger(TarjetaBipController.class);

    private final TarjetaBipService service;

    public TarjetaBipController(TarjetaBipService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas las tarjetas")
    public List<TarjetaResponse> listar(HttpServletRequest requestServlet) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        List<TarjetaResponse> respuesta = service.listar();
        log.info("--- response list cards: {} found ---", respuesta.size());
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una tarjeta por id")
    public TarjetaResponse obtener(HttpServletRequest requestServlet, @PathVariable Long id) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        TarjetaResponse respuesta = service.obtener(id);
        log.info("--- response get card: {} ---", respuesta);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @PostMapping
    @Operation(summary = "Registrar una tarjeta nueva")
    @ApiResponse(responseCode = "201", description = "Tarjeta creada")
    public ResponseEntity<TarjetaResponse> crear(HttpServletRequest requestServlet,
                                                 @Valid @RequestBody CrearTarjetaRequest request) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        TarjetaResponse creada = service.crear(request);
        log.info("--- response create card: {} ---", creada);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return ResponseEntity.created(URI.create("/api/tarjetas/" + creada.id())).body(creada);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar una tarjeta (el número no se puede cambiar)")
    public TarjetaResponse actualizar(HttpServletRequest requestServlet, @PathVariable Long id,
                                      @Valid @RequestBody ActualizarTarjetaRequest request) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        TarjetaResponse respuesta = service.actualizar(id, request);
        log.info("--- response update card: {} ---", respuesta);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return respuesta;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una tarjeta")
    @ApiResponse(responseCode = "204", description = "Tarjeta eliminada")
    public ResponseEntity<Void> eliminar(HttpServletRequest requestServlet, @PathVariable Long id) {
        log.info("--- start process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        service.eliminar(id);
        log.info("--- end process endpoint: {} {} ---", requestServlet.getMethod(), requestServlet.getRequestURI());
        return ResponseEntity.noContent().build();
    }
}
