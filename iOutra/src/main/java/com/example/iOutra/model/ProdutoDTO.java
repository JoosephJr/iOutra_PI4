package com.example.iOutra.model;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ProdutoDTO {

    @NotEmpty(message = "O nome do produto é obrigatório")
    private String nomeProduto;

    @Min(0)
    private BigDecimal preco;

    @Min(0)
    @Max(5)
    private float avaliacao;

    @Min(0)
    private Integer qtdEstoque;

    @Size(min = 10, message = "A descrição deve ter no mínimo 10 caracteres!")
    @Size(max = 2000, message = "A descrição não pode ultrapassar 2000 caracteres!")
    private String descricao;

    private MultipartFile imageFile;

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public float getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(float avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public Integer getQtdEstoque() {
        return qtdEstoque;
    }

    public void setQtdEstoque(Integer qtdEstoque) {
        this.qtdEstoque = qtdEstoque;
    }

}
