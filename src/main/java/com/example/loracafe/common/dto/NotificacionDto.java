package com.example.loracafe.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NotificacionDto {
    private String tipo; 
    private String texto;
    private LocalDateTime fecha;
    private String url; 
}