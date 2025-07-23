package com.example.loracafe.dashboard.controller;

import com.example.loracafe.common.entity.Producto;
import com.example.loracafe.common.entity.Promocion;
import com.example.loracafe.common.dto.PromocionDashboardDto;
import com.example.loracafe.common.repository.ProductoRepository;
import com.example.loracafe.common.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard/promotions")
public class PromocionRestController {

    @Autowired
    private PromocionService promocionService;

    @Autowired
    private ProductoRepository productoRepository; 

    @GetMapping
    public ResponseEntity<List<PromocionDashboardDto>> getAllPromociones() {
        return ResponseEntity.ok(promocionService.getAllPromocionesDto());
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Promocion> getPromocionById(@PathVariable Integer id){
        return promocionService.getPromocionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Promocion> createPromocion(@RequestBody Promocion promocion) {
        promocion.setProductos(new HashSet<>()); 
        Promocion savedPromo = promocionService.savePromocion(promocion);
        return new ResponseEntity<>(savedPromo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Promocion> updatePromocion(@PathVariable Integer id, @RequestBody Promocion promoDetails) {
        return promocionService.getPromocionById(id)
            .map(promoExistente -> {
                promoExistente.setNombre(promoDetails.getNombre());
                promoExistente.setDescripcion(promoDetails.getDescripcion());
                promoExistente.setTipo(promoDetails.getTipo());
                promoExistente.setDescuento(promoDetails.getDescuento());
                promoExistente.setFechaInicio(promoDetails.getFechaInicio());
                promoExistente.setFechaFin(promoDetails.getFechaFin());
                promoExistente.setActiva(promoDetails.isActiva());
                promoExistente.setImagenUrl(promoDetails.getImagenUrl()); 
                
                Set<Producto> productosAsociados = new HashSet<>();
                if (promoDetails.getProductos() != null) {
                    productosAsociados = promoDetails.getProductos().stream()
                        .map(productoConId -> productoRepository.findById(productoConId.getId()).orElse(null))
                        .filter(Objects::nonNull) 
                        .collect(Collectors.toSet());
                }
                promoExistente.setProductos(productosAsociados);
                
                Promocion updatedPromo = promocionService.savePromocion(promoExistente);
                return ResponseEntity.ok(updatedPromo);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePromocion(@PathVariable Integer id) {
        Optional<Promocion> promo = promocionService.getPromocionById(id);
        if (promo.isPresent()) {
            promocionService.deletePromocion(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}