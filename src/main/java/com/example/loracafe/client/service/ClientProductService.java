package com.example.loracafe.client.service;

import com.example.loracafe.common.dto.ProductoClienteDto;
import com.example.loracafe.common.entity.Categoria;
import com.example.loracafe.common.entity.Producto; 
import com.example.loracafe.common.repository.CategoriaRepository;
import com.example.loracafe.common.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional; 
import java.util.stream.Collectors;

@Service
public class ClientProductService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

   
    public List<ProductoClienteDto> getAvailableProducts() {
        return productoRepository.findByDisponibleTrue()
                .stream()
                .map(ProductoClienteDto::new)
                .collect(Collectors.toList());
    }

    
    public List<ProductoClienteDto> getAvailableProductsByCategory(Integer categoriaId) {
        return productoRepository.findByDisponibleTrueAndCategoriaId(categoriaId)
                .stream()
                .map(ProductoClienteDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     
      @param id 
      @return 
     */
    public Optional<Producto> getAvailableProductById(Integer id) {
        return productoRepository.findByIdAndDisponibleTrue(id);
    }

    
    public List<Categoria> getActiveCategories() {
        return categoriaRepository.findByActivaTrue();
    }
}