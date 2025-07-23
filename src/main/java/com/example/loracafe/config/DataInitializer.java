package com.example.loracafe.config;

import com.example.loracafe.common.entity.Usuario;
import com.example.loracafe.common.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        if (usuarioRepository.findByEmail("18davidescatetorres@gmail.com").isEmpty()) {
            System.out.println("Creando usuario administrador por defecto...");
            
            Usuario admin = new Usuario();
            admin.setNombre("David");
            admin.setApellido("Escate Torres");
            admin.setEmail("18davidescatetorres@gmail.com");
            admin.setPassword(passwordEncoder.encode(".david1803")); 
            admin.setRol(Usuario.Rol.ADMIN);
            admin.setActivo(true);

            usuarioRepository.save(admin);
            
            System.out.println("Usuario administrador creado con éxito.");
            System.out.println("Email: 18davidescatetorres@gmail.com");
            System.out.println("Password: .david1803");
        }
    }
}