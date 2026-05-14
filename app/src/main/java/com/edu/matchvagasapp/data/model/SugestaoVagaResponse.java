package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SugestaoVagaResponse {

    @SerializedName("vaga")
    private VagaResponse vaga;

    @SerializedName("pontuacao")
    private int pontuacao;

    @SerializedName("motivos")
    private List<String> motivos;

    public VagaResponse getVaga()        { return vaga; }
    public int getPontuacao()            { return pontuacao; }
    public List<String> getMotivos()     { return motivos; }
}
