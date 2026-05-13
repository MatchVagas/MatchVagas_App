package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CandidatoPerfilRequest;
import com.edu.matchvagasapp.data.network.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CandidatoRepositoryTest {

    @Mock ApiService apiService;
    @Mock CandidatoRepository.CriarPerfilCallback callback;

    private CandidatoRepository repository;
    private final ResponseBody errorBody =
            ResponseBody.create(MediaType.parse("text/plain"), "");

    @Before
    public void setUp() {
        repository = new CandidatoRepository(apiService);
    }

    @Test
    public void criarPerfil_respostaComSucesso_chamaOnSuccess() {
        Call<ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfil(mock(CandidatoPerfilRequest.class), callback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(errorBody));

        verify(callback).onSuccess();
    }

    @Test
    public void criarPerfil_resposta400_chamaOnErrorCpfInvalido() {
        Call<ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfil(mock(CandidatoPerfilRequest.class), callback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(400, errorBody));

        verify(callback).onError("CPF inválido ou dados incorretos");
    }

    @Test
    public void criarPerfil_respostaErroServidor_chamaOnErrorComCodigo() {
        Call<ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfil(mock(CandidatoPerfilRequest.class), callback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(500, errorBody));

        verify(callback).onError("Erro ao criar perfil (código 500)");
    }

    @Test
    public void criarPerfil_falhaDeRede_chamaOnErrorSemConexao() {
        Call<ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfil(mock(CandidatoPerfilRequest.class), callback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(callback).onError("Sem conexão com o servidor");
    }

    @SuppressWarnings("unchecked")
    private <T> Call<T> mockCall() {
        return mock(Call.class);
    }

    @SuppressWarnings("unchecked")
    private <T> Callback<T> captureCallback(Call<T> call) {
        ArgumentCaptor<Callback<T>> captor = ArgumentCaptor.forClass(Callback.class);
        verify(call).enqueue(captor.capture());
        return captor.getValue();
    }
}
