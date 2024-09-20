package com.example.iOutra.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class UsuarioDTO {
    
    @NotBlank (message = "O preenchimento do nome é obrigatório!")
private String nome;

@NotBlank (message = "O preenchimento do CPF é obrigatório!")
@Size(min = 11, max = 11, message = "O CPF deve conter 11 dígitos.")
private String cpf;

@NotEmpty(message = "O preenchimento do email é obrigatório!")
@Email (message = "Formato de e-mail inválido!")
private String email;

@NotEmpty(message = "O grupo é obrigatório!")
private String grupo;

@NotEmpty(message = "A senha é obrigatória!")
@Size(min = 4, max = 20, message = "A senha deve conter entre 4 e 20 caracteres!")
private String senha;

@NotEmpty(message = "A confirmação da senha é obrigatória!")
@Size(min = 4, max = 20, message = "A senha deve conter entre 4 e 20 caracteres!")
private String confirmaSenha;



public String getNome() {
    return nome;
}

public void setNome(String nome) {
    this.nome = nome;
}

public String getCpf() {
    return cpf;
}

public void setCpf(String cpf) {
    this.cpf = cpf;
}

public String getEmail() {
    return email;
}

public void setEmail(String email) {
    this.email = email;
}

public String getGrupo() {
    return grupo;
}

public void setGrupo(String grupo) {
    this.grupo = grupo;
}

public String getSenha() {
    return senha;
}

public void setSenha(String senha) {
    this.senha = senha;
}

public String getConfirmaSenha() {
    return confirmaSenha;
}

public void setConfirmaSenha(String confirmaSenha) {
    this.confirmaSenha = confirmaSenha;
}




}
