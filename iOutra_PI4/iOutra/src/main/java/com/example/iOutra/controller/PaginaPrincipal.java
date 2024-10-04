package com.example.iOutra.controller;


import com.example.iOutra.repository.ClientRepository;
import com.example.iOutra.services.ClientService;
import com.example.iOutra.services.ProdutoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.iOutra.model.*;
import java.util.Optional;


    @Controller
    @RequestMapping("")
    public class PaginaPrincipal {

        @Autowired
        private ProdutoService produtoService;

        @Autowired
        private ClientService clientService;

        @Autowired
        private ClientRepository clientRepository;

        @GetMapping
        public String landingPage(Model model, HttpSession session) {
            // Verifica se o carrinho já está na sessão; se não estiver, cria um novo
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
                }
            }

            var listaProdutos = produtoService.buscarProdutosAtivos();
            model.addAttribute("produtoPage", listaProdutos);
            return "store/index";
        }

        @GetMapping("/login")
        public String login(Client client, HttpSession session) {

            if (session.getAttribute("UsuarioLogado") != null) {
                return "redirect:/";
            }

            return "store/login";
        }

        @PostMapping("/login")
        public String processamentoLogin(@ModelAttribute("client") Client client, Model model, HttpSession session) {
            boolean valido = clientService.validarLogin(client.getEmail(), client.getPassword());

            session.setAttribute("UsuarioLogado", client.getEmail());

            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

            if (valido && carrinho.getItens().size() > 0) {
                return "redirect:/carrinho";
            } else if (valido) {
                return "redirect:/";
            } else {
                model.addAttribute("Error", "Usuário e/ou senha inválidos");

                return "store/login";
            }
        }

        @GetMapping("/cadastro")
        public String register(ClientDTO client, HttpSession session, Model model) {

            if (session.getAttribute("UsuarioLogado") != null) {
                return "redirect:/";
            }

            client.getDeliveryAddresses().get(0).setDefault(true);
            return "store/register";
        }

        @GetMapping("/produto")
        public String obterporId(@RequestParam(name = "id", required = false) Long id, Model model, HttpSession session) {

            Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");
            if (carrinho == null) {
                carrinho = new Carrinho();
                session.setAttribute("carrinho", carrinho);
            }

            // Verifica se o cliente está logado
            if (session.getAttribute("UsuarioLogado") != null) {
                String emailCliente = (String) session.getAttribute("UsuarioLogado");
                Optional<Client> clienteOptional = clientRepository.findByEmail(emailCliente);
                if (clienteOptional.isPresent()) {
                    Client cliente = clienteOptional.get();
                    String nomeCliente = cliente.getFullName();
                    session.setAttribute("clientId", cliente.getId());
                    model.addAttribute("nameClient", nomeCliente);
                    model.addAttribute("client", cliente);
                }
            }

            Optional<Produto> produtoVisualizar = produtoService.buscarProdutoPorId(id);

            if (produtoVisualizar.isPresent()) {
                Produto produto = produtoVisualizar.get();
                model.addAttribute("produto", produto);
                return "store/product";
            } else {
                return "index";
            }
        }

        @PostMapping("/sair")
        public String sair(HttpSession session) {
            session.invalidate();
            return "redirect:/";

        }

    }
