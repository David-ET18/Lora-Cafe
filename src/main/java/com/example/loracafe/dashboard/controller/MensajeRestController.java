package com.example.loracafe.dashboard.controller;

import com.example.loracafe.common.dto.MensajeDetalleDto;
import com.example.loracafe.common.dto.MensajeDto;
import com.example.loracafe.common.entity.Mensaje;
import com.example.loracafe.common.service.MensajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/dashboard/messages")
public class MensajeRestController {

    @Autowired
    private MensajeService mensajeService;

    
    @GetMapping
    public ResponseEntity<List<MensajeDto>> getAllMessages() {
        List<MensajeDto> mensajesDto = mensajeService.getAllMensajes();
        return ResponseEntity.ok(mensajesDto);
    }

    /**
      @param id 
      @return 
     */
    @GetMapping("/{id}")
    public ResponseEntity<MensajeDetalleDto> getMessageById(@PathVariable Integer id) {
        Optional<Mensaje> mensajeOptional = mensajeService.getMensajeById(id);

        if (mensajeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Mensaje mensaje = mensajeOptional.get();
        
        if (mensaje.getEstado() == Mensaje.EstadoMensaje.NUEVO) {
            mensaje = mensajeService.marcarComoLeido(id);
        }

        MensajeDetalleDto dto = new MensajeDetalleDto(mensaje);
        return ResponseEntity.ok(dto);
    }
    
    /**
      @param id 
      @param body 
      @return 
     */
    @PostMapping("/{id}/reply")
    public ResponseEntity<MensajeDetalleDto> replyToMessage(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        String respuesta = body.get("respuesta");
        if (respuesta == null || respuesta.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            Mensaje mensajeRespondido = mensajeService.responderMensaje(id, respuesta);
            
            MensajeDetalleDto dto = new MensajeDetalleDto(mensajeRespondido);
            
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Integer id) {
        Optional<Mensaje> mensajeOptional = mensajeService.getMensajeById(id);
        if (mensajeOptional.isPresent()) {
            mensajeService.deleteMensaje(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}