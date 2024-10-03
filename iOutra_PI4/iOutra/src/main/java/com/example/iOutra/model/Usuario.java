package com.example.iOutra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "Usuario")
public class Usuario {

public Usuario(String email, String senha){
    this.email = email;
    this.senha = senha;
}

public Usuario(){

}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O preenchimento do nome é obrigatório!")
    private String nome;

    @Column(unique = true)
    @NotBlank(message = "O preenchimento do CPF é obrigatório!")
    @Size(min = 11, max = 11, message = "O CPF deve conter 11 dígitos.")
    private String cpf;

    @Column(unique = true, nullable = false)
    @Email(message = "Formato de e-mail inválido!")
    private String email;

    private String grupo;

    private boolean isActive = true;
    @NotBlank(message = "A função do usuario é obrigatória")
    private String role;

// private String status; //definir se vou usar essa ou boolean    

    @NotEmpty(message = "A senha é obrigatória!")
    @Size(min = 4, max = 20, message = "A senha deve conter entre 4 e 20 caracteres!")
    private String senha;

    @NotEmpty(message = "A senha é obrigatória!")
    @Size(min = 4, max = 20, message = "A senha deve conter entre 4 e 20 caracteres!")
    private String confirmaSenha;

}




