package com.hujb.app.registros.repositories;

import com.hujb.app.registros.entities.Registro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroRepository extends JpaRepository<Registro,Long> {
}
