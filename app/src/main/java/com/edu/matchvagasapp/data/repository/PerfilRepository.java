package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.DadosPessoaisRequest;
import com.edu.matchvagasapp.data.model.DadosPessoaisResponse;
import com.edu.matchvagasapp.data.model.ExperienciaRequest;
import com.edu.matchvagasapp.data.model.ExperienciaResponse;
import com.edu.matchvagasapp.data.model.FormacaoRequest;
import com.edu.matchvagasapp.data.model.FormacaoResponse;
import com.edu.matchvagasapp.data.model.HabilidadesRequest;
import com.edu.matchvagasapp.data.model.HabilidadesResponse;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilRepository {

    // ── Callbacks de salvamento ───────────────────────────────────────────────

    public interface PerfilCallback {
        void onSuccess();
        void onError(String mensagem);
    }

    // ── Callbacks de carregamento ─────────────────────────────────────────────

    public interface DadosPessoaisCallback {
        void onSucesso(DadosPessoaisResponse dados);
        void onVazio();
    }

    public interface ExperienciasCallback {
        void onSucesso(List<ExperienciaResponse> lista);
        void onVazio();
    }

    public interface FormacoesCallback {
        void onSucesso(List<FormacaoResponse> lista);
        void onVazio();
    }

    public interface HabilidadesCallback {
        void onSucesso(HabilidadesResponse dados);
        void onVazio();
    }

    // ── Buscar ────────────────────────────────────────────────────────────────

    public void buscarDadosPessoais(DadosPessoaisCallback callback) {
        RetrofitClient.getInstance().getApiService().buscarDadosPessoais()
                .enqueue(new Callback<DadosPessoaisResponse>() {
                    @Override
                    public void onResponse(Call<DadosPessoaisResponse> call,
                                           Response<DadosPessoaisResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSucesso(response.body());
                        } else {
                            callback.onVazio();
                        }
                    }

                    @Override
                    public void onFailure(Call<DadosPessoaisResponse> call, Throwable t) {
                        callback.onVazio();
                    }
                });
    }

    public void buscarExperiencias(ExperienciasCallback callback) {
        RetrofitClient.getInstance().getApiService().buscarExperiencias()
                .enqueue(new Callback<List<ExperienciaResponse>>() {
                    @Override
                    public void onResponse(Call<List<ExperienciaResponse>> call,
                                           Response<List<ExperienciaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && !response.body().isEmpty()) {
                            callback.onSucesso(response.body());
                        } else {
                            callback.onVazio();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ExperienciaResponse>> call, Throwable t) {
                        callback.onVazio();
                    }
                });
    }

    public void buscarFormacoes(FormacoesCallback callback) {
        RetrofitClient.getInstance().getApiService().buscarFormacoes()
                .enqueue(new Callback<List<FormacaoResponse>>() {
                    @Override
                    public void onResponse(Call<List<FormacaoResponse>> call,
                                           Response<List<FormacaoResponse>> response) {
                        if (response.isSuccessful() && response.body() != null
                                && !response.body().isEmpty()) {
                            callback.onSucesso(response.body());
                        } else {
                            callback.onVazio();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<FormacaoResponse>> call, Throwable t) {
                        callback.onVazio();
                    }
                });
    }

    public void buscarHabilidades(HabilidadesCallback callback) {
        RetrofitClient.getInstance().getApiService().buscarHabilidades()
                .enqueue(new Callback<HabilidadesResponse>() {
                    @Override
                    public void onResponse(Call<HabilidadesResponse> call,
                                           Response<HabilidadesResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSucesso(response.body());
                        } else {
                            callback.onVazio();
                        }
                    }

                    @Override
                    public void onFailure(Call<HabilidadesResponse> call, Throwable t) {
                        callback.onVazio();
                    }
                });
    }

    // ── Salvar ────────────────────────────────────────────────────────────────

    public void atualizarDadosPessoais(DadosPessoaisRequest request, PerfilCallback callback) {
        RetrofitClient.getInstance().getApiService().atualizarDadosPessoais(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void adicionarExperiencia(ExperienciaRequest request, PerfilCallback callback) {
        RetrofitClient.getInstance().getApiService().adicionarExperiencia(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void adicionarFormacao(FormacaoRequest request, PerfilCallback callback) {
        RetrofitClient.getInstance().getApiService().adicionarFormacao(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void atualizarHabilidades(HabilidadesRequest request, PerfilCallback callback) {
        RetrofitClient.getInstance().getApiService().atualizarHabilidades(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }
}
