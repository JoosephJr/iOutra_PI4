package com.example.iOutra.repository;

import com.example.iOutra.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findAllByOrderByIdDesc();
    List<Pedido> findAllByOrderByDataPedidoDesc();
}
