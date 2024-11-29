package com.example.iOutra.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", foreignKey = @ForeignKey(name = "fk_item_pedido"))
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "produto_id", foreignKey = @ForeignKey(name = "fk_item_pedido_produto"))
    private Produto produto;

    private BigDecimal total;

    private int quantidade;

    @Override
    public String toString() {
        return "ItemPedido{" +
                "id=" + id +
                ", pedido=" + pedido +
                ", produto=" + produto +
                ", total=" + total +
                ", quantidade=" + quantidade +
                '}';
    }
}