package com.example.iOutra.controller.backoffice.produto;


import com.example.iOutra.controller.backoffice.Utils;
import com.example.iOutra.model.ImagemProduto;
import com.example.iOutra.model.ImagemProdutoDto;
import com.example.iOutra.model.Produto;
import com.example.iOutra.model.ProdutoDto;
import com.example.iOutra.repository.ImagemProdutoRepository;
import com.example.iOutra.repository.ProdutoRepository;
import com.example.iOutra.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("backoffice")
public class ProdutoController {

    @Autowired
    private Utils utils;

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private ImagemProdutoRepository imgRepository;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("produtos")
    public String listarProdutos(Model model, Authentication authentication, @RequestParam(name = "page", defaultValue = "0") int page) {

        // Define o tamanho da página (quantidade de produtos por página)
        int pageSize = 10;

        // Obtém a lista de usuários cadastrados no backoffice.
        Page<Produto> produtosPage = repository.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

        // Adiciona a lista de usuários ao modelo para exibição na página.
        model.addAttribute("produtos", produtosPage.getContent());
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", produtosPage.getTotalPages());

        // Retorna o nome da página de listagem de usuários.
        return "backoffice/produto/lista_produtos";

    }

    @GetMapping("/produto")
    public String obterporId(@RequestParam(name = "id", required = false) Long id, Model model, Authentication authentication) {
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));

        Optional<Produto> produtoVisualizar = produtoService.buscarProdutoPorId(id);

        if (produtoVisualizar.isPresent()) {
            Produto produto = produtoVisualizar.get();
            model.addAttribute("produto", produto);
            return "/backoffice/produto/visualizar";
        } else {
            return "redirect:/produto/lista_produto";
        }
    }

    @GetMapping("buscar")
    public String procurar(Model model, @RequestParam(name = "name", required = false) String name, Authentication authentication, @RequestParam(name = "page", defaultValue = "0") int page) {
    
        // Define o tamanho da página (quantidade de produtos por página)
        int pageSize = 10;
    
        // Realiza a busca paginada dos produtos pelo nome.
        Page<Produto> produtosPage = repository.findByNameContainingIgnoreCaseOrderByIdDesc(name, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
    
        // Adiciona a lista de produtos ao modelo para exibição na página.
        model.addAttribute("produtos", produtosPage.getContent());
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", produtosPage.getTotalPages());
    
        return "backoffice/produto/lista_produtos";
    }
    

    @GetMapping("produtos/{id}")
    public String handleBackofficeGetProduto(@PathVariable Long id, Model model, Authentication authentication) {
        Produto produto = repository.findById(id).orElseThrow();
        model.addAttribute("produto", produto);
        model.addAttribute("imagens", produto.getImagens());
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/produto/form_produto";
    }

    @GetMapping("produtos/cadastro")
    public String cadastrar(Produto produto, Model model, Authentication authentication) {
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/produto/form_produto";
    }

    public boolean erroImagens(List<ImagemProdutoDto> imagens) {

        if (imagens != null) {
            int principalCount = 0;

            for (ImagemProdutoDto imgDto : imagens) {
                if (imgDto.isPrincipal())
                    principalCount++;
            }

            if(principalCount == 0 && imagens.size() >= 1){
                return true;
            }

            if (principalCount > 1 && imagens.size() > 1) {
                return true;
            }

        }

        return false;
    }

    @PostMapping("produto/cadastra")
    public String cadastra(@Valid ProdutoDto dto, BindingResult result, Model model, Authentication authentication)
            throws IOException {

        if (result.hasErrors() || erroImagens(dto.getImagens())) {
            return "backoffice/produto/form_produto";
        }

        String uploadDir = "src/main/resources/static/img/produtos/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Produto entity = new Produto();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());
        entity.setRating(dto.getRating());
        entity.setStockQuantity(dto.getStockQuantity());

        List<ImagemProduto> imagensEntities = new ArrayList<>();


        if (dto.getImagens() != null && !dto.getImagens().isEmpty()) {
            for (ImagemProdutoDto imgDto : dto.getImagens()) {
                ImagemProduto imgEntity = new ImagemProduto();
                imgEntity.setNomeArquivo(imgDto.getArquivo().getOriginalFilename());
                try {
                    imgEntity.setArquivo(imgDto.getArquivo().getBytes());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                imgEntity.setPrincipal(imgDto.isPrincipal());
                imgEntity.setProduto(entity);
                imagensEntities.add(imgEntity);

                Path destino = uploadPath.resolve(imgEntity.getNomeArquivo());
                Files.write(destino, imgEntity.getArquivo());
            }
        }

        entity.setImagens(imagensEntities);
        repository.save(entity);
        return "redirect:/backoffice/produtos";
    }

    @PostMapping("produto/edita")
    public String edita(@Valid ProdutoDto dto, BindingResult result, Model model) throws Exception {

        if (result.hasErrors() || erroImagens(dto.getImagens())) {
            return "backoffice/produto/form_produto";
        }

        String uploadDir = "src/main/resources/static/img/produtos/";
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Fetch the existing product entity from the database
        Produto entity = repository.findById(Long.valueOf(dto.getId())).orElseThrow();
        entity.setName(dto.getName());
        entity.setPrice(dto.getPrice());
        entity.setStockQuantity(dto.getStockQuantity());
        entity.setRating(dto.getRating());
        entity.setDescription(dto.getDescription());

        // Handle image updates
        if (dto.getImagens() != null && !dto.getImagens().isEmpty()) {

            List<ImagemProduto> imagensEntities = new ArrayList<>();
            for (int i = 0; i < dto.getImagens().size(); i++) {
                ImagemProdutoDto imgDto = dto.getImagens().get(i);

                // Verifying if the index is within bounds
                if (i < entity.getImagens().size()) {
                    ImagemProduto imgDb = entity.getImagens().get(i);
                    if (imgDto.getArquivo().getOriginalFilename().trim().isEmpty()) {
                        // Se não houver bytes de arquivo, presume-se que a imagem já existe no banco de
                        // dados
                        imgDb.setPrincipal(imgDto.isPrincipal());
                        imagensEntities.add(imgDb); // Adiciona a imagem existente à lista de entidades
                    } else {
                        // Se houver bytes de arquivo, presume-se que seja uma nova imagem ou uma
                        // atualização

                        ImagemProduto updateImage = imgDb;
                                        
                        Path apagar = uploadPath.resolve(updateImage.getNomeArquivo());

                        try {
                            Files.delete(apagar);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        updateImage.setNomeArquivo(imgDto.getArquivo().getOriginalFilename());
                        updateImage.setArquivo(imgDto.getArquivo().getBytes());
                        updateImage.setPrincipal(imgDto.isPrincipal());
                        updateImage.setProduto(entity);
                        imagensEntities.add(updateImage);

                        Path destino = uploadPath.resolve(updateImage.getNomeArquivo());
                        Files.write(destino, updateImage.getArquivo());
                    }
                } else {
                    // Se o índice do DTO estiver fora dos limites da lista de entidades,
                    // é uma nova imagem e deve ser adicionada à lista de entidades
                    ImagemProduto newImageEntity = new ImagemProduto();
                    newImageEntity.setNomeArquivo(imgDto.getArquivo().getOriginalFilename());
                    newImageEntity.setArquivo(imgDto.getArquivo().getBytes());
                    newImageEntity.setPrincipal(imgDto.isPrincipal());
                    newImageEntity.setProduto(entity);
                    imagensEntities.add(newImageEntity);

                    Path destino = uploadPath.resolve(newImageEntity.getNomeArquivo());
                    Files.write(destino, newImageEntity.getArquivo());
                }
            }
            // Atualizar a lista de imagens do produto e salvar no banco de dados
            entity.setImagens(imagensEntities);
        }

        repository.save(entity);

        return "redirect:/backoffice/produtos";
    }

    @GetMapping("produtos/{id}/inativar")
    public String inativarProduto(@PathVariable Long id) {
        // Busca o produto no banco de dados pelo ID
        Optional<Produto> produtoOptional = repository.findById(id);

        // Verifica se o produto foi encontrado
        if (produtoOptional.isPresent()) {
            // Produto encontrado, obtém o produto
            Produto produto = produtoOptional.get();

            // Realiza a lógica para inativar o produto (por exemplo, alterar o status)
            produto.setActive(false);

            // Salva as alterações no banco de dados
            repository.save(produto);

            // Redireciona para a página de listagem de produtos
            return "redirect:/backoffice/produtos";
        } else {

            return "redirect:/backoffice/produtos?error=Produto não encontrado";
        }
    }

    @GetMapping("produtos/{id}/reativar")
    public String reativarProduto(@PathVariable Long id) {
        // Busca o produto no banco de dados pelo ID
        Optional<Produto> produtoOptional = repository.findById(id);

        // Verifica se o produto foi encontrado
        if (produtoOptional.isPresent()) {
            // Produto encontrado, obtém o produto
            Produto produto = produtoOptional.get();

            // Realiza a lógica para reativar o produto (por exemplo, alterar o status)
            produto.setActive(true);

            // Salva as alterações no banco de dados
            repository.save(produto);

            // Redireciona para a página de listagem de produtos
            return "redirect:/backoffice/produtos";
        } else {

            return "redirect:/backoffice/produtos?error=Produto não encontrado";
        }
    }


    @DeleteMapping("/produto/removerimg/{id}")
    public String removeImg(@PathVariable Long id) {

        ImagemProduto imgEntity = imgRepository.findById(id).orElseThrow();

        String uploadDir = "src/main/resources/static/img/produtos/";
        Path uploadPath = Paths.get(uploadDir);

        Path apagar = uploadPath.resolve(imgEntity.getNomeArquivo());

        try {
            Files.delete(apagar);
        } catch (Exception e) {
            // TODO: handle exception
        }
        

        imgRepository.deleteById(id);
        return "redirect:/backoffice/produtos";
    }
}
