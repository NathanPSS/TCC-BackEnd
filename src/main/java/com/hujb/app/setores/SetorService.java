package com.hujb.app.setores;

import jakarta.transaction.Transactional;
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


    public void create(CreateSetorDTO dto) {
        repository.save(
                new Setor(dto.nome())
        );
    }
    public void update(String nome,Long id){
        repository.save(
                new Setor(id,nome)
        );
    }

    @Transactional
    public void remove(Long id){
        repository.softDeleteById(id);
    }

}
