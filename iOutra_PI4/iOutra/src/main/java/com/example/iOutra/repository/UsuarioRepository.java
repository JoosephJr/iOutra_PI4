package com.example.iOutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iOutra.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{



    boolean existsByCpf(String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);
    Optional<Usuario> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByNomeContainingIgnoreCase(String nome);
}
