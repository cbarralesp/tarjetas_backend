package cl.ipss.tarjetas.repository;

import cl.ipss.tarjetas.entity.TarjetaBip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * REPOSITORY: acceso a datos. Al extender JpaRepository, Spring genera
 * en tiempo de ejecución save, findById, findAll, deleteById, existsById...
 */
@Repository
public interface TarjetaBipRepository extends JpaRepository<TarjetaBip, Long> {

    /** Método derivado: Spring crea el SELECT leyendo el nombre del método. */
    boolean existsByNumero(String numero);
}
