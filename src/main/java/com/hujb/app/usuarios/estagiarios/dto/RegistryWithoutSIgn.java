package com.hujb.app.usuarios.estagiarios.dto;

import com.hujb.app.utils.time.FormaterDateStrings;
import jakarta.persistence.Tuple;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public record RegistryWithoutSIgn(String nome, String hrEntrada, String hrSaida,String data, String tempo, String descricao, String setorNome) {
    public static RegistryWithoutSIgn serializeObject(Tuple tuple) {
        System.out.println(tuple.getElements().toString());
        return new RegistryWithoutSIgn(
                (String) tuple.get("nome"),
                DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hr_entrada", String.class)).atZone(ZoneId.systemDefault())),
                DateTimeFormatter.ofPattern("HH:mm:ss").format(Instant.parse(tuple.get("hr_saida", String.class)).atZone(ZoneId.systemDefault())),
                DateTimeFormatter.ofPattern("dd/MM/yyyy").format(Instant.parse(tuple.get("hr_saida", String.class)).atZone(ZoneId.systemDefault())),
                FormaterDateStrings.parseTimeDuration(Duration.parse(tuple.get("tempo", String.class))),
                (String) tuple.get("descricao"),
                (String) tuple.get("setor_nome")
        );
    }
    public static List<RegistryWithoutSIgn> serialize(List<Tuple> tuples) {
        return tuples.stream().map(RegistryWithoutSIgn::serializeObject).toList();
    }
}
