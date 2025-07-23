package com.example.loracafe.client.controller;

import com.example.loracafe.common.entity.Usuario;
import com.example.loracafe.common.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    
    
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "Cliente/login";
    }

    /**
      @param usuario 
      @param redirectAttributes 
      @return 
     */
    @PostMapping("/registro")
    public String processRegistration(@ModelAttribute Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.registrarNuevoCliente(usuario);
            redirectAttributes.addFlashAttribute("successMessage", "¡Registro exitoso! Por favor, inicia sesión.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/login";
    }
}