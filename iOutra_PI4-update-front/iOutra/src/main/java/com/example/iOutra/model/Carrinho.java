package com.example.iOutra.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table
public class Carrinho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "carrinho", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<ItemCarrinho> itens = new ArrayList<>();

    private BigDecimal total;

    public void atualizarTotal() {
        this.total = BigDecimal.ZERO;
        for (ItemCarrinho item : this.itens) {
            this.total = this.total.add(item.getTotal());
        }
    }

    public void subTotal(ItemCarrinho item){
        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }

        this.total = this.total.subtract(item.getTotal());
    }

    public void removerValor(BigDecimal valor){
        if (this.total == null) {
            this.total = BigDecimal.ZERO;
        }

        this.total = this.total.subtract(valor);
    }

    @Override
    public String toString() {
        return "Carrinho{" +
                "id=" + id +
                ", total=" + total +
                '}';
    }


}
