package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class TelefoneRequest {

    @SerializedName("numero")
    private final String numero;

    @SerializedName("tipoTelefoneId")
    private final long tipoTelefoneId;

    @SerializedName("wpp")
    private final boolean wpp;

    public TelefoneRequest(String numero, boolean wpp) {
        this.numero = numero;
        this.tipoTelefoneId = 1L;
        this.wpp = wpp;
    }
}
