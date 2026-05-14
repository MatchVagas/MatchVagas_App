package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class FormacaoRequest {

    @SerializedName("instituicao")
    private final String instituicao;

    @SerializedName("curso")
    private final String curso;

    @SerializedName("nivel")
    private final String nivel;

    @SerializedName("dataInicio")
    private final String dataInicio;

    @SerializedName("dataFim")
    private final String dataFim;

    public FormacaoRequest(String instituicao, String curso, String nivel,
                           String dataInicio, String dataFim) {
        this.instituicao = instituicao;
        this.curso = curso;
        this.nivel = nivel;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }
}
