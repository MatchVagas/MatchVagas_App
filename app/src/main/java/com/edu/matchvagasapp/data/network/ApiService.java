package com.edu.matchvagasapp.data.network;

import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.CandidatoPerfilRequest;
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
import com.edu.matchvagasapp.data.model.SugestaoVagaResponse;
import com.edu.matchvagasapp.data.model.UsuarioResponse;
import com.edu.matchvagasapp.data.model.VagaResponse;

import java.util.List;

import retrofit2.Call;
import okhttp3.ResponseBody;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
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

    @POST("api/candidatos")
    Call<okhttp3.ResponseBody> criarPerfilCandidato(@Body CandidatoPerfilRequest request);

    // ── Vagas ─────────────────────────────────────────────────────────────────

    @GET("api/candidatos/sugestoes")
    Call<List<SugestaoVagaResponse>> buscarSugestoes();

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

    // ── Candidato — dados pessoais + perfil profissional ─────────────────────
    // Ambas as telas usam o mesmo endpoint GET/PUT /api/candidatos/meu-perfil.
    // DadosPessoaisResponse contém todos os campos de CandidatoResponseDTO.

    @GET("api/candidatos/meu-perfil")
    Call<DadosPessoaisResponse> buscarDadosPessoais();

    @PUT("api/candidatos/meu-perfil")
    Call<DadosPessoaisResponse> atualizarDadosPessoais(@Body DadosPessoaisRequest request);

    // ── Habilidades ───────────────────────────────────────────────────────────

    @GET("api/candidatos/habilidades")
    Call<List<HabilidadesResponse>> buscarHabilidades();

    @POST("api/candidatos/habilidades")
    Call<HabilidadesResponse> adicionarHabilidade(@Body HabilidadesRequest request);

    @DELETE("api/candidatos/habilidades/{nome}")
    Call<Void> removerHabilidade(@Path("nome") String nome);

    // ── Experiências ──────────────────────────────────────────────────────────

    @GET("api/candidatos/experiencias")
    Call<List<ExperienciaResponse>> buscarExperiencias();

    @POST("api/candidatos/experiencias")
    Call<ExperienciaResponse> adicionarExperiencia(@Body ExperienciaRequest request);

    @PUT("api/candidatos/experiencias/{id}")
    Call<ExperienciaResponse> atualizarExperiencia(@Path("id") Long id, @Body ExperienciaRequest request);

    @DELETE("api/candidatos/experiencias/{id}")
    Call<Void> removerExperiencia(@Path("id") Long id);

    // ── Formações ─────────────────────────────────────────────────────────────

    @GET("api/candidatos/formacoes")
    Call<List<FormacaoResponse>> buscarFormacoes();

    @POST("api/candidatos/formacoes")
    Call<FormacaoResponse> adicionarFormacao(@Body FormacaoRequest request);

    @PUT("api/candidatos/formacoes/{id}")
    Call<FormacaoResponse> atualizarFormacao(@Path("id") Long id, @Body FormacaoRequest request);

    @DELETE("api/candidatos/formacoes/{id}")
    Call<Void> removerFormacao(@Path("id") Long id);
}
