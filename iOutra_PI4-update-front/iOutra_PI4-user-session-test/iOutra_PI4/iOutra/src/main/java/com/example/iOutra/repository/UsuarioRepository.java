package com.example.iOutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iOutra.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    boolean existsByEmailOrCpf(String email, String cpf);
    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findByFullnameContainingIgnoreCase(String fullname);

}
