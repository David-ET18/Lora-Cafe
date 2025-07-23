package com.example.loracafe.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Integer productoId;
    private int cantidad;
}