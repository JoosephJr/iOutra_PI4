package com.example.iOutra.controller.backoffice.pedido;

import com.example.iOutra.controller.backoffice.Utils;
import com.example.iOutra.model.Pedido;
import com.example.iOutra.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/backoffice")
@Controller
public class PedidoController {

    @Autowired
    PedidoRepository pedidoRepository;

    @Autowired
    private Utils utils;

    @GetMapping("/pedidos")
    public String listarPedidos(Model model, Authentication authentication){

        // Adiciona a lista de usuários ao modelo para exibição na página.
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));

        List<Pedido> pedidos = pedidoRepository.findAllByOrderByDataPedidoDesc();

        model.addAttribute("pedidos", pedidos);

        return "backoffice/pedido/lista_pedidos";
    }

    @PostMapping("/pedidos/editarStatus")
    public String editarStatusPedido(@RequestParam Long pedidoId, @RequestParam String status) {

        Pedido pedido = pedidoRepository.findById(pedidoId).orElseThrow();

        if (pedido != null) {
            pedido.setStatus(status);;
            pedidoRepository.save(pedido);
        }
        return "redirect:/backoffice/pedidos";
    }
}