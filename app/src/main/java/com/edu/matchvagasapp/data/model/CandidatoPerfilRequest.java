package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CandidatoPerfilRequest {

    @SerializedName("cpf")
    private final String cpf;

    @SerializedName("telefone")
    private final TelefoneRequest telefone;

    @SerializedName("localizacao")
    private final LocalizacaoRequest localizacao;

    public CandidatoPerfilRequest(String cpf, TelefoneRequest telefone, LocalizacaoRequest localizacao) {
        this.cpf = cpf;
        this.telefone = telefone;
        this.localizacao = localizacao;
    }
}
