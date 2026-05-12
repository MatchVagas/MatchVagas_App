package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class FormacaoResponse {

    @SerializedName("id")              private Long id;
    @SerializedName("instituicao")     private String instituicao;
    @SerializedName("curso")           private String curso;
    @SerializedName("grau")            private String grau;
    @SerializedName("mesInicio")       private String mesInicio;
    @SerializedName("anoInicio")       private String anoInicio;
    @SerializedName("mesConclusao")    private String mesConclusao;
    @SerializedName("anoConclusao")    private String anoConclusao;
    @SerializedName("aindaCursando")   private boolean aindaCursando;

    public Long getId()                { return id; }
    public String getInstituicao()     { return instituicao; }
    public String getCurso()           { return curso; }
    public String getGrau()            { return grau; }
    public String getMesInicio()       { return mesInicio; }
    public String getAnoInicio()       { return anoInicio; }
    public String getMesConclusao()    { return mesConclusao; }
    public String getAnoConclusao()    { return anoConclusao; }
    public boolean isAindaCursando()   { return aindaCursando; }
}
