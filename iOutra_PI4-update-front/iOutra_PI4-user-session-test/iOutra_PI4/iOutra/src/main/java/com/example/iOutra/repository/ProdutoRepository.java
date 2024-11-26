package com.example.iOutra.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.iOutra.model.Produto;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    Page<Produto> findByNameContainingIgnoreCaseOrderByIdDesc(String name, Pageable pageable);

    @Query(value = "SELECT (max(id)+1) FROM produto", nativeQuery = true)
    Long getNextProductId();

    List<Produto> findByIsActiveTrueOrderByIdDesc();
    
}
