package com.example.loracafe.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.loracafe.common.entity.Promocion;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Integer> {

    /**
      @param fechaInicioReferencia 
      @param fechaFinReferencia
      @return 
     */
    List<Promocion> findByActivaTrueAndFechaInicioBeforeAndFechaFinAfter(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}