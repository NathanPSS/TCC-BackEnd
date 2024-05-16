package com.hujb.app.registros.entities;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import jakarta.persistence.*;


@Entity
public class Registro {

    @Id
    private String id;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private Estagiario estagiario;

    @ManyToOne()
    @JoinColumn(nullable = false)
    private Setor setor;

    @Column(nullable = false)
    private String tempo;

    @Column(nullable = false)
    private String hrEntrada;

    @Column(nullable = false)
    private String hrSaida;

    @Column(length = 500,nullable = false)
    private String descricao;

    public Registro(String id, Estagiario estagiario, Setor setor, String tempo, String hrEntrada, String hrSaida,String descricao) {
        this.id = id;
        this.estagiario = estagiario;
        this.setor = setor;
        this.tempo = tempo;
        this.hrEntrada = hrEntrada;
        this.hrSaida = hrSaida;
        this.descricao = descricao;
    }

    public Registro(){}

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Estagiario getEstagiario() {
        return estagiario;
    }

    private void setEstagiario(Estagiario estagiario) {
        this.estagiario = estagiario;
    }

    public Setor getSetor() {
        return setor;
    }

    private void setSetor(Setor setor) {
        this.setor = setor;
    }

    public String getTempo() {
        return tempo;
    }

    private void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getHrEntrada() {
        return hrEntrada;
    }

    public void setHrEntrada(String hrEntrada) {
        this.hrEntrada = hrEntrada;
    }

    public String getHrSaida() {
        return hrSaida;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setHrSaida(String hrSaida) {
        this.hrSaida = hrSaida;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "id='" + id + '\'' +
                ", estagiario=" + estagiario +
                ", setor=" + setor +
                ", tempo='" + tempo + '\'' +
                ", hrEntrada='" + hrEntrada + '\'' +
                ", hrSaida='" + hrSaida + '\'' +
                '}';
    }
}
