package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CandidaturaResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("candidatoId")
    private Long candidatoId;

    @SerializedName("nomeCandidato")
    private String nomeCandidato;

    @SerializedName("vagaId")
    private Long vagaId;

    @SerializedName("tituloVaga")
    private String tituloVaga;

    @SerializedName("dataCandidatura")
    private String dataCandidatura;

    @SerializedName("status")
    private String status;

    @SerializedName("compartilharCurriculo")
    private boolean compartilharCurriculo;

    @SerializedName("compartilharExperiencias")
    private boolean compartilharExperiencias;

    @SerializedName("compartilharFormacoes")
    private boolean compartilharFormacoes;

    @SerializedName("compartilharTelefone")
    private boolean compartilharTelefone;

    public Long getId() { return id; }
    public Long getCandidatoId() { return candidatoId; }
    public String getNomeCandidato() { return nomeCandidato; }
    public Long getVagaId() { return vagaId; }
    public String getTituloVaga() { return tituloVaga; }
    public String getDataCandidatura() { return dataCandidatura; }
    public String getStatus() { return status; }
    public boolean isCompartilharCurriculo() { return compartilharCurriculo; }
    public boolean isCompartilharExperiencias() { return compartilharExperiencias; }
    public boolean isCompartilharFormacoes() { return compartilharFormacoes; }
    public boolean isCompartilharTelefone() { return compartilharTelefone; }
}
