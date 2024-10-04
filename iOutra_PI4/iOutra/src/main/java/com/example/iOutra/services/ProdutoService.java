package com.example.iOutra.services;

import com.example.iOutra.model.Produto;
import com.example.iOutra.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Transactional
    public Optional<Produto> buscarProdutoPorId(Long id) {
        return repository.findById(id);
    }
    public List<Produto> buscarProdutosAtivos(){
        return repository.findByIsActiveTrueOrderByIdDesc();
    }

}
