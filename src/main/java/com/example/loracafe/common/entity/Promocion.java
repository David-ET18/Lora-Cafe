package com.example.loracafe.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "promocion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Promocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal descuento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPromocion tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private boolean activa = true;
    
    // Campo opcional para una imagen de la promoción
    @Column(name = "imagen_url", length = 255)
    private String imagenUrl;

    
    public enum TipoPromocion {
        PORCENTAJE,
        MONTO_FIJO
    }

     @ManyToMany(fetch = FetchType.EAGER) // <-- CAMBIA A EAGER
    @JoinTable(
        name = "producto_promocion", 
        joinColumns = @JoinColumn(name = "promocion_id"), 
        inverseJoinColumns = @JoinColumn(name = "producto_id"))
    private Set<Producto> productos;
}