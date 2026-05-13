package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CandidatoPerfilRequest;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandidatoRepository {

    public interface CriarPerfilCallback {
        void onSuccess();
        void onError(String mensagem);
    }

    public void criarPerfil(CandidatoPerfilRequest request, CriarPerfilCallback callback) {
        RetrofitClient.getInstance().getApiService().criarPerfilCandidato(request)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            callback.onSuccess();
                        } else if (response.code() == 400) {
                            callback.onError("CPF inválido ou dados incorretos");
                        } else {
                            callback.onError("Erro ao criar perfil (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }
}
