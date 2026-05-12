package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class CandidaturaRequest {

    @SerializedName("vagaId")
    private final Long vagaId;

    @SerializedName("compartilharObjetivoProfissional")
    private final Boolean compartilharObjetivoProfissional;

    @SerializedName("compartilharDisponibilidade")
    private final Boolean compartilharDisponibilidade;

    @SerializedName("compartilharPretensaoSalarial")
    private final Boolean compartilharPretensaoSalarial;

    @SerializedName("compartilharCurriculo")
    private final Boolean compartilharCurriculo;

    @SerializedName("compartilharExperiencias")
    private final Boolean compartilharExperiencias;

    @SerializedName("compartilharFormacoes")
    private final Boolean compartilharFormacoes;

    @SerializedName("compartilharTelefone")
    private final Boolean compartilharTelefone;

    @SerializedName("compartilharEndereco")
    private final Boolean compartilharEndereco;

    public CandidaturaRequest(Long vagaId,
                               Boolean compartilharObjetivoProfissional,
                               Boolean compartilharDisponibilidade,
                               Boolean compartilharPretensaoSalarial,
                               Boolean compartilharCurriculo,
                               Boolean compartilharExperiencias,
                               Boolean compartilharFormacoes,
                               Boolean compartilharTelefone,
                               Boolean compartilharEndereco) {
        this.vagaId = vagaId;
        this.compartilharObjetivoProfissional = compartilharObjetivoProfissional;
        this.compartilharDisponibilidade = compartilharDisponibilidade;
        this.compartilharPretensaoSalarial = compartilharPretensaoSalarial;
        this.compartilharCurriculo = compartilharCurriculo;
        this.compartilharExperiencias = compartilharExperiencias;
        this.compartilharFormacoes = compartilharFormacoes;
        this.compartilharTelefone = compartilharTelefone;
        this.compartilharEndereco = compartilharEndereco;
    }
}
