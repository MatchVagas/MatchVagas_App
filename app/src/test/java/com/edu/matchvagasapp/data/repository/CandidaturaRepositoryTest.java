package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CandidaturaRequest;
import com.edu.matchvagasapp.data.model.CandidaturaResponse;
import com.edu.matchvagasapp.data.network.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
public class CandidaturaRepositoryTest {

    @Mock ApiService apiService;
    @Mock CandidaturaRepository.CandidaturaCallback candidaturaCallback;
    @Mock CandidaturaRepository.CandidaturasCallback candidaturasCallback;

    private CandidaturaRepository repository;
    private final ResponseBody errorBody =
            ResponseBody.create(MediaType.parse("text/plain"), "");

    @Before
    public void setUp() {
        repository = new CandidaturaRepository(apiService);
    }

    // ── candidatar ────────────────────────────────────────────────────────────

    @Test
    public void candidatar_respostaComSucesso_chamaOnSuccess() {
        Call<CandidaturaResponse> mockCall = mockCall();
        when(apiService.candidatar(any())).thenReturn(mockCall);

        repository.candidatar(umaCandidaturaRequest(), candidaturaCallback);

        Callback<CandidaturaResponse> cb = captureCallback(mockCall);
        CandidaturaResponse body = mock(CandidaturaResponse.class);
        cb.onResponse(mockCall, Response.success(body));

        verify(candidaturaCallback).onSuccess(body);
    }

    @Test
    public void candidatar_resposta401_chamaOnErrorSessaoExpirada() {
        Call<CandidaturaResponse> mockCall = mockCall();
        when(apiService.candidatar(any())).thenReturn(mockCall);

        repository.candidatar(umaCandidaturaRequest(), candidaturaCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(401, errorBody));

        verify(candidaturaCallback).onError("Sessão expirada. Faça login novamente");
    }

    @Test
    public void candidatar_resposta403_chamaOnErrorSessaoExpirada() {
        Call<CandidaturaResponse> mockCall = mockCall();
        when(apiService.candidatar(any())).thenReturn(mockCall);

        repository.candidatar(umaCandidaturaRequest(), candidaturaCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(403, errorBody));

        verify(candidaturaCallback).onError("Sessão expirada. Faça login novamente");
    }

    @Test
    public void candidatar_resposta409_chamaOnErrorJaCandidatou() {
        Call<CandidaturaResponse> mockCall = mockCall();
        when(apiService.candidatar(any())).thenReturn(mockCall);

        repository.candidatar(umaCandidaturaRequest(), candidaturaCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(409, errorBody));

        verify(candidaturaCallback).onError("Você já se candidatou a esta vaga");
    }

    @Test
    public void candidatar_respostaErroServidor_chamaOnErrorComCodigo() {
        Call<CandidaturaResponse> mockCall = mockCall();
        when(apiService.candidatar(any())).thenReturn(mockCall);

        repository.candidatar(umaCandidaturaRequest(), candidaturaCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(500, errorBody));

        verify(candidaturaCallback).onError("Erro ao enviar candidatura (código 500)");
    }

    @Test
    public void candidatar_falhaDeRede_chamaOnErrorSemConexao() {
        Call<CandidaturaResponse> mockCall = mockCall();
        when(apiService.candidatar(any())).thenReturn(mockCall);

        repository.candidatar(umaCandidaturaRequest(), candidaturaCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(candidaturaCallback).onError("Sem conexão com o servidor");
    }

    // ── minhasCandidaturas ────────────────────────────────────────────────────

    @Test
    public void minhasCandidaturas_respostaComSucesso_chamaOnSuccess() {
        Call<List<CandidaturaResponse>> mockCall = mockCall();
        when(apiService.minhasCandidaturas()).thenReturn(mockCall);

        repository.minhasCandidaturas(candidaturasCallback);

        Callback<List<CandidaturaResponse>> cb = captureCallback(mockCall);
        List<CandidaturaResponse> lista = Arrays.asList(mock(CandidaturaResponse.class));
        cb.onResponse(mockCall, Response.success(lista));

        verify(candidaturasCallback).onSuccess(lista);
    }

    @Test
    public void minhasCandidaturas_respostaErro_chamaOnErrorComCodigo() {
        Call<List<CandidaturaResponse>> mockCall = mockCall();
        when(apiService.minhasCandidaturas()).thenReturn(mockCall);

        repository.minhasCandidaturas(candidaturasCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(401, errorBody));

        verify(candidaturasCallback).onError("Erro ao carregar candidaturas (código 401)");
    }

    @Test
    public void minhasCandidaturas_falhaDeRede_chamaOnErrorSemConexao() {
        Call<List<CandidaturaResponse>> mockCall = mockCall();
        when(apiService.minhasCandidaturas()).thenReturn(mockCall);

        repository.minhasCandidaturas(candidaturasCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(candidaturasCallback).onError("Sem conexão com o servidor");
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

    private CandidaturaRequest umaCandidaturaRequest() {
        return new CandidaturaRequest(1L, true, true, true, true, true, true, true, true);
    }
}
