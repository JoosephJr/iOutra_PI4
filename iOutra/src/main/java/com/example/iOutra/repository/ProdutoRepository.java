package com.example.iOutra.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iOutra.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    
}
