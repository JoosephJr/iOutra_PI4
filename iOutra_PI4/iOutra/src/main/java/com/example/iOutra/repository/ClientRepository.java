package com.example.iOutra.repository;

import com.example.iOutra.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    public boolean existsByCpf(String cpf);
    public boolean existsByEmail(String email);

}
