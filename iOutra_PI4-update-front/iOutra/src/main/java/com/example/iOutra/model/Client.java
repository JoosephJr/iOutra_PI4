package com.example.iOutra.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Entity
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;

    private String email;

    private String password;

    private String cpf;

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Pedido> pedidos;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate birthDate;

    private String gender;

    @OneToOne(mappedBy = "client", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private BillingAddress billingAddress;

    @OneToMany(mappedBy = "client", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<DeliveryAddress> deliveryAddresses;

    public DeliveryAddress getMainDeliveryAddress() {
        Optional<DeliveryAddress> mainAddress = deliveryAddresses.stream()
                .filter(DeliveryAddress::isDefault)
                .findFirst();

        return mainAddress.orElse(null);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", billingAddress=" + billingAddress +
                ", deliveryAddresses=" + deliveryAddresses +
                '}';
    }

}
