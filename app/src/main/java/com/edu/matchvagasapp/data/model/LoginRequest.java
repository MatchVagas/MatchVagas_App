package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("email")
    private final String email;

    @SerializedName("senha")
    private final String senha;

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }
}
