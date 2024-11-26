package com.example.iOutra.controller.store;




import com.example.iOutra.model.*;
import com.example.iOutra.repository.ClientRepository;
import com.example.iOutra.repository.ImagemProdutoRepository;
import com.example.iOutra.repository.ProdutoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Controller
public class CartController {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ImagemProdutoRepository imgRepository;

    @Autowired
    ClientRepository clientRepository;

     @Autowired
     List<Frete> tiposDeFrete;


    @GetMapping("/carrinho")
    public String seeCart(HttpSession session, Model model) {

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new Carrinho();
            session.setAttribute("carrinho", carrinho);

        }

        if (session.getAttribute("UsuarioLogado") != null) {
            String emailCliente = (String) session.getAttribute("UsuarioLogado");
            Optional<Client> clienteOptional = clientRepository.findByEmail(emailCliente);
            if (clienteOptional.isPresent()) {
                Client cliente = clienteOptional.get();
                session.setAttribute("client", cliente);
                session.setAttribute("frete", tiposDeFrete);
                session.setAttribute("entrega", cliente.getMainDeliveryAddress());

            }
        }

        return "store/cart";
    }

    @PostMapping("/carrinho")
    public String addInCart(HttpSession session, @RequestParam("produtoId") Long produtoId, @RequestParam("quantidade") int quantidade) {

        boolean produtoJaAdicionado = false;

        Produto produto = produtoRepository.findById(produtoId).orElseThrow();

        ImagemProduto imgProduto = imgRepository.findByProdutoIdAndPrincipalTrue(produtoId);

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
        List<ItemCarrinho> itens = carrinho.getItens();

        if (itens == null) {
            itens = new ArrayList<>();
        }

        for (ItemCarrinho item : itens) {

            if (item.getProduto().getId().equals(produtoId)) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                item.setProduto(produto);
                item.setCarrinho(carrinho);
                item.setImagem(imgProduto.getNomeArquivo());
                produtoJaAdicionado = true;
            }

        }

        if (!produtoJaAdicionado) {
            ItemCarrinho item = new ItemCarrinho();
            item.setCarrinho(carrinho);
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setImagem(imgProduto.getNomeArquivo());
            itens.add(item);

        }

        carrinho.setItens(itens);
        carrinho.atualizarTotal();
        session.setAttribute("carrinho", carrinho);

        return "redirect:/carrinho";
    }

    @PostMapping("/item")
    public String incrementarProduto(HttpSession session, @RequestParam("itemId") Long itemId) {

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

        List<ItemCarrinho> itens = carrinho.getItens();

        for (ItemCarrinho item : itens) {
            if (itemId == item.getProduto().getId()) {
                item.setQuantidade(item.getQuantidade() + 1);
            }
        }

        carrinho.setItens(itens);

        carrinho.atualizarTotal();

        session.setAttribute("carrinho", carrinho);

        return "redirect:/carrinho";

    }

    @PostMapping("/itemreduzir")
    public String decrementarProduto(HttpSession session, @RequestParam("itemId") Long itemId) {

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

        List<ItemCarrinho> itens = carrinho.getItens();

        for (ItemCarrinho item : itens) {
            if (itemId == item.getProduto().getId()) {
                item.setQuantidade(item.getQuantidade() - 1);
            }

        }

        carrinho.setItens(itens);
        carrinho.atualizarTotal();
        session.setAttribute("carrinho", carrinho);

        return "redirect:/carrinho";

    }

    @PostMapping("/remover")
    public String removerDoCarrinho(HttpSession session, @RequestParam("itemId") Long itemId) {

        Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

        List<ItemCarrinho> itens = carrinho.getItens();

        // Itera sobre os itens do carrinho
        Iterator<ItemCarrinho> iterator = itens.iterator();
        while (iterator.hasNext()) {
            ItemCarrinho item = iterator.next();
            if (itemId.equals(item.getProduto().getId())) {
                // Remove o item do carrinho
                iterator.remove();
                // Atualiza o subtotal do carrinho

                carrinho.removerValor(item.getTotal());
                break; // Se encontrar o item, não é necessário continuar iterando
            }

        }

        carrinho.atualizarTotal();
        session.setAttribute("carrinho", carrinho);

        return "redirect:/carrinho";
    }

    @PostMapping("/comprar")
    public String compra(HttpSession session, Model model){
        return "store/orders";
    }

}
