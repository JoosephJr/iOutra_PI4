package com.example.iOutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iOutra.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{
    
    public Usuario findByEmail (String email);
}
