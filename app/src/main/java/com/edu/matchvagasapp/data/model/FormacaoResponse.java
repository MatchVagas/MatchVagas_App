package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class FormacaoResponse {

    @SerializedName("id")          private Long id;
    @SerializedName("candidatoId") private Long candidatoId;
    @SerializedName("instituicao") private String instituicao;
    @SerializedName("curso")       private String curso;
    @SerializedName("nivel")       private String nivel;
    @SerializedName("dataInicio")  private String dataInicio;
    @SerializedName("dataFim")     private String dataFim;

    public Long getId()             { return id; }
    public Long getCandidatoId()    { return candidatoId; }
    public String getInstituicao()  { return instituicao; }
    public String getCurso()        { return curso; }
    public String getNivel()        { return nivel; }
    public String getDataInicio()   { return dataInicio; }
    public String getDataFim()      { return dataFim; }

    public boolean isAindaCursando() {
        return dataFim == null || dataFim.isEmpty();
    }
}
