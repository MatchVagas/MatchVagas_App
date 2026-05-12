package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class FormacaoRequest {

    @SerializedName("instituicao")
    private final String instituicao;

    @SerializedName("curso")
    private final String curso;

    @SerializedName("grau")
    private final String grau;

    @SerializedName("mesInicio")
    private final String mesInicio;

    @SerializedName("anoInicio")
    private final String anoInicio;

    @SerializedName("mesConclusao")
    private final String mesConclusao;

    @SerializedName("anoConclusao")
    private final String anoConclusao;

    @SerializedName("aindaCursando")
    private final boolean aindaCursando;

    public FormacaoRequest(String instituicao, String curso, String grau,
                           String mesInicio, String anoInicio,
                           String mesConclusao, String anoConclusao,
                           boolean aindaCursando) {
        this.instituicao = instituicao;
        this.curso = curso;
        this.grau = grau;
        this.mesInicio = mesInicio;
        this.anoInicio = anoInicio;
        this.mesConclusao = mesConclusao;
        this.anoConclusao = anoConclusao;
        this.aindaCursando = aindaCursando;
    }
}
