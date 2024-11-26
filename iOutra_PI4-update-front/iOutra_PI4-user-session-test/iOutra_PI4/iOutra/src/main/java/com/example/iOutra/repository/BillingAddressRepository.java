package com.example.iOutra.repository;


import com.example.iOutra.model.BillingAddress;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BillingAddressRepository extends JpaRepository<BillingAddress, Long> {
  
}
