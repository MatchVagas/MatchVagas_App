package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class ExperienciaResponse {

    @SerializedName("id")           private Long id;
    @SerializedName("cargo")        private String cargo;
    @SerializedName("empresa")      private String empresa;
    @SerializedName("modalidade")   private String modalidade;
    @SerializedName("vinculo")      private String vinculo;
    @SerializedName("cidade")       private String cidade;
    @SerializedName("mesInicio")    private String mesInicio;
    @SerializedName("anoInicio")    private String anoInicio;
    @SerializedName("mesSaida")     private String mesSaida;
    @SerializedName("anoSaida")     private String anoSaida;
    @SerializedName("empregoAtual") private boolean empregoAtual;

    public Long getId()             { return id; }
    public String getCargo()        { return cargo; }
    public String getEmpresa()      { return empresa; }
    public String getModalidade()   { return modalidade; }
    public String getVinculo()      { return vinculo; }
    public String getCidade()       { return cidade; }
    public String getMesInicio()    { return mesInicio; }
    public String getAnoInicio()    { return anoInicio; }
    public String getMesSaida()     { return mesSaida; }
    public String getAnoSaida()     { return anoSaida; }
    public boolean isEmpregoAtual() { return empregoAtual; }
}
