package com.example.loracafe.dashboard.controller;

import com.example.loracafe.common.entity.Categoria;
import com.example.loracafe.common.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/categories")
public class DashboardCategoryRestController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAllCategories() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }

    @PostMapping
    public ResponseEntity<Categoria> createCategory(@RequestBody Categoria categoria) {
        return new ResponseEntity<>(categoriaService.saveCategoria(categoria), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> updateCategory(@PathVariable Integer id, @RequestBody Categoria catDetails) {
        return categoriaService.getCategoriaById(id)
            .map(cat -> {
                cat.setNombre(catDetails.getNombre());
                cat.setDescripcion(catDetails.getDescripcion());
                cat.setActiva(catDetails.isActiva());
                return ResponseEntity.ok(categoriaService.saveCategoria(cat));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        if (categoriaService.getCategoriaById(id).isPresent()) {
            categoriaService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}