package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.VagaResponse;
import com.edu.matchvagasapp.data.network.ApiService;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VagaRepository {

    private final ApiService apiService;

    public VagaRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    VagaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public interface VagasCallback {
        void onSuccess(List<VagaResponse> vagas);
        void onError(String mensagem);
    }

    public interface VagaCallback {
        void onSuccess(VagaResponse vaga);
        void onError(String mensagem);
    }

    public void buscarVagas(VagasCallback callback) {
        apiService
                .buscarVagas(null, null, null, null)
                .enqueue(new Callback<List<VagaResponse>>() {
                    @Override
                    public void onResponse(Call<List<VagaResponse>> call,
                                           Response<List<VagaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Erro ao carregar vagas (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<VagaResponse>> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void buscarVagaPorId(Long id, VagaCallback callback) {
        apiService
                .buscarVagaPorId(id)
                .enqueue(new Callback<VagaResponse>() {
                    @Override
                    public void onResponse(Call<VagaResponse> call,
                                           Response<VagaResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else if (response.code() == 404) {
                            callback.onError("Vaga não encontrada");
                        } else {
                            callback.onError("Erro ao carregar vaga (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<VagaResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void buscarVagasFiltradas(String titulo, String areaAtuacao, VagasCallback callback) {
        apiService
                .buscarVagas(titulo, areaAtuacao, null, null)
                .enqueue(new Callback<List<VagaResponse>>() {
                    @Override
                    public void onResponse(Call<List<VagaResponse>> call,
                                           Response<List<VagaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Erro ao buscar vagas (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<VagaResponse>> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }
}
