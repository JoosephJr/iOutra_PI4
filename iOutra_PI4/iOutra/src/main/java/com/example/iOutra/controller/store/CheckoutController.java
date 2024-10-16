package com.example.iOutra.controller.store;

import com.example.iOutra.*;
import com.example.iOutra.model.*;
import com.example.iOutra.repository.ClientRepository;
import com.example.iOutra.repository.DeliveryAddressRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Controller
@RequestMapping("pagamento")
public class CheckoutController {

    @Autowired
    List<Frete> tiposDeFrete;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;

    @PostMapping
    public String pagamento(@RequestParam(value = "freteradio", required = false) BigDecimal freteId, HttpSession session) {

        if (session.getAttribute("UsuarioLogado") == null) {  
            return "redirect:/login";
        }

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

                if (freteId != null) {
                    Optional<Frete> selectedFrete = tiposDeFrete.stream().filter(f -> f.getId().equals(freteId))
                            .findFirst();
                    if (selectedFrete.isPresent()) {
                        session.setAttribute("selectedFreteId", freteId);
                        // carrinho.setFrete(selectedFrete.get());
                    }
                }
            }
        }

        return "store/checkout";
    }

    @PostMapping("/concluir-pedido")
public String concluirPedido(HttpSession session, @RequestParam("idEnderecoPrincipal") String idEndereco,
        @RequestParam("valorFrete") String frete, @RequestParam("formaPagamento") String formaPagamento) {

    // Retrieve the delivery address
    DeliveryAddress endereco = deliveryAddressRepository.findById(Long.valueOf(idEndereco))
            .orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

    // Create a new Pedido object
    Pedido pedido = new Pedido();
    List<ItemPedido> itensPedido = new ArrayList<>();

    // Retrieve the client
    Client client = clientRepository.findById((Long) session.getAttribute("clientId"))
            .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

    // Retrieve the cart
    Carrinho carrinho = (Carrinho) session.getAttribute("carrinho");

    // Calculate the total order value
    BigDecimal valorFrete = new BigDecimal(frete);
    BigDecimal totalPedido = carrinho.getTotal().add(valorFrete);

    // Apply discount for "Boleto Bancário" payment method
    if (formaPagamento.equals("Boleto Bancário")) {
        totalPedido = totalPedido.multiply(new BigDecimal(0.95));
    }


    // Set properties on the Pedido object
    pedido.setTotal(totalPedido);
    pedido.setCliente(client);
    pedido.setStatus("Aguardando pagamento");
    pedido.setEnderecoEntrega(endereco);
    pedido.setValorFrete(valorFrete);
    pedido.setFormaPagamento(formaPagamento);
    pedido.setSerial(ThreadLocalRandom.current().nextLong(1000000000L, 10000000000L));

    // Set formatted order date
    LocalDateTime dataAtual = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
    String dataHoraFormatada = dataAtual.format(formatter);
    pedido.setDataPedido(dataHoraFormatada);

    // Transfer items from the cart to the order
    for (ItemCarrinho itemCarrinho : carrinho.getItens()) {
        ItemPedido itemPedido = new ItemPedido();
        itemPedido.setProduto(itemCarrinho.getProduto());
        itemPedido.setPedido(pedido);
        itemPedido.setTotal(itemCarrinho.getTotal());
        itemPedido.setQuantidade(itemCarrinho.getQuantidade());
        itensPedido.add(itemPedido);
    }

    pedido.setItens(itensPedido);

    // Store the order in the session and remove the cart
    session.setAttribute("pedido", pedido);
    //session.removeAttribute("carrinho");

    // Redirect to the order summary page
    return "redirect:/resumo";
}


    @PostMapping("/addEndereco")
    public String adicionarEndereco(
            @RequestParam("novoCEP") String cep,
            @RequestParam("novoLogradouro") String logradouro,
            @RequestParam("novoNumero") String numero,
            @RequestParam("novoComplemento") String complemento,
            @RequestParam("novoBairro") String bairro,
            @RequestParam("novoCidade") String cidade,
            @RequestParam("novoUF") String uf,
            HttpSession session) {
        Client client = (Client) session.getAttribute("client");

        DeliveryAddress endereco = new DeliveryAddress();
        endereco.setClient(client);
        endereco.setZipCode(cep);
        endereco.setStreet(logradouro);
        endereco.setNumber(numero);
        endereco.setComplement(complemento);
        endereco.setNeighborhood(bairro);
        endereco.setCity(cidade);
        endereco.setState(uf);
        endereco.setActive(true);
        endereco.setDefault(false);

        deliveryAddressRepository.save(endereco);

        return "redirect:/pagamento";
    }

}
