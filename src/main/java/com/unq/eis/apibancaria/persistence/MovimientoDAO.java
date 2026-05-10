package com.unq.eis.apibancaria.persistence;

import com.unq.eis.apibancaria.modelo.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoDAO extends JpaRepository<Movimiento, Long> {
}
