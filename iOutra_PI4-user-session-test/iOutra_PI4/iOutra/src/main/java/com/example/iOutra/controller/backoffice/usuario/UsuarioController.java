package com.example.iOutra.controller.backoffice.usuario;


import com.example.iOutra.controller.backoffice.Utils;
import com.example.iOutra.model.Usuario;
import com.example.iOutra.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("backoffice")
public class UsuarioController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    UsuarioRepository repository;

    @Autowired
    private Utils utils;

    // USUARIOS REST - GET / POST / PUT / DELETE


@GetMapping("usuarios/login")
public String handleBackofficeLogin(Usuario usuario) {
    return "backoffice/login";
}

    @GetMapping("usuarios")
    public String listarUsuarios(Model model, Authentication authentication) {

        // Obtém a lista de usuários cadastrados no backoffice.
        List<Usuario> usuarios = repository.findAll();

        // Adiciona a lista de usuários ao modelo para exibição na página.
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));

        // Retorna o nome da página de listagem de usuários.
        return "backoffice/usuario/lista_usuarios";
    }

    @GetMapping("procurar")
    public String procurar(Model model, @RequestParam(name = "fullname", required = false) String fullname,
            Authentication authentication) {

        List<Usuario> usuarios = repository.findByFullnameContainingIgnoreCase(fullname);

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/usuario/lista_usuarios";
    }

    // Método para habilitar ou desabilitar o usuário
    @PostMapping("trocar-status-usuario/{id}")
    public String habilitaDesabilita(@PathVariable long id) {
        Usuario usuario = repository.findById(id).get();
        usuario.setActive(!usuario.isActive());
        repository.save(usuario);
        return "redirect:/backoffice/usuarios";
    }
      
    @GetMapping("usuarios/cadastro")
    public String cadastrar(Usuario usuario, Model model, Authentication authentication) {
        model.addAttribute("usuarioAutenticado", utils.getUsuarioAutenticado(authentication));
        return "backoffice/usuario/form_usuario";
    }


    @GetMapping("usuarios/{id}")
    public String handleBackofficeGetUsuario(@PathVariable Long id, Model model, Authentication authentication) {
        Usuario usuario = repository.findById(id).orElseThrow();
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
        if (!ValidaCPF.isCPF(usuario.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "Cpf inválido");
        }

        // Validar senha vazia
        if (usuario.getPassword().trim().isEmpty() || usuario.getPassword().length() < 4) {
            result.rejectValue("password", "error.password", "Senha ter pelo menos 4 caracteres");
        }

        // Valida se já existe esse email ou cpf
        if (repository.existsByEmail(usuario.getEmail())) {
            result.rejectValue("username", "error.username", "Email já cadastrado");
        }

        if (repository.existsByCpf(usuario.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "Cpf já cadastrado");
        }

        if (result.hasErrors()) {
            return "backoffice/usuario/form_usuario";
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        repository.save(usuario);

        return "redirect:/backoffice/usuarios";
    }

    @PostMapping("usuario/edita")
    public String handleBackofficeEditar(@Valid Usuario usuario, BindingResult result, Authentication authentication, Model model) {

        Usuario usuarioAutenticado = utils.getUsuarioAutenticado(authentication);
        model.addAttribute("usuarioAutenticado", usuarioAutenticado);
        model.addAttribute("usuario", usuario);

        // Valida o CPF
        if (!ValidaCPF.isCPF(usuario.getCpf())) {
            result.rejectValue("cpf", "error.cpf", "Cpf inválido");
        }

        // Valida se há outra usuário já cadastrado com esse CPF a não ser ele próprio
        if (repository.existsByCpfAndIdNot(usuario.getCpf(), usuario.getId())) {
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
        if (usuario.getPassword() != null && !usuario.getPassword().isEmpty()) {
            // If a new password is provided, encode and set it
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            // If no new password is provided, retain the existing password
            Usuario existingUser = repository.findById(usuario.getId()).orElse(null);
            if (existingUser != null) {
                usuario.setPassword(existingUser.getPassword());
            }
        }

        repository.save(usuario);

        return "redirect:/backoffice/usuarios";
    }

}
