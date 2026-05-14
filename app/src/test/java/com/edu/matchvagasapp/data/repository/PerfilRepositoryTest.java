package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.DadosPessoaisRequest;
import com.edu.matchvagasapp.data.model.DadosPessoaisResponse;
import com.edu.matchvagasapp.data.model.ExperienciaRequest;
import com.edu.matchvagasapp.data.model.ExperienciaResponse;
import com.edu.matchvagasapp.data.model.FormacaoRequest;
import com.edu.matchvagasapp.data.model.FormacaoResponse;
import com.edu.matchvagasapp.data.model.HabilidadesRequest;
import com.edu.matchvagasapp.data.model.HabilidadesResponse;
import com.edu.matchvagasapp.data.network.ApiService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
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
public class PerfilRepositoryTest {

    @Mock ApiService apiService;
    @Mock PerfilRepository.DadosPessoaisCallback dadosPessoaisCallback;
    @Mock PerfilRepository.ExperienciasCallback experienciasCallback;
    @Mock PerfilRepository.FormacoesCallback formacoesCallback;
    @Mock PerfilRepository.HabilidadesCallback habilidadesCallback;
    @Mock PerfilRepository.PerfilCallback perfilCallback;

    private PerfilRepository repository;
    private final ResponseBody errorBody =
            ResponseBody.create(MediaType.parse("text/plain"), "");

    @Before
    public void setUp() {
        repository = new PerfilRepository(apiService);
    }

    // ── buscarDadosPessoais ───────────────────────────────────────────────────

    @Test
    public void buscarDadosPessoais_respostaComSucesso_chamaOnSucesso() {
        Call<DadosPessoaisResponse> mockCall = mockCall();
        when(apiService.buscarDadosPessoais()).thenReturn(mockCall);

        repository.buscarDadosPessoais(dadosPessoaisCallback);

        Callback<DadosPessoaisResponse> cb = captureCallback(mockCall);
        DadosPessoaisResponse body = mock(DadosPessoaisResponse.class);
        cb.onResponse(mockCall, Response.success(body));

        verify(dadosPessoaisCallback).onSucesso(body);
    }

    @Test
    public void buscarDadosPessoais_respostaErro_chamaOnVazio() {
        Call<DadosPessoaisResponse> mockCall = mockCall();
        when(apiService.buscarDadosPessoais()).thenReturn(mockCall);

        repository.buscarDadosPessoais(dadosPessoaisCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(404, errorBody));

        verify(dadosPessoaisCallback).onVazio();
    }

    @Test
    public void buscarDadosPessoais_falhaDeRede_chamaOnVazio() {
        Call<DadosPessoaisResponse> mockCall = mockCall();
        when(apiService.buscarDadosPessoais()).thenReturn(mockCall);

        repository.buscarDadosPessoais(dadosPessoaisCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(dadosPessoaisCallback).onVazio();
    }

    // ── buscarExperiencias ────────────────────────────────────────────────────

    @Test
    public void buscarExperiencias_respostaComLista_chamaOnSucesso() {
        Call<List<ExperienciaResponse>> mockCall = mockCall();
        when(apiService.buscarExperiencias()).thenReturn(mockCall);

        repository.buscarExperiencias(experienciasCallback);

        Callback<List<ExperienciaResponse>> cb = captureCallback(mockCall);
        List<ExperienciaResponse> lista = Arrays.asList(mock(ExperienciaResponse.class));
        cb.onResponse(mockCall, Response.success(lista));

        verify(experienciasCallback).onSucesso(lista);
    }

    @Test
    public void buscarExperiencias_respostaComListaVazia_chamaOnVazio() {
        Call<List<ExperienciaResponse>> mockCall = mockCall();
        when(apiService.buscarExperiencias()).thenReturn(mockCall);

        repository.buscarExperiencias(experienciasCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(Collections.emptyList()));

        verify(experienciasCallback).onVazio();
    }

    @Test
    public void buscarExperiencias_falhaDeRede_chamaOnVazio() {
        Call<List<ExperienciaResponse>> mockCall = mockCall();
        when(apiService.buscarExperiencias()).thenReturn(mockCall);

        repository.buscarExperiencias(experienciasCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(experienciasCallback).onVazio();
    }

    // ── buscarFormacoes ───────────────────────────────────────────────────────

    @Test
    public void buscarFormacoes_respostaComLista_chamaOnSucesso() {
        Call<List<FormacaoResponse>> mockCall = mockCall();
        when(apiService.buscarFormacoes()).thenReturn(mockCall);

        repository.buscarFormacoes(formacoesCallback);

        Callback<List<FormacaoResponse>> cb = captureCallback(mockCall);
        List<FormacaoResponse> lista = Arrays.asList(mock(FormacaoResponse.class));
        cb.onResponse(mockCall, Response.success(lista));

        verify(formacoesCallback).onSucesso(lista);
    }

    @Test
    public void buscarFormacoes_respostaComListaVazia_chamaOnVazio() {
        Call<List<FormacaoResponse>> mockCall = mockCall();
        when(apiService.buscarFormacoes()).thenReturn(mockCall);

        repository.buscarFormacoes(formacoesCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(Collections.emptyList()));

        verify(formacoesCallback).onVazio();
    }

    @Test
    public void buscarFormacoes_falhaDeRede_chamaOnVazio() {
        Call<List<FormacaoResponse>> mockCall = mockCall();
        when(apiService.buscarFormacoes()).thenReturn(mockCall);

        repository.buscarFormacoes(formacoesCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(formacoesCallback).onVazio();
    }

    // ── buscarHabilidades ─────────────────────────────────────────────────────

    @Test
    public void buscarHabilidades_respostaComLista_chamaOnSucesso() {
        Call<List<HabilidadesResponse>> mockCall = mockCall();
        when(apiService.buscarHabilidades()).thenReturn(mockCall);

        repository.buscarHabilidades(habilidadesCallback);

        Callback<List<HabilidadesResponse>> cb = captureCallback(mockCall);
        List<HabilidadesResponse> lista = Arrays.asList(mock(HabilidadesResponse.class));
        cb.onResponse(mockCall, Response.success(lista));

        verify(habilidadesCallback).onSucesso(lista);
    }

    @Test
    public void buscarHabilidades_respostaErro_chamaOnVazio() {
        Call<List<HabilidadesResponse>> mockCall = mockCall();
        when(apiService.buscarHabilidades()).thenReturn(mockCall);

        repository.buscarHabilidades(habilidadesCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(404, errorBody));

        verify(habilidadesCallback).onVazio();
    }

    @Test
    public void buscarHabilidades_falhaDeRede_chamaOnVazio() {
        Call<List<HabilidadesResponse>> mockCall = mockCall();
        when(apiService.buscarHabilidades()).thenReturn(mockCall);

        repository.buscarHabilidades(habilidadesCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(habilidadesCallback).onVazio();
    }

    // ── atualizarDadosPessoais ────────────────────────────────────────────────

    @Test
    public void atualizarDadosPessoais_respostaComSucesso_chamaOnSuccess() {
        Call<Void> mockCall = mockCall();
        when(apiService.atualizarDadosPessoais(any())).thenReturn(mockCall);

        repository.atualizarDadosPessoais(umDadosPessoaisRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(null));

        verify(perfilCallback).onSuccess();
    }

    @Test
    public void atualizarDadosPessoais_resposta401_chamaOnErrorSessaoExpirada() {
        Call<Void> mockCall = mockCall();
        when(apiService.atualizarDadosPessoais(any())).thenReturn(mockCall);

        repository.atualizarDadosPessoais(umDadosPessoaisRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(401, errorBody));

        verify(perfilCallback).onError("Sessão expirada. Faça login novamente.");
    }

    @Test
    public void atualizarDadosPessoais_resposta403_chamaOnErrorSessaoExpirada() {
        Call<Void> mockCall = mockCall();
        when(apiService.atualizarDadosPessoais(any())).thenReturn(mockCall);

        repository.atualizarDadosPessoais(umDadosPessoaisRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(403, errorBody));

        verify(perfilCallback).onError("Sessão expirada. Faça login novamente.");
    }

    @Test
    public void atualizarDadosPessoais_falhaDeRede_chamaOnErrorSemConexao() {
        Call<Void> mockCall = mockCall();
        when(apiService.atualizarDadosPessoais(any())).thenReturn(mockCall);

        repository.atualizarDadosPessoais(umDadosPessoaisRequest(), perfilCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(perfilCallback).onError("Sem conexão com o servidor");
    }

    // ── adicionarExperiencia ──────────────────────────────────────────────────

    @Test
    public void adicionarExperiencia_respostaComSucesso_chamaOnSuccess() {
        Call<Void> mockCall = mockCall();
        when(apiService.adicionarExperiencia(any())).thenReturn(mockCall);

        repository.adicionarExperiencia(umaExperienciaRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(null));

        verify(perfilCallback).onSuccess();
    }

    @Test
    public void adicionarExperiencia_resposta403_chamaOnErrorSessaoExpirada() {
        Call<Void> mockCall = mockCall();
        when(apiService.adicionarExperiencia(any())).thenReturn(mockCall);

        repository.adicionarExperiencia(umaExperienciaRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(403, errorBody));

        verify(perfilCallback).onError("Sessão expirada. Faça login novamente.");
    }

    @Test
    public void adicionarExperiencia_falhaDeRede_chamaOnErrorSemConexao() {
        Call<Void> mockCall = mockCall();
        when(apiService.adicionarExperiencia(any())).thenReturn(mockCall);

        repository.adicionarExperiencia(umaExperienciaRequest(), perfilCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(perfilCallback).onError("Sem conexão com o servidor");
    }

    // ── adicionarFormacao ─────────────────────────────────────────────────────

    @Test
    public void adicionarFormacao_respostaComSucesso_chamaOnSuccess() {
        Call<Void> mockCall = mockCall();
        when(apiService.adicionarFormacao(any())).thenReturn(mockCall);

        repository.adicionarFormacao(umaFormacaoRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(null));

        verify(perfilCallback).onSuccess();
    }

    @Test
    public void adicionarFormacao_respostaErroServidor_chamaOnErrorComCodigo() {
        Call<Void> mockCall = mockCall();
        when(apiService.adicionarFormacao(any())).thenReturn(mockCall);

        repository.adicionarFormacao(umaFormacaoRequest(), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(500, errorBody));

        verify(perfilCallback).onError("Erro no servidor (código 500)");
    }

    @Test
    public void adicionarFormacao_falhaDeRede_chamaOnErrorSemConexao() {
        Call<Void> mockCall = mockCall();
        when(apiService.adicionarFormacao(any())).thenReturn(mockCall);

        repository.adicionarFormacao(umaFormacaoRequest(), perfilCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(perfilCallback).onError("Sem conexão com o servidor");
    }

    // ── adicionarHabilidade ───────────────────────────────────────────────────

    @Test
    public void adicionarHabilidade_respostaComSucesso_chamaOnSuccess() {
        Call<HabilidadesResponse> mockCall = mockCall();
        when(apiService.adicionarHabilidade(any())).thenReturn(mockCall);

        repository.adicionarHabilidade(new HabilidadesRequest("Java", null), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(mock(HabilidadesResponse.class)));

        verify(perfilCallback).onSuccess();
    }

    @Test
    public void adicionarHabilidade_resposta401_chamaOnErrorSessaoExpirada() {
        Call<HabilidadesResponse> mockCall = mockCall();
        when(apiService.adicionarHabilidade(any())).thenReturn(mockCall);

        repository.adicionarHabilidade(new HabilidadesRequest("Java", null), perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(401, errorBody));

        verify(perfilCallback).onError("Sessão expirada. Faça login novamente.");
    }

    @Test
    public void adicionarHabilidade_falhaDeRede_chamaOnErrorSemConexao() {
        Call<HabilidadesResponse> mockCall = mockCall();
        when(apiService.adicionarHabilidade(any())).thenReturn(mockCall);

        repository.adicionarHabilidade(new HabilidadesRequest("Java", null), perfilCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(perfilCallback).onError("Sem conexão com o servidor");
    }

    // ── removerHabilidade ─────────────────────────────────────────────────────

    @Test
    public void removerHabilidade_respostaComSucesso_chamaOnSuccess() {
        Call<Void> mockCall = mockCall();
        when(apiService.removerHabilidade("Java")).thenReturn(mockCall);

        repository.removerHabilidade("Java", perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(null));

        verify(perfilCallback).onSuccess();
    }

    @Test
    public void removerHabilidade_resposta403_chamaOnErrorSessaoExpirada() {
        Call<Void> mockCall = mockCall();
        when(apiService.removerHabilidade("Java")).thenReturn(mockCall);

        repository.removerHabilidade("Java", perfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(403, errorBody));

        verify(perfilCallback).onError("Sessão expirada. Faça login novamente.");
    }

    @Test
    public void removerHabilidade_falhaDeRede_chamaOnErrorSemConexao() {
        Call<Void> mockCall = mockCall();
        when(apiService.removerHabilidade("Java")).thenReturn(mockCall);

        repository.removerHabilidade("Java", perfilCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(perfilCallback).onError("Sem conexão com o servidor");
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

    private DadosPessoaisRequest umDadosPessoaisRequest() {
        return new DadosPessoaisRequest("Nome", "email@test.com", "11999999999",
                "2000-01-01", "123.456.789-00", "M", "01310-100",
                "São Paulo", "SP", null, null);
    }

    private ExperienciaRequest umaExperienciaRequest() {
        return new ExperienciaRequest("Desenvolvedor", "Empresa X", "Remoto",
                "CLT", "São Paulo", "01", "2022", null, null, true);
    }

    private FormacaoRequest umaFormacaoRequest() {
        return new FormacaoRequest("USP", "Ciência da Computação", "Bacharelado",
                "02", "2019", null, null, true);
    }
}
