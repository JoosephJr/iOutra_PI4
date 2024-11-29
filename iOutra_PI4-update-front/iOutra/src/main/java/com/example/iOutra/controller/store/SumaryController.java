package com.example.iOutra.controller.store;


import com.example.iOutra.model.Carrinho;
import com.example.iOutra.model.Pedido;
import com.example.iOutra.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resumo")
public class SumaryController {

    @Autowired
    PedidoRepository pedidoRepository;

    @GetMapping
    public String resumoPedido(HttpSession session) {
        return "store/summary";
    }

    @PostMapping("/concluir-pagamento")
    public String concluirPagamento(HttpSession session) {

        // Obtém o pedido da sessão
        Pedido pedido = (Pedido) session.getAttribute("pedido");

        // Salva o pedido no banco de dados
        pedidoRepository.save(pedido);

        // Limpa o pedido da sessão
        session.removeAttribute("pedido");
        session.setAttribute("carrinho", new Carrinho());

        // Redireciona para a página de confirmação de pagamento
        return "redirect:/pedidos";
    }

}
