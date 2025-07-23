package com.example.loracafe.dashboard.controller;

import com.example.loracafe.common.dto.ClienteDashboardDto;
import com.example.loracafe.dashboard.service.CustomerDashboardService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard/customers")
public class CustomerRestController {

    @Autowired
    private CustomerDashboardService customerDashboardService;

    /**
      @return 
     */
    @GetMapping
    public ResponseEntity<List<ClienteDashboardDto>> getAllCustomersWithOrderCount() {
        List<ClienteDashboardDto> clientes = customerDashboardService.getClientesConTotalPedidos();
        return ResponseEntity.ok(clientes);
    }
    
}