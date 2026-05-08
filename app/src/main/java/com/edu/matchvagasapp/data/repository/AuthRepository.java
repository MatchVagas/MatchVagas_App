package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.UsuarioResponse;
import com.edu.matchvagasapp.data.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    public interface CadastroCallback {
        void onSuccess(UsuarioResponse response);
        void onError(String mensagem);
    }

    public void cadastrar(CadastroRequest request, CadastroCallback callback) {
        RetrofitClient.getInstance().getApiService().cadastrar(request)
                .enqueue(new Callback<UsuarioResponse>() {
                    @Override
                    public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            callback.onSuccess(response.body());
                        } else if (response.code() == 400) {
                            callback.onError("E-mail já cadastrado ou dados inválidos");
                        } else {
                            callback.onError("Erro no servidor (código " + response.code() + ")");
                        }
                    }

                    @Override
                    public void onFailure(Call<UsuarioResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor");
                    }
                });
    }
}
