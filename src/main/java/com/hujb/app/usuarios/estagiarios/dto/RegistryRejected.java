package com.hujb.app.usuarios.estagiarios.dto;

import com.hujb.app.utils.time.FormaterDateStrings;
import jakarta.persistence.Tuple;


import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public record RegistryRejected(String nome, String hrEntrada, String hrSaida,String data, String tempo, String descricao,
                               String setorNome, String preceptorNome, String motivo, String menssage) {
    public static RegistryRejected serializeObject(Tuple tuple) {
        return new RegistryRejected(
                (String) tuple.get("nome"),
                DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hrEntrada", String.class)).atZone(ZoneId.systemDefault())),
                DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hrSaida", String.class)).atZone(ZoneId.systemDefault())),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(Instant.parse(tuple.get("hrSaida", String.class)).atZone(ZoneId.systemDefault())),
                FormaterDateStrings.parseTimeDuration(Duration.parse(tuple.get("tempo", String.class))),
                (String) tuple.get("descricao"),
                (String) tuple.get("setorNome"),
                (String) tuple.get("preceptorNome"),
                (String) tuple.get("menssage"),
                (String) tuple.get("motivo")
        );
    }
    public static List<RegistryRejected> serialize(List<Tuple> tuples) {
        return tuples.stream().map(RegistryRejected::serializeObject).toList();
    }
}
