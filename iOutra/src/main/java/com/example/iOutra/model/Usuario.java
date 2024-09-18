package com.example.iOutra.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Usuario")
public class Usuario {
    
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int idUsuario;

@NotBlank (message = "O preenchimento do nome é obrigatório!")
private String nome;

@Column(unique = true)
@NotBlank (message = "O preenchimento do CPF é obrigatório!")
@Size(min = 11, max = 11, message = "O CPF deve conter 11 dígitos.")
private String cpf;

@Column(unique = true, nullable = false)
@Email (message = "Formato de e-mail inválido!")
private String email;

@Enumerated(EnumType.STRING)
private com.example.iOutra.model.Role grupo;

private boolean isActive = true;

// private String status; //definir se vou usar essa ou boolean    

@NotEmpty(message = "A senha é obrigatória!")
@Size(min = 4, max = 20, message = "A senha deve conter entre 4 e 20 caracteres!")
private String senha;

public int getIdUsuario() {
    return idUsuario;
}

public void setIdUsuario(int idUsuario) {
    this.idUsuario = idUsuario;
}

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

public com.example.iOutra.model.Role getGrupo() {
    return grupo;
}

public void setGrupo(Role grupo) {
    this.grupo = grupo;
}

public boolean isActive() {
    return isActive;
}

public void setActive(boolean isActive) {
    this.isActive = isActive;
}

// public String getStatus() {
//     return status;
// }

// public void setStatus(String status) {
//     this.status = status;
// }

public String getSenha() {
    return senha;
}

public void setSenha(String senha) {
    this.senha = senha;
}



}
