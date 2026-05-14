package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class HabilidadesRequest {

    @SerializedName("nome")
    private final String nome;

    @SerializedName("nivel")
    private final String nivel; // nullable

    public HabilidadesRequest(String nome, String nivel) {
        this.nome  = nome;
        this.nivel = nivel;
    }
}
