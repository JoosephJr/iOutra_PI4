package com.example.iOutra.controller.store;


import ch.qos.logback.core.model.Model;
import com.example.iOutra.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pedidos")
public class OrdersController {

    @Autowired
    PedidoRepository pedidoRepository;
    
    @GetMapping
    public String pedidos(HttpSession session, Model model){

        if (session.getAttribute("UsuarioLogado") == null) {
            return "redirect:/login";
        }
        
        session.setAttribute("pedidos", pedidoRepository.findAllByOrderByIdDesc());

        return "store/orders";
    }

   

}
