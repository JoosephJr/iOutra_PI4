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
    public String exibirListaUsuarios(Model model){
        List<Usuario> usuarios = userRepo.findAll(Sort.by(Sort.Direction.DESC, "idUsuario"));
        model.addAttribute("usuarios", usuarios);
        return "usuarios/index";
    }

    @GetMapping("/cadastrarUsuario")
    public String exibirCadastrarUsuario(Model model){
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

        Usuario usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setCpf(usuarioDTO.getCpf());
        usuario.setGrupo(usuarioDTO.getGrupo());
        usuario.setSenha(usuarioDTO.getSenha()); //verificar!!!

        userRepo.save(usuario);


        return "redirect:/usuarios";
    }


    @GetMapping("/editarUsuario")
    public String exibirEditarUsuario (Model model, @RequestParam int idUsuario){
        Usuario usuario = userRepo.findById(idUsuario).get();

        if (usuario == null) {
            return "redirect:/usuarios";
        }

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNome(usuario.getNome());
        usuarioDTO.setCpf(usuario.getCpf());
        usuarioDTO.setGrupo(usuario.getGrupo());
        usuarioDTO.setSenha(usuario.getSenha());

        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioDTO", usuarioDTO);

        return "usuarios/editarUsuario";
    }


}
