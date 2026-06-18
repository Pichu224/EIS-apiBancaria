package com.unq.eis.apibancaria.persistence;

import com.unq.eis.apibancaria.modelo.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CajaDAO extends JpaRepository<Caja, Long> {

    boolean existsByNroCaja(Long nroCaja);

    boolean existsByAlias(String alias);

    List<Caja> findByUsuario_IdUsuario(Long idUsuario);

    Optional<Caja> findByAlias(String alias);
}
