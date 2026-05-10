package com.unq.eis.apibancaria.persistence;

import com.unq.eis.apibancaria.modelo.Transferencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaDAO extends JpaRepository<Transferencia, Long> {
}
