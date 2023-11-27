package com.hujb.app.registros.repositories;

import com.hujb.app.registros.entities.Registro;
import com.hujb.app.registros.entities.RegistroAssinado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroAssinadoRepository extends JpaRepository<RegistroAssinado,Registro> {


}
