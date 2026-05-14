package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CandidatoAtualizarRequest {

    @SerializedName("cpf")
    private final String cpf;

    @SerializedName("resumoProfissional")
    private final String resumoProfissional;

    @SerializedName("disponibilidade")
    private final String disponibilidade;

    @SerializedName("pretensaoSalarial")
    private final BigDecimal pretensaoSalarial;

    public CandidatoAtualizarRequest(String cpf, String resumoProfissional,
                                     String disponibilidade, BigDecimal pretensaoSalarial) {
        this.cpf = cpf;
        this.resumoProfissional = resumoProfissional;
        this.disponibilidade = disponibilidade;
        this.pretensaoSalarial = pretensaoSalarial;
    }
}
