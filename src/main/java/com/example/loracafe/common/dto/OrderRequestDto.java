package com.example.loracafe.common.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private List<OrderItemDto> items;
    private String direccionEntrega;
    private String notas;
}