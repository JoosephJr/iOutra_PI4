package com.example.iOutra.controller.backoffice;

import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("backoffice")
public class BackofficeController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Utils utils;

    @GetMapping("setup")
    public String cadastrarUsuario() {

        boolean userExists = repository.existsByEmail("admin@root.com");

        if (userExists) {
            return "backoffice";
        }

        Usuario usuario = new Usuario();
        usuario.setEmail("admin@root.com");
        usuario.setFullname("admin");
        usuario.setCpf("99999999999");
        usuario.setPassword(passwordEncoder.encode("admin"));
        usuario.setRole("Administrador");
        usuario.setActive(true);

        repository.save(usuario);

        return "redirect:/backoffice/login";
    }

    @GetMapping("")
    public String handleBackofficeLogin(Usuario usuario) {
        return "backoffice/login";
    }

    @GetMapping("home")
    public String handleBackofficeHome(Model model, Authentication authentication) {
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/home";
    }
}



