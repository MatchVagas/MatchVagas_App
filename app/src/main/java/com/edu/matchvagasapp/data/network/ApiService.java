package com.edu.matchvagasapp.data.network;

import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.CandidaturaRequest;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.model.DadosPessoaisRequest;
import com.edu.matchvagasapp.data.model.DadosPessoaisResponse;
import com.edu.matchvagasapp.data.model.ExperienciaRequest;
import com.edu.matchvagasapp.data.model.ExperienciaResponse;
import com.edu.matchvagasapp.data.model.FormacaoRequest;
import com.edu.matchvagasapp.data.model.FormacaoResponse;
import com.edu.matchvagasapp.data.model.HabilidadesRequest;
import com.edu.matchvagasapp.data.model.HabilidadesResponse;
import com.edu.matchvagasapp.data.model.LoginRequest;
import com.edu.matchvagasapp.data.model.LoginResponse;
import com.edu.matchvagasapp.data.model.UsuarioResponse;
import com.edu.matchvagasapp.data.model.VagaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    // ── Perfil do candidato ───────────────────────────────────────────────────

    @GET("api/perfil/dados-pessoais")
    Call<DadosPessoaisResponse> buscarDadosPessoais();

    @PUT("api/perfil/dados-pessoais")
    Call<Void> atualizarDadosPessoais(@Body DadosPessoaisRequest request);

    @GET("api/perfil/experiencias")
    Call<List<ExperienciaResponse>> buscarExperiencias();

    @POST("api/perfil/experiencias")
    Call<Void> adicionarExperiencia(@Body ExperienciaRequest request);

    @GET("api/perfil/formacoes")
    Call<List<FormacaoResponse>> buscarFormacoes();

    @POST("api/perfil/formacoes")
    Call<Void> adicionarFormacao(@Body FormacaoRequest request);

    @GET("api/perfil/habilidades")
    Call<HabilidadesResponse> buscarHabilidades();

    @PUT("api/perfil/habilidades")
    Call<Void> atualizarHabilidades(@Body HabilidadesRequest request);
}
