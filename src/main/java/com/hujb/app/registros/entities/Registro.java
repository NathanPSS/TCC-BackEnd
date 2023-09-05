package com.hujb.app.registros.entities;
import com.hujb.app.registros.enums.StatusRegistro;
import com.hujb.app.setores.Setor;
import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import jakarta.persistence.*;


@Entity
public class Registro {

    @Id
    private String id;

    @ManyToOne()
    @JoinColumn(name = "id_estagiario")
    private Estagiario estagiario;

    @ManyToOne()
    @JoinColumn(name = "setor")
    private Setor setor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusRegistro status;

    @Column(name = "tempo")
    private String tempo;


    @Column(name = "hrEntrada")
    private String hrEntrada;

    @Column(name = "hrSaida")
    private String hrSaida;

    public Registro(String id, Estagiario estagiario, Setor setor, StatusRegistro status, String tempo, String hrEntrada, String hrSaida) {
        this.id = id;
        this.estagiario = estagiario;
        this.setor = setor;
        this.status = status;
        this.tempo = tempo;
        this.hrEntrada = hrEntrada;
        this.hrSaida = hrSaida;
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

    public StatusRegistro getStatus() {
        return status;
    }

    private void setStatus(StatusRegistro status) {
        this.status = status;
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

    public void setHrSaida(String hrSaida) {
        this.hrSaida = hrSaida;
    }

    @Override
    public String toString() {
        return "Registro{" +
                "id='" + id + '\'' +
                ", estagiario=" + estagiario +
                ", setor=" + setor +
                ", status=" + status +
                ", tempo='" + tempo + '\'' +
                ", hrEntrada='" + hrEntrada + '\'' +
                ", hrSaida='" + hrSaida + '\'' +
                '}';
    }
}
