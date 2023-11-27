package com.hujb.app.registros.dto;


import java.io.Serializable;

public record CheckInClosed(CheckInOpen checkInOpen,String closedAt) implements Serializable { }
