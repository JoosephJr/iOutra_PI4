package com.example.iOutra.DTO;

import com.example.iOutra.model.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReqRes {

    private int statusCOD;
    private String erro;
    private String mensagem;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String email;
    private String nome;
    private String senha;
    private String cpf;
    private String role;
    private Usuario usuario;
    private List<Usuario> usuarioList;
}
