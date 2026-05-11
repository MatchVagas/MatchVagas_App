package com.edu.matchvagasapp.data.network;

import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.LoginRequest;
import com.edu.matchvagasapp.data.model.LoginResponse;
import com.edu.matchvagasapp.data.model.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/register")
    Call<UsuarioResponse> cadastrar(@Body CadastroRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
