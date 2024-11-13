package com.example.iOutra.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table
public class DeliveryAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isDefault;

    private boolean active = true; // Novo campo para marcar se o endereço está ativo ou não

    private String zipCode;

    private String street;

    private String number;

    private String complement;

    private String neighborhood;

    private String city;

    private String state;

    @OneToMany(mappedBy = "enderecoEntrega", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Pedido> pedidos;


    @ManyToOne
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "fk_delivery_address"))
    private Client client;

    public String toString() {
        return "DeliveryAddress{" +
                "zipCode='" + zipCode + '\'' +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", complement='" + complement + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

}

