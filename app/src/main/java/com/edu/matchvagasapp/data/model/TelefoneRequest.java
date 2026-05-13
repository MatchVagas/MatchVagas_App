package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class TelefoneRequest {

    @SerializedName("numero")
    private final String numero;

    @SerializedName("tipoTelefoneId")
    private final Long tipoTelefoneId;

    @SerializedName("wpp")
    private final boolean wpp;

    public TelefoneRequest(String numero, Long tipoTelefoneId, boolean wpp) {
        this.numero = numero;
        this.tipoTelefoneId = tipoTelefoneId;
        this.wpp = wpp;
    }
}
