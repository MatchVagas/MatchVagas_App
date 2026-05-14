package com.edu.matchvagasapp.data.repository;

import com.edu.matchvagasapp.data.model.CadastroRequest;
import com.edu.matchvagasapp.data.model.CandidatoPerfilRequest;
import com.edu.matchvagasapp.data.model.LoginResponse;
import com.edu.matchvagasapp.data.model.UsuarioResponse;
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
public class AuthRepositoryTest {

    @Mock ApiService apiService;
    @Mock AuthRepository.CadastroCallback cadastroCallback;
    @Mock AuthRepository.LoginCallback loginCallback;
    @Mock AuthRepository.CriarPerfilCallback criarPerfilCallback;

    private AuthRepository repository;
    private final ResponseBody errorBody =
            ResponseBody.create(MediaType.parse("text/plain"), "");

    @Before
    public void setUp() {
        repository = new AuthRepository(apiService);
    }

    // ── cadastrar ─────────────────────────────────────────────────────────────

    @Test
    public void cadastrar_respostaComSucesso_chamaOnSuccess() {
        Call<UsuarioResponse> mockCall = mockCall();
        when(apiService.cadastrar(any())).thenReturn(mockCall);

        repository.cadastrar(umCadastroRequest(), cadastroCallback);

        Callback<UsuarioResponse> cb = captureCallback(mockCall);
        UsuarioResponse body = mock(UsuarioResponse.class);
        cb.onResponse(mockCall, Response.success(body));

        verify(cadastroCallback).onSuccess(body);
    }

    @Test
    public void cadastrar_resposta400_chamaOnErrorEmailExistente() {
        Call<UsuarioResponse> mockCall = mockCall();
        when(apiService.cadastrar(any())).thenReturn(mockCall);

        repository.cadastrar(umCadastroRequest(), cadastroCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(400, errorBody));

        verify(cadastroCallback).onError("E-mail já cadastrado ou dados inválidos");
    }

    @Test
    public void cadastrar_respostaErroServidor_chamaOnErrorComCodigo() {
        Call<UsuarioResponse> mockCall = mockCall();
        when(apiService.cadastrar(any())).thenReturn(mockCall);

        repository.cadastrar(umCadastroRequest(), cadastroCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(500, errorBody));

        verify(cadastroCallback).onError("Erro no servidor (código 500)");
    }

    @Test
    public void cadastrar_falhaDeRede_chamaOnErrorSemConexao() {
        Call<UsuarioResponse> mockCall = mockCall();
        when(apiService.cadastrar(any())).thenReturn(mockCall);

        repository.cadastrar(umCadastroRequest(), cadastroCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException("timeout"));

        verify(cadastroCallback).onError("Sem conexão com o servidor");
    }

    // ── login ─────────────────────────────────────────────────────────────────

    @Test
    public void login_respostaComSucesso_chamaOnSuccess() {
        Call<LoginResponse> mockCall = mockCall();
        when(apiService.login(any())).thenReturn(mockCall);

        repository.login("user@test.com", "senha123", loginCallback);

        Callback<LoginResponse> cb = captureCallback(mockCall);
        LoginResponse body = mock(LoginResponse.class);
        cb.onResponse(mockCall, Response.success(body));

        verify(loginCallback).onSuccess(body);
    }

    @Test
    public void login_resposta401_chamaOnErrorCredenciaisErradas() {
        Call<LoginResponse> mockCall = mockCall();
        when(apiService.login(any())).thenReturn(mockCall);

        repository.login("user@test.com", "errada", loginCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(401, errorBody));

        verify(loginCallback).onError("E-mail ou senha incorretos");
    }

    @Test
    public void login_resposta403_chamaOnErrorContaInativa() {
        Call<LoginResponse> mockCall = mockCall();
        when(apiService.login(any())).thenReturn(mockCall);

        repository.login("user@test.com", "senha123", loginCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(403, errorBody));

        verify(loginCallback).onError("Conta inativa. Entre em contato com o suporte");
    }

    @Test
    public void login_respostaErroServidor_chamaOnErrorComCodigo() {
        Call<LoginResponse> mockCall = mockCall();
        when(apiService.login(any())).thenReturn(mockCall);

        repository.login("user@test.com", "senha123", loginCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(503, errorBody));

        verify(loginCallback).onError("Erro no servidor (código 503)");
    }

    @Test
    public void login_falhaDeRede_chamaOnErrorSemConexao() {
        Call<LoginResponse> mockCall = mockCall();
        when(apiService.login(any())).thenReturn(mockCall);

        repository.login("user@test.com", "senha123", loginCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(loginCallback).onError("Sem conexão com o servidor");
    }

    // ── criarPerfilCandidato ──────────────────────────────────────────────────

    @Test
    public void criarPerfilCandidato_respostaComSucesso_chamaOnSuccess() {
        Call<okhttp3.ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfilCandidato(mock(CandidatoPerfilRequest.class), criarPerfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.success(errorBody));

        verify(criarPerfilCallback).onSuccess();
    }

    @Test
    public void criarPerfilCandidato_respostaErro_chamaOnErrorComCodigo() {
        Call<okhttp3.ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfilCandidato(mock(CandidatoPerfilRequest.class), criarPerfilCallback);

        captureCallback(mockCall).onResponse(mockCall, Response.error(422, errorBody));

        verify(criarPerfilCallback).onError("Erro ao criar perfil (código 422)");
    }

    @Test
    public void criarPerfilCandidato_falhaDeRede_chamaOnErrorSemConexao() {
        Call<okhttp3.ResponseBody> mockCall = mockCall();
        when(apiService.criarPerfilCandidato(any())).thenReturn(mockCall);

        repository.criarPerfilCandidato(mock(CandidatoPerfilRequest.class), criarPerfilCallback);

        captureCallback(mockCall).onFailure(mockCall, new IOException());

        verify(criarPerfilCallback).onError("Sem conexão com o servidor");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

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

    private CadastroRequest umCadastroRequest() {
        return new CadastroRequest("Nome", "email@test.com", "senha",
                "2000-01-01", "CANDIDATO", true);
    }
}
