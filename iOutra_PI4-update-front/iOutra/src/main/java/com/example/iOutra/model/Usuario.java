package com.example.iOutra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table
public class Usuario {



    public interface New {
        // Marcador de interface vazio para representar validações para novos objetos
    }

    public interface Existing {
        // Marcador de interface vazio para representar validações para objetos existentes
    }

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Usuario(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome completo é obrigatório")
    private String fullname;


    @Column(unique = true)
    @NotBlank(message = "O CPF é obrigatório")
    @Size(min = 11, max = 11, message = "O CPF deve ter exatamente 11 dígitos")
    private String cpf;

    private String grupo;

    @Column(unique = true)
    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;

    private boolean isActive = true;

    @NotBlank(message = "A senha é obrigatória", groups = {New.class})
    @Size(min = 4, message = "A senha deve ter pelo menos 4 caracteres", groups = {New.class})
    @Null(message = "A senha não pode ser vazia", groups = {New.class})
    private String password;

    @NotBlank(message = "A função do usuário é obrigatória")
    private String role;

}


