package com.hujb.app.registros.services;


import com.hujb.app.registros.repositories.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrosService {

    public RegistroRepository repository;


    @Autowired
    public RegistrosService(RegistroRepository repository) {
        this.repository = repository;
    }


}
