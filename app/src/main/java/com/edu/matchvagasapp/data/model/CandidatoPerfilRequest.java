package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CandidatoPerfilRequest {

    @SerializedName("cpf")
    private final String cpf;

    @SerializedName("resumoProfissional")
    private final String resumoProfissional;

    @SerializedName("disponibilidade")
    private final String disponibilidade;

    @SerializedName("pretensaoSalarial")
    private final BigDecimal pretensaoSalarial;

    @SerializedName("telefone")
    private final TelefoneRequest telefone;

    @SerializedName("localizacao")
    private final LocalizacaoRequest localizacao;

    public CandidatoPerfilRequest(String cpf, String resumoProfissional, String disponibilidade,
                                   BigDecimal pretensaoSalarial, TelefoneRequest telefone,
                                   LocalizacaoRequest localizacao) {
        this.cpf = cpf;
        this.resumoProfissional = resumoProfissional;
        this.disponibilidade = disponibilidade;
        this.pretensaoSalarial = pretensaoSalarial;
        this.telefone = telefone;
        this.localizacao = localizacao;
    }
}
