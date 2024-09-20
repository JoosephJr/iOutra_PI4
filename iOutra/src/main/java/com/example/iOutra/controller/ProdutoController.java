package com.example.iOutra.controller;

import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.iOutra.model.Produto;
import com.example.iOutra.model.ProdutoDTO;
import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.ProdutoRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository repo;

    @GetMapping({ "", "/" })
    public String exibirListaProdutos(Model model) {
        List<Produto> produtos = repo.findAll(Sort.by(Sort.Direction.DESC, "idProduto"));
        model.addAttribute("produtos", produtos);
        return "produtos/index";
    }

    @GetMapping("/cadastrarProduto")
    public String exibirCadastrarProduto(Model model) {
        ProdutoDTO produtoDTO = new ProdutoDTO();
        model.addAttribute("produtoDTO", produtoDTO);
        return "produtos/cadastrarProduto";
    }

    @PostMapping("/cadastrarProduto")
    public String cadastrarProduto(@Valid @ModelAttribute ProdutoDTO produtoDTO,
            BindingResult result) {
        if (produtoDTO.getImageFile().isEmpty()) {
            result.addError(new FieldError("produtoDTO", "imageFile", "O campo Imagem é obrigatório!"));
        }
        if (result.hasErrors()) {
            return "produtos/cadastrarProduto";
        }

        // Lógica para salvar a imagem no banco de dados
        MultipartFile imagem = produtoDTO.getImageFile();
        // cria um ID aleatório e único para atribuir ao nome do arquivo da imagem
        String storageFileName = UUID.randomUUID().toString() + "_" + imagem.getOriginalFilename();

        try {
            String uploadDir = "src/main/resources/static/images/";
            Path uploadPath = Paths.get(uploadDir);

            // Cria o diretório se não existir
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Tenta salvar no diretório
            try (InputStream inputStream = imagem.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir, storageFileName),
                        StandardCopyOption.REPLACE_EXISTING);

            }
        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        // Cria um produto e repassa as informações do ProdutoDTO para salvar no bd
        Produto produto = new Produto();
        produto.setNomeProduto(produtoDTO.getNomeProduto());
        produto.setDescricao(produtoDTO.getDescricao());
        produto.setPreco(produtoDTO.getPreco());
        produto.setAvaliacao(produtoDTO.getAvaliacao());
        produto.setQtdEstoque(produtoDTO.getQtdEstoque());
        // a imagem é recebida pelo explorador de arquivo
        produto.setImageFileName(storageFileName);

        // salva no bd
        repo.save(produto);

        return "redirect:/produtos";
    }

    @GetMapping("/editarProduto")
    public String exibirEditarProduto(Model model, @RequestParam int idProduto) {

        try {
            Produto produto = repo.findById(idProduto).get();
            model.addAttribute("produto", produto);

            ProdutoDTO produtoDTO = new ProdutoDTO();
            produtoDTO.setNomeProduto(produto.getNomeProduto());
            produtoDTO.setDescricao(produto.getDescricao());
            produtoDTO.setPreco(produto.getPreco());
            produtoDTO.setAvaliacao(produto.getAvaliacao());
            produtoDTO.setQtdEstoque(produto.getQtdEstoque());

            model.addAttribute("produtoDTO", produtoDTO);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
            return "redirect:/produtos";
        }

        return "produtos/editarProduto";
    }

    @PostMapping("/editarProduto")
    public String editarProduto(Model model, @RequestParam int idProduto,
            @Valid @ModelAttribute ProdutoDTO produtoDTO, BindingResult result) {

        try {
            Produto produto = repo.findById(idProduto).get();
            model.addAttribute("produto", produto);

            if (result.hasErrors()) {
                return "produtos/editarProduto";
            }

            if (!produtoDTO.getImageFile().isEmpty()) {
                // excluir imagem antiga
                String uploadDir = "src/main/resources/static/images/";
                Path oldImagePath = Paths.get(uploadDir + produto.getImageFileName());

                try {
                    Files.delete(oldImagePath);
                } catch (Exception ex) {
                    System.out.println("Exception: " + ex.getMessage());
                }

                // salvar novo caminho da imagem
                MultipartFile imagem = produtoDTO.getImageFile();
                // cria um ID aleatório e único para atribuir ao nome do arquivo da imagem
                String storageFileName = UUID.randomUUID().toString() + "_" + imagem.getOriginalFilename();

                try (InputStream inputStream = imagem.getInputStream()) {
                    Files.copy(inputStream, Paths.get(uploadDir + storageFileName),
                            StandardCopyOption.REPLACE_EXISTING);
                }

                produto.setImageFileName(storageFileName);
            }

            produto.setNomeProduto(produtoDTO.getNomeProduto());
            produto.setDescricao(produtoDTO.getDescricao());
            produto.setPreco(produtoDTO.getPreco());
            produto.setAvaliacao(produtoDTO.getAvaliacao());
            produto.setQtdEstoque(produtoDTO.getQtdEstoque());

            repo.save(produto);

        } catch (Exception ex) {
            System.out.println("Exception: " + ex.getMessage());
        }

        return "redirect:/produtos";
    }

    @GetMapping("/detalhe")
    public String exibirDetalhe(Model model, @RequestParam int idProduto) {
        try {
            // Buscar o produto pelo ID no banco de dados
            Produto produto = repo.findById(idProduto).get();
            model.addAttribute("produto", produto);

            ProdutoDTO produtoDTO = new ProdutoDTO();
            produtoDTO.setNomeProduto(produto.getNomeProduto());
            produtoDTO.setDescricao(produto.getDescricao());
            produtoDTO.setPreco(produto.getPreco());
            produtoDTO.setAvaliacao(produto.getAvaliacao());

            model.addAttribute("produtoDTO", produtoDTO);

        } catch (Exception e) {
            System.out.println("Erro ao buscar detalhes do produto: " + e.getMessage());
            return "redirect:/produtos";
        }
        return "produtos/detalhe";
    }

    @PostMapping("/alterarStatus")
    public String alterarStatus(@RequestParam int idProduto) {
        Produto produto = repo.findById(idProduto).get();
        produto.setActive(!produto.isActive());
        repo.save(produto);
        return "redirect:/produtos";
    }

}
