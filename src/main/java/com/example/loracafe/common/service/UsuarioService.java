package com.example.loracafe.common.service;

import com.example.loracafe.common.dto.UserProfileDto;
import com.example.loracafe.common.entity.Usuario;
import com.example.loracafe.common.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario registrarNuevoCliente(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalStateException("El correo electrónico ya está registrado.");
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setRol(Usuario.Rol.CLIENTE);
        usuario.setActivo(true);
        return usuarioRepository.save(usuario);
    }

    /**
      @param emailActual 
      @param profileDto  
      @return 
     */
    @Transactional
    public Usuario updateUserProfile(String emailActual, UserProfileDto profileDto) {
        Usuario usuario = usuarioRepository.findByEmail(emailActual)
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado."));

        if (!emailActual.equals(profileDto.getEmail())) {
            if (usuarioRepository.findByEmail(profileDto.getEmail()).isPresent()) {
                throw new IllegalStateException("El nuevo correo electrónico ya está en uso.");
            }
            usuario.setEmail(profileDto.getEmail());
        }

        usuario.setNombre(profileDto.getNombre());
        usuario.setApellido(profileDto.getApellido());
        usuario.setTelefono(profileDto.getTelefono());
        usuario.setDireccion(profileDto.getDireccion());

        return usuarioRepository.save(usuario);
    }


    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> getUsuarioByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> searchUsuarios(String term) {
        return usuarioRepository.searchByTerm(term);
    }

    /**
      @param usuario 
      @return 
     */
    @Transactional
    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getId() != null) {
            Usuario usuarioExistente = usuarioRepository.findById(usuario.getId())
                .orElseThrow(() -> new IllegalStateException("Usuario no encontrado"));
            
            if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
                usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
            
            usuarioExistente.setNombre(usuario.getNombre());
            usuarioExistente.setApellido(usuario.getApellido());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setRol(usuario.getRol());
            usuarioExistente.setActivo(usuario.isActivo());

            return usuarioRepository.save(usuarioExistente);

        } else {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            return usuarioRepository.save(usuario);
        }
    }

    @Transactional
    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}