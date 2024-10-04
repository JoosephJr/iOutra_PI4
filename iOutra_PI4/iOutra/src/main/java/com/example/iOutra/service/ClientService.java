package com.example.iOutra.service;

import com.example.iOutra.model.Client;
import com.example.iOutra.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Client findByEmail(String email) {
        Optional<Client> clientOptional = repository.findByEmail(email);
        return clientOptional.orElseThrow(() -> new RuntimeException("Cliente não encontrado para o email: " + email));
    }

    public boolean validarLogin(String email, String password) {
        Optional<Client> clienteOptional = repository.findByEmail(email);
        if (clienteOptional.isPresent()) {
            Client cliente = clienteOptional.get();
            return passwordEncoder.matches(password, cliente.getPassword());
        }
        return false;
    }

}
