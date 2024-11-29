package com.example.iOutra.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProdutoDto {

    private Long id;

    @Size(min = 2, max = 200, message = "Nome do produto deve ter no máximo 200 caracteres")
    private String name;

    @Size(max = 2000, message = "A descrição deve ter no máximo 2000 caracteres")
    private String description;

    @Min(value = 1, message = "Nota deve estar entre 1 e 5")
    @Max(value = 5, message = "Nota deve estar entre 1 e 5")
    @NotNull(message = "Rating valor não pode ser nulo")
    private float rating;

    @NotNull(message = "Preco valor não pode ser nulo")
    @Min(value = 0, message = "não pode ser negativo")
    private BigDecimal price;

    @Min(value = 0, message = "Não pode ser negativo")
    @NotNull(message = "Quantidade valor não pode ser nulo")
    private Integer stockQuantity;

    private List<ImagemProdutoDto> imagens;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public List<ImagemProdutoDto> getImagens() {
        return imagens;
    }

    public void setImagens(List<ImagemProdutoDto> imagens) {
        this.imagens = imagens;
    }

}

