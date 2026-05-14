package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class CandidatoPerfilResponse {

    @SerializedName("cpf")
    private String cpf;

    @SerializedName("objetivoProfissional")
    private String objetivoProfissional;

    @SerializedName("disponibilidade")
    private String disponibilidade;

    @SerializedName("pretensaoSalarial")
    private BigDecimal pretensaoSalarial;

    // Construtor para uso interno no PerfilRepository
    public CandidatoPerfilResponse(String cpf, String objetivoProfissional,
                                   String disponibilidade, BigDecimal pretensaoSalarial) {
        this.cpf = cpf;
        this.objetivoProfissional = objetivoProfissional;
        this.disponibilidade = disponibilidade;
        this.pretensaoSalarial = pretensaoSalarial;
    }

    public String getCpf()                   { return cpf; }
    public String getObjetivoProfissional()  { return objetivoProfissional; }
    public String getDisponibilidade()       { return disponibilidade; }
    public BigDecimal getPretensaoSalarial() { return pretensaoSalarial; }
}
