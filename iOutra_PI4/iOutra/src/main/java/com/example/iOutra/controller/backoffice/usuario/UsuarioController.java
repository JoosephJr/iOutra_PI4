package com.example.iOutra.controller.backoffice.usuario;

import java.util.List;

import com.example.iOutra.controller.ValidaCpf;
import com.example.iOutra.controller.backoffice.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("backoffice")
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private Utils utils;

    @GetMapping("usuarios")
    public String listarUsuarios(Model model, Authentication authentication) {

        List <Usuario> usuariosList = usuarioRepository.findAll();

        model.addAttribute("usuarios", usuariosList);
        model.addAttribute("usuarioAutenticado" ,  utils.getUsuarioAutenticado(authentication) );

        return "backoffice/usuario/lista-usuarios";
    }

    @GetMapping("procurar")
    public String procurar (Model model, @RequestParam(name = "nome", required = false) String nome,
                            Authentication authentication) {
        List <Usuario> usuarios = usuarioRepository.findByNomeContainingIgnoreCase(nome);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuariosAutenticado" , utils.getUsuarioAutenticado(authentication));
        return "backoffice/usuario/lista-usuarios";
    }

    @GetMapping("usuarios/cadastro")
    public String cadastrar(Usuario usuario, Model model, Authentication authentication) {
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/usuario/form_usuario";
    }

    @GetMapping("usuarios/{id}")
    public String handleBackOfficeGetUsuario(@PathVariable Long id, Model model, Authentication authentication) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow();
        model.addAttribute("usuario", usuario);
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/usuario/form_usuario";

    }
    @PostMapping("usuario/cadastra")
    public String salvaFormulario(@Valid Usuario usuario, BindingResult result, Authentication authentication, Model model) {

        Usuario usuarioAutenticado = utils.getUsuarioAutenticado(authentication);
        model.addAttribute("usuarioAutenticado", usuarioAutenticado);
        model.addAttribute("usuario", usuario);

        // Valida o CPF
        if (!ValidaCpf.isCPF(usuario.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "Cpf inválido");
        }

        // Validar senha vazia
        if (usuario.getSenha().trim().isEmpty() || usuario.getSenha().length() < 4) {
            result.rejectValue("password", "error.password", "Senha ter pelo menos 4 caracteres");
        }

        // Valida se já existe esse email ou cpf
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            result.rejectValue("username", "error.username", "Email já cadastrado");
        }

        if (usuarioRepository.existsByCpf(usuario.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "Cpf já cadastrado");
        }

        if (result.hasErrors()) {
            return "backoffice/usuario/form_usuario";
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario);

        return "redirect:/backoffice/usuarios";
    }


    @PostMapping("usuario/edita")
    public String handleBackofficeEditar(@Valid Usuario usuario, BindingResult result, Authentication authentication, Model model) {

        Usuario usuarioAutenticado = utils.getUsuarioAutenticado(authentication);
        model.addAttribute("usuarioAutenticado", usuarioAutenticado);
        model.addAttribute("usuario", usuario);

        // Valida o CPF
        if (!ValidaCpf.isCPF(usuario.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "Cpf inválido");
        }

        // Valida se há outra usuário já cadastrado com esse CPF a não ser ele próprio
        if (usuarioRepository.existsByCpfAndIdNot(usuario.getCpf(), usuario.getId())) {
            result.rejectValue("cpf", "error.cpf", "Cpf já cadastrado");
        }

        // Usuário logado não pode trocar o seu próprio grupo de acesso
        if (usuario.getId().equals(usuarioAutenticado.getId()) &&
                !usuarioAutenticado.getRole().equals(usuario.getRole())) {
            result.rejectValue("role", "error.role", "Usuário não pode trocar o seu grupo");
        }

        if (result.hasErrors()) {
            return "backoffice/usuario/form_usuario";
        }

        // Check if a new password is provided
        if (usuario.getSenha() != null && !usuario.getSenha().isEmpty()) {
            // If a new password is provided, encode and set it
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        } else {
            // If no new password is provided, retain the existing password
            Usuario existingUser = usuarioRepository.findById(usuario.getId()).orElse(null);
            if (existingUser != null) {
                usuario.setSenha(existingUser.getSenha());
            }
        }

        usuarioRepository.save(usuario);

        return "redirect:/backoffice/usuarios";
    }



}
