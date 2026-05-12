package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class DadosPessoaisRequest {

    @SerializedName("nome")
    private final String nome;

    @SerializedName("email")
    private final String email;

    @SerializedName("telefone")
    private final String telefone;

    @SerializedName("dataNascimento")
    private final String dataNascimento;

    @SerializedName("cpf")
    private final String cpf;

    @SerializedName("genero")
    private final String genero;

    @SerializedName("cep")
    private final String cep;

    @SerializedName("cidade")
    private final String cidade;

    @SerializedName("estado")
    private final String estado;

    @SerializedName("linkedin")
    private final String linkedin;

    @SerializedName("portfolio")
    private final String portfolio;

    public DadosPessoaisRequest(String nome, String email, String telefone,
                                String dataNascimento, String cpf, String genero,
                                String cep, String cidade, String estado,
                                String linkedin, String portfolio) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.cpf = cpf;
        this.genero = genero;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.linkedin = linkedin;
        this.portfolio = portfolio;
    }
}
