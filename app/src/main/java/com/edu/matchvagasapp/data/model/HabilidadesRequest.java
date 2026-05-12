package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HabilidadesRequest {

    @SerializedName("habilidades")
    private final List<String> habilidades;

    public HabilidadesRequest(List<String> habilidades) {
        this.habilidades = habilidades;
    }
}
