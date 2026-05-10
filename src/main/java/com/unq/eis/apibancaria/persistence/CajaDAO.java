package com.unq.eis.apibancaria.persistence;

import com.unq.eis.apibancaria.modelo.Caja;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CajaDAO extends JpaRepository<Caja, Long> {
}
