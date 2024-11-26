package com.example.iOutra.repository;

import com.example.iOutra.model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

    DeliveryAddress findByIsDefaultTrueAndClientId(Long clientId);

}
