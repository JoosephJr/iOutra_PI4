package com.example.iOutra.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nomeArquivo;

    private boolean principal;

    @Lob
    @Column(columnDefinition="LONGBLOB")
    private byte[] arquivo;

    @ManyToOne
    @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_imagem_produto"))
    private Produto produto;

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public boolean isPrincipal() {
        return principal;
    }

    public void setPrincipal(boolean principal) {
        this.principal = principal;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }


    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    @Override
    public String toString() {
        return "ImagemProduto{" +
                "id=" + id +
                ", nomeArquivo='" + nomeArquivo + '\'' +
                ", principal=" + principal +
                ", arquivo=" + arquivo +
                '}';
    }

}
