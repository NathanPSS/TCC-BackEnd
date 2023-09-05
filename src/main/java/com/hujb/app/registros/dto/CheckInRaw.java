package com.hujb.app.registros.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.util.Converter;
import com.hujb.app.setores.Setor;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.time.Instant;

public record CheckInRaw(String username, String createdAT, String id, Setor setor) implements Serializable {

}
