package com.unq.eis.apibancaria.persistence;

import com.unq.eis.apibancaria.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioDAO extends JpaRepository<Usuario, Long> {

    boolean existsByEmail(String email);
}
