package com.hujb.app.setores;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SetorService {

    private final SetorRepository repository;

    public SetorService(SetorRepository repository) {
        this.repository = repository;
    }

    public List<Setor> getAll(){
        return repository.findAll();
    }
}
