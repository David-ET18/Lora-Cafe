package com.example.loracafe.dashboard.service;

import com.example.loracafe.common.dto.ClienteDashboardDto;
import com.example.loracafe.common.entity.Usuario;
import com.example.loracafe.common.repository.PedidoRepository;
import com.example.loracafe.common.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerDashboardService {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
      @return 
     */
    public List<ClienteDashboardDto> getClientesConTotalPedidos() {
        List<Usuario> todosLosUsuarios = usuarioService.getAllUsuarios();

        return todosLosUsuarios.stream()
            .filter(usuario -> usuario.getRol() == Usuario.Rol.CLIENTE)
            .map(cliente -> {
                long totalPedidos = pedidoRepository.countByUsuarioId(cliente.getId());
                return new ClienteDashboardDto(cliente, totalPedidos);
            })
            .collect(Collectors.toList());
    }

}