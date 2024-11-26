package com.example.iOutra.controller.store;

import com.example.iOutra.*;
import com.example.iOutra.model.Pedido;
import com.example.iOutra.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pedidos")
public class OrderInfoController {

    @Autowired
    PedidoRepository pedidoRepository;

    @GetMapping("/{id}")
    public String getOrderInfo(HttpSession session, @PathVariable Long id, Model model) {

        if (session.getAttribute("UsuarioLogado") == null) {
            return "redirect:/login";
        }

        Pedido pedido = pedidoRepository.findById(id).orElse(null);

        // Adiciona o pedido ao modelo para que seja acessível na página
        model.addAttribute("pedido", pedido);
        model.addAttribute("itens", pedido.getItens());

        // Retorna a página de detalhes do pedido
        return "store/order_info";
    }
}
