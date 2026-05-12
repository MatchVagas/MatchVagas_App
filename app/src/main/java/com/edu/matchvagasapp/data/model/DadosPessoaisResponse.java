package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class DadosPessoaisResponse {

    @SerializedName("nome")        private String nome;
    @SerializedName("email")       private String email;
    @SerializedName("telefone")    private String telefone;
    @SerializedName("dataNascimento") private String dataNascimento;
    @SerializedName("cpf")         private String cpf;
    @SerializedName("genero")      private String genero;
    @SerializedName("cep")         private String cep;
    @SerializedName("cidade")      private String cidade;
    @SerializedName("estado")      private String estado;
    @SerializedName("linkedin")    private String linkedin;
    @SerializedName("portfolio")   private String portfolio;

    public String getNome()            { return nome; }
    public String getEmail()           { return email; }
    public String getTelefone()        { return telefone; }
    public String getDataNascimento()  { return dataNascimento; }
    public String getCpf()             { return cpf; }
    public String getGenero()          { return genero; }
    public String getCep()             { return cep; }
    public String getCidade()          { return cidade; }
    public String getEstado()          { return estado; }
    public String getLinkedin()        { return linkedin; }
    public String getPortfolio()       { return portfolio; }
}
