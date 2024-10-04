package com.example.iOutra.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table
public class BillingAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String zipCode;


    private String street;


    private String number;

    private String complement;


    private String neighborhood;


    private String city;


    private String state;

    private String longitude;

    private String latitude;

    @OneToOne
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "fk_billing_address"))
    private Client client;

    public String toString() {
        return "BillingAddress{" +
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