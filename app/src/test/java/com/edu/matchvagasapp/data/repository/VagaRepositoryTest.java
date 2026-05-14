package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.VagaResponse;
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

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VagaRepositoryTest {

    @Mock ApiService apiService;
    @Mock VagaRepository.VagasCallback vagasCallback;
    @Mock VagaRepository.VagaCallback vagaCallback;

    private VagaRepository repository;
    private final ResponseBody errorBody =
            ResponseBody.create(MediaType.parse("text/plain"), "");

    @Before
    public void setUp() {
        repository = new VagaRepository(apiService);
    }

    // ── buscarVagas ───────────────────────────────────────────────────────────

    @Test
    public void buscarVagas_respostaComSucesso_chamaOnSuccess() {
        Call<List<VagaResponse>> mockCall = mockCall();
        when(apiService.buscarVagas(isNull(), isNull(), isNull(), isNull())).thenReturn(mockCall);

        repository.buscarVagas(vagasCallback);

        Callback<List<VagaResponse>> cb = captureCallback(mockCall);
        List<VagaResponse> vagas = Arrays.asList(mock(VagaResponse.class));
        cb.onResponse(mockCall, Response.success(vagas));

        verify(vagasCallback).onSuccess(vagas);
    }

    @Test
    public void buscarVagas_respostaErro_chamaOnErrorComCodigo() {
        Call<List<VagaResponse>> mockCall = mockCall();
        when(apiService.buscarVagas(isNull(), isNull(), isNull(), isNull())).thenReturn(mockCall);

        repository.buscarVagas(vagasCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(500, errorBody));

        verify(vagasCallback).onError("Erro ao carregar vagas (código 500)");
    }

    @Test
    public void buscarVagas_falhaDeRede_chamaOnErrorSemConexao() {
        Call<List<VagaResponse>> mockCall = mockCall();
        when(apiService.buscarVagas(isNull(), isNull(), isNull(), isNull())).thenReturn(mockCall);

        repository.buscarVagas(vagasCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(vagasCallback).onError("Sem conexão com o servidor");
    }

    // ── buscarVagaPorId ───────────────────────────────────────────────────────

    @Test
    public void buscarVagaPorId_respostaComSucesso_chamaOnSuccess() {
        Call<VagaResponse> mockCall = mockCall();
        when(apiService.buscarVagaPorId(1L)).thenReturn(mockCall);

        repository.buscarVagaPorId(1L, vagaCallback);

        Callback<VagaResponse> cb = captureCallback(mockCall);
        VagaResponse body = mock(VagaResponse.class);
        cb.onResponse(mockCall, Response.success(body));

        verify(vagaCallback).onSuccess(body);
    }

    @Test
    public void buscarVagaPorId_resposta404_chamaOnErrorVagaNaoEncontrada() {
        Call<VagaResponse> mockCall = mockCall();
        when(apiService.buscarVagaPorId(99L)).thenReturn(mockCall);

        repository.buscarVagaPorId(99L, vagaCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(404, errorBody));

        verify(vagaCallback).onError("Vaga não encontrada");
    }

    @Test
    public void buscarVagaPorId_respostaErroServidor_chamaOnErrorComCodigo() {
        Call<VagaResponse> mockCall = mockCall();
        when(apiService.buscarVagaPorId(1L)).thenReturn(mockCall);

        repository.buscarVagaPorId(1L, vagaCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(503, errorBody));

        verify(vagaCallback).onError("Erro ao carregar vaga (código 503)");
    }

    @Test
    public void buscarVagaPorId_falhaDeRede_chamaOnErrorSemConexao() {
        Call<VagaResponse> mockCall = mockCall();
        when(apiService.buscarVagaPorId(1L)).thenReturn(mockCall);

        repository.buscarVagaPorId(1L, vagaCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(vagaCallback).onError("Sem conexão com o servidor");
    }

    // ── buscarVagasFiltradas ──────────────────────────────────────────────────

    @Test
    public void buscarVagasFiltradas_respostaComSucesso_chamaOnSuccess() {
        Call<List<VagaResponse>> mockCall = mockCall();
        when(apiService.buscarVagas(eq("Dev"), eq("TI"), isNull(), isNull()))
                .thenReturn(mockCall);

        repository.buscarVagasFiltradas("Dev", "TI", vagasCallback);

        Callback<List<VagaResponse>> cb = captureCallback(mockCall);
        List<VagaResponse> vagas = Arrays.asList(mock(VagaResponse.class), mock(VagaResponse.class));
        cb.onResponse(mockCall, Response.success(vagas));

        verify(vagasCallback).onSuccess(vagas);
    }

    @Test
    public void buscarVagasFiltradas_respostaErro_chamaOnErrorComCodigo() {
        Call<List<VagaResponse>> mockCall = mockCall();
        when(apiService.buscarVagas(eq("Dev"), isNull(), isNull(), isNull()))
                .thenReturn(mockCall);

        repository.buscarVagasFiltradas("Dev", null, vagasCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(400, errorBody));

        verify(vagasCallback).onError("Erro ao buscar vagas (código 400)");
    }

    @Test
    public void buscarVagasFiltradas_falhaDeRede_chamaOnErrorSemConexao() {
        Call<List<VagaResponse>> mockCall = mockCall();
        when(apiService.buscarVagas(isNull(), isNull(), isNull(), isNull()))
                .thenReturn(mockCall);

        repository.buscarVagasFiltradas(null, null, vagasCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(vagasCallback).onError("Sem conexão com o servidor");
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
