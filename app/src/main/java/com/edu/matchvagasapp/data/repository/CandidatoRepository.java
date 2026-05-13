package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CandidatoPerfilRequest;
import com.edu.matchvagasapp.data.network.ApiService;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatoRepository {

    private final ApiService apiService;

    public CandidatoRepository() {
        this.apiService = RetrofitClient.getInstance().getApiService();
    }

    CandidatoRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public interface CriarPerfilCallback {
        void onSuccess();
        void onError(String mensagem);
    }

    public void criarPerfil(CandidatoPerfilRequest request, CriarPerfilCallback callback) {
        apiService.criarPerfilCandidato(request)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 400) {
                            callback.onError("CPF inválido ou dados incorretos");
                        } else {
                            callback.onError("Erro ao criar perfil (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }
}
