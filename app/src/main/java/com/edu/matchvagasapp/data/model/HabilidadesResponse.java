package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HabilidadesResponse {

    @SerializedName("habilidades")
    private List<String> habilidades;

    public List<String> getHabilidades() { return habilidades; }
}
