package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class ExperienciaResponse {

    @SerializedName("id")          private Long id;
    @SerializedName("candidatoId") private Long candidatoId;
    @SerializedName("empresa")     private String empresa;
    @SerializedName("cargo")       private String cargo;
    @SerializedName("descricao")   private String descricao;
    @SerializedName("dataInicio")  private String dataInicio; // "MM/yyyy"
    @SerializedName("dataFim")     private String dataFim;    // null ou "MM/yyyy"

    public Long getId()             { return id; }
    public Long getCandidatoId()    { return candidatoId; }
    public String getEmpresa()      { return empresa; }
    public String getCargo()        { return cargo; }
    public String getDescricao()    { return descricao; }
    public String getDataInicio()   { return dataInicio; }
    public String getDataFim()      { return dataFim; }

    /** Retorna true se dataFim for null ou vazio (emprego atual). */
    public boolean isEmpregoAtual() { return dataFim == null || dataFim.isEmpty(); }
}
