package com.example.iOutra.repository;

import com.example.iOutra.model.ImagemProduto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagemProdutoRepository  extends JpaRepository<ImagemProduto, Long> {
    ImagemProduto findByNomeArquivoContaining(String nomeArquivo);

    List<ImagemProduto> findByProdutoId(Long id);

    ImagemProduto findByProdutoIdAndPrincipalTrue(Long produtoId);

}
