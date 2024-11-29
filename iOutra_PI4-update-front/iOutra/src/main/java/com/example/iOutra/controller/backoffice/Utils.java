package com.example.iOutra.controller.backoffice;


import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    @Autowired
    UsuarioRepository repository;
    
    //Método auxiliar para pegar informação do usuario que está autenticado no sistema
    public Usuario getUsuarioAutenticado(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        return repository.findByEmail(username).orElse(null);
    }

}
