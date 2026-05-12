package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class ExperienciaRequest {

    @SerializedName("cargo")
    private final String cargo;

    @SerializedName("empresa")
    private final String empresa;

    @SerializedName("modalidade")
    private final String modalidade;

    @SerializedName("vinculo")
    private final String vinculo;

    @SerializedName("cidade")
    private final String cidade;

    @SerializedName("mesInicio")
    private final String mesInicio;

    @SerializedName("anoInicio")
    private final String anoInicio;

    @SerializedName("mesSaida")
    private final String mesSaida;

    @SerializedName("anoSaida")
    private final String anoSaida;

    @SerializedName("empregoAtual")
    private final boolean empregoAtual;

    public ExperienciaRequest(String cargo, String empresa, String modalidade,
                              String vinculo, String cidade, String mesInicio,
                              String anoInicio, String mesSaida, String anoSaida,
                              boolean empregoAtual) {
        this.cargo = cargo;
        this.empresa = empresa;
        this.modalidade = modalidade;
        this.vinculo = vinculo;
        this.cidade = cidade;
        this.mesInicio = mesInicio;
        this.anoInicio = anoInicio;
        this.mesSaida = mesSaida;
        this.anoSaida = anoSaida;
        this.empregoAtual = empregoAtual;
    }
}
