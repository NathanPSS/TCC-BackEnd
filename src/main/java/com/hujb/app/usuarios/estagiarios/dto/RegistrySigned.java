package com.hujb.app.usuarios.estagiarios.dto;

import com.hujb.app.utils.time.FormaterDateStrings;
import jakarta.persistence.Tuple;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public record RegistrySigned(String nome, String hrEntrada, String hrSaida,String data, String tempo, String descricao, String setorNome, String preceptorNome)  {

    public static RegistrySigned serializeObject(Tuple tuple) {

        return new RegistrySigned(
                (String) tuple.get("nome"),
                DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hr_entrada", String.class)).atZone(ZoneId.systemDefault())),
                DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hr_saida", String.class)).atZone(ZoneId.systemDefault())),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(Instant.parse(tuple.get("hr_saida", String.class)).atZone(ZoneId.systemDefault())),
                FormaterDateStrings.parseTimeDuration(Duration.parse(tuple.get("tempo", String.class))),
                (String) tuple.get("descricao"),
                (String) tuple.get("setor_nome"),
                (String) tuple.get("preceptor_nome")
        );
    }
    public static List<RegistrySigned> serialize(List<Tuple> tuples) {
        return tuples.stream().map(RegistrySigned::serializeObject).toList();
    }
}
