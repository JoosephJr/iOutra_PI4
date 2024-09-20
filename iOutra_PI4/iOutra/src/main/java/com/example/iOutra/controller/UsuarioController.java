package com.example.iOutra.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import com.example.iOutra.model.Usuario;
import com.example.iOutra.model.UsuarioDTO;
import com.example.iOutra.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository userRepo;

    @GetMapping({ "", "/" })
    public String exibirListaUsuarios(Model model) {
        List<Usuario> usuarios = userRepo.findAll(Sort.by(Sort.Direction.DESC, "idUsuario"));
        model.addAttribute("usuarios", usuarios);
        return "usuarios/index";
    }

    @GetMapping("/cadastrarUsuario")
    public String exibirCadastrarUsuario(Model model) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        model.addAttribute("usuarioDTO", usuarioDTO);
        return "usuarios/cadastrarUsuario";
    }

    @PostMapping("/cadastrarUsuario")
    public String cadastrarUsuario(@Valid @ModelAttribute UsuarioDTO usuarioDTO,
    BindingResult result){

        if (userRepo.findByEmail(usuarioDTO.getEmail()) != null) {
            result.addError(new FieldError("usuarioDTO", "email", usuarioDTO.getEmail(),
            false, null, null, "Endereço de e-mail já está sendo utilizado!"));
        }

        if (result.hasErrors()) {
            return "usuarios/cadastrarUsuario";
        }

        if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmaSenha())) {
            result.addError(new FieldError("usuarioDTO", "senha", "As senhas não são iguais!"));
        }

        if (result.hasErrors()) {
            return "usuarios/cadastrarUsuario";
        }


        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setGrupo(usuarioDTO.getGrupo());
        usuario.setSenha(usuarioDTO.getSenha()); //verificar!!!
        usuario.setConfirmaSenha(usuarioDTO.getConfirmaSenha());

        if (!usuarioDTO.getSenha().equals(usuarioDTO.getConfirmaSenha())) {
            result.addError(new FieldError("usuarioDTO", "senha", "As senhas não são iguais!"));
        }

        if (result.hasErrors()) {
            return "usuarios/cadastrarUsuario";
        }

        userRepo.save(usuario);

        return "redirect:/usuarios";
    }

    @GetMapping("/editarUsuario")
    public String exibirEditarUsuario(Model model, @RequestParam int idUsuario) {
        Usuario usuario = userRepo.findById(idUsuario).get();

        if (usuario == null) {
            return "redirect:/usuarios";
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setCpf(usuario.getCpf());
        usuarioDTO.setGrupo(usuario.getGrupo());
        usuarioDTO.setSenha(usuario.getSenha());

        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioDTO", usuarioDTO);

        return "usuarios/editarUsuario";
    }

    @PostMapping("/editarUsuario")
    public String editarUsuario(Model model, @RequestParam int idUsuario,
            @Valid @ModelAttribute UsuarioDTO usuarioDTO, BindingResult result) {

        Usuario usuario = userRepo.findById(idUsuario).orElse(null);
        if (usuario == null) {
            return "redirect:/usuarios";
        }

        model.addAttribute("usuario", usuario);

        if (result.hasErrors()) {
            return "usuarios/editarUsuario";
        }

        usuario.setNome(usuarioDTO.getNome());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setGrupo(usuarioDTO.getGrupo());
        usuario.setSenha(usuarioDTO.getSenha());

        try {
            userRepo.save(usuario);

        } catch (Exception e) {
            // talvez mexer aqui pra pegar exceção melhor
            System.out.println(e);
        }

        return "redirect:/usuarios";
    }

    @PostMapping("/alterarStatus")
    public String alterarStatus(@RequestParam int id) {
        Usuario usuario = userRepo.findById(id).orElseThrow();
        usuario.setActive(!usuario.isActive());
        userRepo.save(usuario);
        return "redirect:/usuarios";
    }

}
