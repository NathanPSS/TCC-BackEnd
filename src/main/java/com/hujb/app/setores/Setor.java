package com.hujb.app.setores;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.hibernate.annotations.Where;

@Entity
@Where(clause = "deleted = false")
public class Setor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Boolean deleted = Boolean.FALSE;

    public Setor(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Setor(String nome) {
        this.nome = nome;
    }

    public Setor(){}

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    private void setNome(String nome) {
        this.nome = nome;
    }
}
