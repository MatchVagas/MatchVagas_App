package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CandidatoPerfilResponse;
import com.edu.matchvagasapp.data.model.DadosPessoaisRequest;
import com.edu.matchvagasapp.data.model.DadosPessoaisResponse;
import com.edu.matchvagasapp.data.model.ExperienciaRequest;
import com.edu.matchvagasapp.data.model.ExperienciaResponse;
import com.edu.matchvagasapp.data.model.FormacaoRequest;
import com.edu.matchvagasapp.data.model.FormacaoResponse;
import com.edu.matchvagasapp.data.model.HabilidadesRequest;
import com.edu.matchvagasapp.data.model.HabilidadesResponse;
import com.edu.matchvagasapp.data.network.ApiService;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilRepository {

    private final ApiService apiService;

    public PerfilRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    PerfilRepository(ApiService apiService) {
        this.apiService = apiService;
    }

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
        void onSucesso(List<HabilidadesResponse> lista);
        void onVazio();
    }

    public interface PerfilProfissionalCallback {
        void onSucesso(CandidatoPerfilResponse dados);
        void onVazio();
    }

    // ── Buscar ────────────────────────────────────────────────────────────────

    public void buscarDadosPessoais(DadosPessoaisCallback callback) {
        apiService.buscarDadosPessoais()
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
        apiService.buscarExperiencias()
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
        apiService.buscarFormacoes()
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
        apiService.buscarHabilidades()
                .enqueue(new Callback<List<HabilidadesResponse>>() {
                    @Override
                    public void onResponse(Call<List<HabilidadesResponse>> call,
                                           Response<List<HabilidadesResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSucesso(response.body());
                        } else {
                            callback.onVazio();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<HabilidadesResponse>> call, Throwable t) {
                        callback.onVazio();
                    }
                });
    }

    // ── Salvar ────────────────────────────────────────────────────────────────

    public void atualizarDadosPessoais(DadosPessoaisRequest request, PerfilCallback callback) {
        apiService.atualizarDadosPessoais(request)
                .enqueue(new Callback<DadosPessoaisResponse>() {
                    @Override
                    public void onResponse(Call<DadosPessoaisResponse> call,
                                           Response<DadosPessoaisResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<DadosPessoaisResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void adicionarExperiencia(ExperienciaRequest request, PerfilCallback callback) {
        apiService.adicionarExperiencia(request)
                .enqueue(new Callback<ExperienciaResponse>() {
                    @Override
                    public void onResponse(Call<ExperienciaResponse> call,
                                           Response<ExperienciaResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<ExperienciaResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void atualizarExperiencia(Long id, ExperienciaRequest request, PerfilCallback callback) {
        apiService.atualizarExperiencia(id, request)
                .enqueue(new Callback<ExperienciaResponse>() {
                    @Override
                    public void onResponse(Call<ExperienciaResponse> call,
                                           Response<ExperienciaResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<ExperienciaResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void removerExperiencia(Long id, PerfilCallback callback) {
        apiService.removerExperiencia(id)
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
        apiService.adicionarFormacao(request)
                .enqueue(new Callback<FormacaoResponse>() {
                    @Override
                    public void onResponse(Call<FormacaoResponse> call,
                                           Response<FormacaoResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<FormacaoResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void atualizarFormacao(Long id, FormacaoRequest request, PerfilCallback callback) {
        apiService.atualizarFormacao(id, request)
                .enqueue(new Callback<FormacaoResponse>() {
                    @Override
                    public void onResponse(Call<FormacaoResponse> call,
                                           Response<FormacaoResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<FormacaoResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void removerFormacao(Long id, PerfilCallback callback) {
        apiService.removerFormacao(id)
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

    // buscarPerfilProfissional reutiliza buscarDadosPessoais — mesmo endpoint
    public void buscarPerfilProfissional(PerfilProfissionalCallback callback) {
        apiService.buscarDadosPessoais()
                .enqueue(new Callback<DadosPessoaisResponse>() {
                    @Override
                    public void onResponse(Call<DadosPessoaisResponse> call,
                                           Response<DadosPessoaisResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            DadosPessoaisResponse d = response.body();
                            CandidatoPerfilResponse perfil = new CandidatoPerfilResponse(
                                    d.getCpf(),
                                    d.getObjetivoProfissional(),
                                    d.getDisponibilidade(),
                                    d.getPretensaoSalarial());
                            callback.onSucesso(perfil);
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

    public void atualizarPerfilProfissional(DadosPessoaisRequest request, PerfilCallback callback) {
        apiService.atualizarDadosPessoais(request)
                .enqueue(new Callback<DadosPessoaisResponse>() {
                    @Override
                    public void onResponse(Call<DadosPessoaisResponse> call,
                                           Response<DadosPessoaisResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<DadosPessoaisResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void adicionarHabilidade(HabilidadesRequest request, PerfilCallback callback) {
        apiService.adicionarHabilidade(request)
                .enqueue(new Callback<HabilidadesResponse>() {
                    @Override
                    public void onResponse(Call<HabilidadesResponse> call,
                                           Response<HabilidadesResponse> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente.");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<HabilidadesResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void removerHabilidade(String nome, PerfilCallback callback) {
        apiService.removerHabilidade(nome)
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
