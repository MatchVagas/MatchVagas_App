package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CadastroRequest {

    @SerializedName("nome")
    private final String nome;

    @SerializedName("email")
    private final String email;

    @SerializedName("senha")
    private final String senha;

    @SerializedName("dataNascimento")
    private final String dataNascimento;

    @SerializedName("tipoUsuario")
    private final String tipoUsuario;

    @SerializedName("ativo")
    private final Boolean ativo;

    public CadastroRequest(String nome, String email, String senha,
                           String dataNascimento, String tipoUsuario, Boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.tipoUsuario = tipoUsuario;
        this.ativo = ativo;
    }
}
