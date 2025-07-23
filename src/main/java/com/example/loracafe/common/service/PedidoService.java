package com.example.loracafe.common.service;

import com.example.loracafe.common.dto.OrderRequestDto;
import com.example.loracafe.common.dto.OrderItemDto;
import com.example.loracafe.common.entity.*;
import com.example.loracafe.common.repository.PedidoRepository;
import com.example.loracafe.common.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;
    
    
    public Page<Pedido> getAllPedidos(Specification<Pedido> spec, Pageable pageable) {
        return pedidoRepository.findAll(spec, pageable);
    }

    
    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    
    public Optional<Pedido> getPedidoById(Integer id) {
        return pedidoRepository.findById(id);
    }

   
    public List<Pedido> getPedidosByUsuario(Integer usuarioId) {
        return pedidoRepository.findByUsuarioIdOrderByFechaPedidoDesc(usuarioId);
    }
    
    
    public Pedido savePedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
    
    /**
      @param orderRequest 
      @param usuario 
      @return 
      @throws IllegalStateException 
     */
    @Transactional
    public Pedido crearNuevoPedido(OrderRequestDto orderRequest, Usuario usuario) {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuario(usuario);
        nuevoPedido.setDireccionEntrega(orderRequest.getDireccionEntrega());
        nuevoPedido.setNotas(orderRequest.getNotas());
        nuevoPedido.setEstado(Pedido.EstadoPedido.PENDIENTE);
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        
        BigDecimal totalPedido = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        for (OrderItemDto itemDto : orderRequest.getItems()) {
            Producto producto = productoRepository.findById(itemDto.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemDto.getProductoId()));

            if (producto.getStock() < itemDto.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setProducto(producto);
            detalle.setCantidad(itemDto.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            BigDecimal subtotal = producto.getPrecio().multiply(new BigDecimal(itemDto.getCantidad()));
            detalle.setSubtotal(subtotal);
            detalle.setPedido(nuevoPedido); 
            
            detalles.add(detalle);
            totalPedido = totalPedido.add(subtotal);
        }
        
        nuevoPedido.setTotal(totalPedido);
        nuevoPedido.setDetalles(detalles);

        Pedido pedidoGuardado = pedidoRepository.save(nuevoPedido);

        for (DetallePedido detalle : pedidoGuardado.getDetalles()) {
            Producto producto = detalle.getProducto();
            int nuevoStock = producto.getStock() - detalle.getCantidad();
            producto.setStock(nuevoStock);
            productoRepository.save(producto); 
        }

        return pedidoGuardado;
    }
}