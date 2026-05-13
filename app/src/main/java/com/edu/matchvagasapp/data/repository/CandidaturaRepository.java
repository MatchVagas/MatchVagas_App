package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CandidaturaRequest;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.network.ApiService;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidaturaRepository {

    private final ApiService apiService;

    public CandidaturaRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    CandidaturaRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public interface CandidaturaCallback {
        void onSuccess(CandidaturaResponse response);
        void onError(String mensagem);
    }

    public interface CandidaturasCallback {
        void onSuccess(List<CandidaturaResponse> candidaturas);
        void onError(String mensagem);
    }

    public void candidatar(CandidaturaRequest request, CandidaturaCallback callback) {
        apiService
                .candidatar(request)
                .enqueue(new Callback<CandidaturaResponse>() {
                    @Override
                    public void onResponse(Call<CandidaturaResponse> call,
                                           Response<CandidaturaResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else if (response.code() == 401 || response.code() == 403) {
                            callback.onError("Sessão expirada. Faça login novamente");
                        } else if (response.code() == 409) {
                            callback.onError("Você já se candidatou a esta vaga");
                        } else {
                            callback.onError("Erro ao enviar candidatura (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<CandidaturaResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }

    public void minhasCandidaturas(CandidaturasCallback callback) {
        apiService
                .minhasCandidaturas()
                .enqueue(new Callback<List<CandidaturaResponse>>() {
                    @Override
                    public void onResponse(Call<List<CandidaturaResponse>> call,
                                           Response<List<CandidaturaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else {
                            callback.onError("Erro ao carregar candidaturas (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CandidaturaResponse>> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }
}
