package com.edu.matchvagasapp.data.network;

import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.CandidaturaRequest;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.model.LoginRequest;
import com.edu.matchvagasapp.data.model.LoginResponse;
import com.edu.matchvagasapp.data.model.UsuarioResponse;
import com.edu.matchvagasapp.data.model.VagaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ── Auth ─────────────────────────────────────────────────────────────────

    @POST("api/auth/register")
    Call<UsuarioResponse> cadastrar(@Body CadastroRequest request);

    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // ── Vagas ─────────────────────────────────────────────────────────────────

    @GET("api/vagas")
    Call<List<VagaResponse>> buscarVagas(
            @Query("titulo") String titulo,
            @Query("areaAtuacao") String areaAtuacao,
            @Query("tipoVagaId") Long tipoVagaId,
            @Query("modalidadeId") Long modalidadeId);

    @GET("api/vagas/{id}")
    Call<VagaResponse> buscarVagaPorId(@Path("id") Long id);

    // ── Candidaturas ──────────────────────────────────────────────────────────

    @POST("api/candidaturas")
    Call<CandidaturaResponse> candidatar(@Body CandidaturaRequest request);

    @GET("api/candidaturas/minhas")
    Call<List<CandidaturaResponse>> minhasCandidaturas();
}
