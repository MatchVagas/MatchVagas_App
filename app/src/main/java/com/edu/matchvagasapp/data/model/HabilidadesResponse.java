package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HabilidadesResponse {

    @SerializedName("nome")
    private String nome;

    @SerializedName("nivel")
    private String nivel; // nullable

    public String getNome()  { return nome; }
    public String getNivel() { return nivel; }

    /** Compatibilidade retroativa — retorna null (não usado pelo backend novo). */
    public List<String> getHabilidades() { return null; }
}
