package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class ExperienciaRequest {

    @SerializedName("empresa")
    private final String empresa;

    @SerializedName("cargo")
    private final String cargo;

    @SerializedName("descricao")
    private final String descricao; // nullable

    @SerializedName("dataInicio")
    private final String dataInicio; // "MM/yyyy"

    @SerializedName("dataFim")
    private final String dataFim; // null ou "MM/yyyy"

    public ExperienciaRequest(String empresa, String cargo, String descricao,
                              String dataInicio, String dataFim) {
        this.empresa = empresa;
        this.cargo = cargo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
}
