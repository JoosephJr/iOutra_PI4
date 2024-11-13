package com.example.iOutra.controller.store;

import com.example.iOutra.*;
import com.example.iOutra.model.*;
import com.example.iOutra.repository.BillingAddressRepository;
import com.example.iOutra.repository.ClientRepository;
import com.example.iOutra.repository.DeliveryAddressRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("cadastro")
public class ClientController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    BillingAddressRepository billingAddressRepository;

    @Autowired
    DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("")
    public String cadastro(@Valid ClientDTO client, BindingResult result) {

        int contAddressDefault = 0;

        for (DeliveryAddressDTO address : client.getDeliveryAddresses()) {
            if (address.isDefault()) {
                contAddressDefault++;
            }
        }

        if (contAddressDefault > 1) {
            result.rejectValue("deliveryAddresses", "error.deliveryAddresses",
                    "Defina apenas um endereço como principal");
        } else if (contAddressDefault == 0) {
            result.rejectValue("deliveryAddresses", "error.deliveryAddresses", "Defina um endereço como principal");
        }

        if (client.getPassword().trim().isEmpty() || client.getPassword().length() < 4) {
            result.rejectValue("password", "error.password", "Senha ter pelo menos 4 caracteres");
        }

        if (clientRepository.existsByCpf(client.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "CPF já cadastrado");
        }

        if (clientRepository.existsByEmail(client.getEmail()) && !client.getEmail().trim().isBlank()) {
            result.rejectValue("email", "error.email", "Email já cadastrado");
        }

        if (result.hasErrors()) {
            return "store/register";
        }

        Client entity = new Client();
        List<DeliveryAddress> deliveryAddresses = new ArrayList<>();

        entity.setFullName(client.getFullName());
        entity.setCpf(client.getCpf());
        entity.setEmail(client.getEmail());
        entity.setBirthDate(client.getBirthDate());
        entity.setGender(client.getGender());
        entity.setPassword(passwordEncoder.encode(client.getPassword()));

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setZipCode(client.getBillingAddress().getZipCode());
        billingAddress.setStreet(client.getBillingAddress().getStreet());
        billingAddress.setNumber(client.getBillingAddress().getNumber());
        billingAddress.setComplement(client.getBillingAddress().getComplement());
        billingAddress.setNeighborhood(client.getBillingAddress().getNeighborhood());
        billingAddress.setCity(client.getBillingAddress().getCity());
        billingAddress.setState(client.getBillingAddress().getState());
        billingAddress.setClient(entity);

        for (DeliveryAddressDTO address : client.getDeliveryAddresses()) {
            DeliveryAddress deliveryAddress = new DeliveryAddress();
            deliveryAddress.setZipCode(address.getZipCode());
            deliveryAddress.setStreet(address.getStreet());
            deliveryAddress.setNumber(address.getNumber());
            deliveryAddress.setComplement(address.getComplement());
            deliveryAddress.setNeighborhood(address.getNeighborhood());
            deliveryAddress.setCity(address.getCity());
            deliveryAddress.setState(address.getState());
            deliveryAddress.setClient(entity);
            deliveryAddress.setDefault(address.isDefault());
            deliveryAddresses.add(deliveryAddress);
        }

        entity.setBillingAddress(billingAddress);
        entity.setDeliveryAddresses(deliveryAddresses);

        clientRepository.save(entity);

        return "redirect:/login";
    }

    @PostMapping("{id}")
    public String update(@PathVariable Long id, @Valid ClientDTO client, BindingResult result) {

        int contAddressDefault = 0;

        for (DeliveryAddressDTO address : client.getDeliveryAddresses()) {
            if (address.isDefault()) {
                contAddressDefault++;
            }
        }

        if (contAddressDefault > 1) {
            result.rejectValue("deliveryAddresses", "error.deliveryAddresses",
                    "Defina apenas um endereço como principal");
        } else if (contAddressDefault == 0) {
            result.rejectValue("deliveryAddresses", "error.deliveryAddresses", "Defina um endereço como principal");
        }

        if (result.hasErrors()) {
            return "store/register"; // Retornar para a página de edição com mensagens de erro, se houver
        }

        // Recuperar o cliente existente do repositório
        Client existingClient = clientRepository.findById(id).orElseThrow();

        // Preencher o objeto Client com os dados atualizados do ClientDTO
        existingClient.setFullName(client.getFullName());
        existingClient.setCpf(client.getCpf());
        existingClient.setEmail(client.getEmail());
        existingClient.setBirthDate(client.getBirthDate());
        existingClient.setGender(client.getGender());
        // Check if a new password is provided
        if (client.getPassword() != null && !client.getPassword().isEmpty()) {
            // If a new password is provided, encode and set it
            existingClient.setPassword(passwordEncoder.encode(client.getPassword()));
        } else {
            // If no new password is provided, retain the existing password
            if (existingClient != null) {
                existingClient.setPassword(existingClient.getPassword());
            }
        }

        BillingAddress existingBillingAddress = billingAddressRepository.findById(client.getBillingAddress().getId())
                .orElseThrow();
        existingBillingAddress.setZipCode(client.getBillingAddress().getZipCode());
        existingBillingAddress.setStreet(client.getBillingAddress().getStreet());
        existingBillingAddress.setNumber(client.getBillingAddress().getNumber());
        existingBillingAddress.setComplement(client.getBillingAddress().getComplement());
        existingBillingAddress.setNeighborhood(client.getBillingAddress().getNeighborhood());
        existingBillingAddress.setCity(client.getBillingAddress().getCity());
        existingBillingAddress.setState(client.getBillingAddress().getState());

        List<DeliveryAddress> existingDeliveryAddresses = new ArrayList<>();

        for (DeliveryAddressDTO address : client.getDeliveryAddresses()) {

            DeliveryAddress deliveryAddress;

            if (address.getId() == null) {
                deliveryAddress = new DeliveryAddress();
            } else {
                deliveryAddress = deliveryAddressRepository.findById(address.getId()).orElseThrow();
            }
            if (address.isActive()) { // Verificar se o endereço está ativo
                deliveryAddress.setZipCode(address.getZipCode());
                deliveryAddress.setStreet(address.getStreet());
                deliveryAddress.setNumber(address.getNumber());
                deliveryAddress.setComplement(address.getComplement());
                deliveryAddress.setNeighborhood(address.getNeighborhood());
                deliveryAddress.setCity(address.getCity());
                deliveryAddress.setState(address.getState());
                deliveryAddress.setClient(existingClient);
                deliveryAddress.setDefault(address.isDefault());
            } else {
                // Marcar o endereço como inativo
                deliveryAddress.setActive(false);
            }

            existingDeliveryAddresses.add(deliveryAddress);
        }

        existingClient.setBillingAddress(existingBillingAddress);
        existingClient.setDeliveryAddresses(existingDeliveryAddresses);

        clientRepository.save(existingClient);

        return "redirect:/cadastro/{id}"; 
    }

    @GetMapping("{id}")
    public String getClientForm(@PathVariable Long id, Model model, HttpSession session, ClientDTO client) {

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

        // Verifica se há um cliente autenticado na sessão
        Long clientId = (Long) session.getAttribute("clientId");

        if (clientId == null || !clientId.equals(id)) {
            // Redireciona para a página inicial ou para uma página de erro
            return "redirect:/";
        }

        Client entity = clientRepository.findById(id).orElseThrow();

        model.addAttribute("clientDTO", entity);

        return "store/register";
    }

    @PostMapping("endereco/excluir/{addressId}")
    public String deleteAddress(@PathVariable Long addressId) {

        System.out.println("\n\n\n" + "CHEGUEI AQUI" + "\n\n\n");

        Optional<DeliveryAddress> addressOptional = deliveryAddressRepository.findById(addressId);

        if (addressOptional.isPresent()) {
            
            System.out.println("\n\n\n" + "DELETANDO...." + "\n\n\n");
            DeliveryAddress address = addressOptional.get();
            deliveryAddressRepository.delete(address);
        }

        return "redirect:/cadastro"; 
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
            HttpSession session
            ) {
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

        return "redirect:/carrinho";
    }


}
