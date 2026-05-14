package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class LocalizacaoRequest {

    @SerializedName("logradouro")
    private final String logradouro;

    @SerializedName("numero")
    private final String numero;

    @SerializedName("complemento")
    private final String complemento;

    @SerializedName("bairro")
    private final String bairro;

    @SerializedName("cep")
    private final String cep;

    @SerializedName("cidade")
    private final String cidade;

    @SerializedName("estado")
    private final String estado;

    public LocalizacaoRequest(String logradouro, String numero, String complemento,
                               String bairro, String cep, String cidade, String estado) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento.isEmpty() ? null : complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
    }
}
