package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("usuarioId")
    private Long usuarioId;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("perfil")
    private String perfil;

    public String getToken() { return token; }
    public String getTipo() { return tipo; }
    public Long getUsuarioId() { return usuarioId; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPerfil() { return perfil; }
}
