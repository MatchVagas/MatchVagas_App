# Histórico de Modificações — MatchVagas App

---

## [2026-05-10] Integração da tela de login com a API backend

### Contexto
A tela de login possuía validação local completa, mas o botão "Entrar" apenas navegava para o Dashboard sem chamar o backend. O backend já expunha `POST /api/auth/login` retornando um JWT.

### Arquivos criados

#### `app/src/main/java/com/edu/matchvagasapp/data/model/LoginRequest.java`
Modelo de requisição enviado ao backend. Campos: `email`, `senha` (mapeados via `@SerializedName` para Gson).

#### `app/src/main/java/com/edu/matchvagasapp/data/model/LoginResponse.java`
Modelo de resposta do backend (`AuthResponse`). Campos: `token`, `tipo`, `usuarioId`, `nome`, `email`, `perfil`.

#### `app/src/main/java/com/edu/matchvagasapp/data/local/TokenManager.java`
Gerenciador de sessão usando `SharedPreferences`. Persiste e recupera: JWT token, usuarioId, nome e perfil. Método `isLogado()` para verificar sessão ativa. Método `limpar()` para logout.

### Arquivos modificados

#### `app/src/main/java/com/edu/matchvagasapp/data/network/ApiService.java`
- Adicionado endpoint de login:
  ```java
  @POST("api/auth/login")
  Call<LoginResponse> login(@Body LoginRequest request);
  ```

#### `app/src/main/java/com/edu/matchvagasapp/data/repository/AuthRepository.java`
- Adicionada interface `LoginCallback` com `onSuccess(LoginResponse)` e `onError(String)`.
- Adicionado método `login(email, senha, callback)` com tratamento de erros HTTP:
  - `401` → "E-mail ou senha incorretos"
  - `403` → "Conta inativa. Entre em contato com o suporte"
  - Outros → "Erro no servidor (código X)"
  - Falha de rede → "Sem conexão com o servidor"

#### `app/src/main/java/com/edu/matchvagasapp/features/login/LoginFragment.java`
- Removido o `TODO` de integração.
- Adicionada chamada a `AuthRepository.login()` ao clicar em "Entrar".
- Token salvo via `TokenManager` após login bem-sucedido.
- Botão desabilitado com texto "Entrando..." durante a requisição (feedback visual).
- Erros exibidos via `Toast`.
- Verificação `isAdded()` antes de atualizar a UI (evita crash se o fragment for destruído durante a chamada).

### Endpoint backend utilizado
```
POST http://10.0.2.2:8080/api/auth/login
Content-Type: application/json

{ "email": "...", "senha": "..." }

Resposta 200:
{ "token": "...", "tipo": "Bearer", "usuarioId": 1, "nome": "...", "email": "...", "perfil": "CANDIDATO" }
```

---

## [2026-05-10] Segurança — URL da API movida para BuildConfig

### Contexto
A URL base da API estava hardcoded em `RetrofitClient.java`, impossibilitando trocar entre ambientes (dev/produção) sem alterar o código-fonte.

### Arquivos modificados

#### `app/build.gradle.kts`
- Habilitado `buildFeatures { buildConfig = true }` para geração da classe `BuildConfig`.
- Adicionado bloco `debug` com URL do emulador:
  ```
  buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
  ```
- Adicionado `buildConfigField` no bloco `release` com URL de produção:
  ```
  buildConfigField("String", "BASE_URL", "\"https://api.matchvagas.com/\"")
  ```
- Ativado `isMinifyEnabled = true` no release (estava `false`).

#### `app/src/main/java/com/edu/matchvagasapp/data/network/RetrofitClient.java`
- Substituída a string hardcoded por `BuildConfig.BASE_URL`.
- Removido comentário que expunha detalhe de implementação do emulador.

### Resultado
- Build debug aponta para `http://10.0.2.2:8080/` automaticamente.
- Build release aponta para `https://api.matchvagas.com/` automaticamente.
- Nenhuma URL fica como literal no código-fonte.

### Pendência (backend)
O `jwt.secret` em `backend/src/main/resources/application.properties` está hardcoded e marcado com TODO. Deve ser movido para variável de ambiente antes de ir para produção.

---

## [2026-05-11] Integração do perfil do candidato com a API backend

### Contexto
As telas de edição do perfil (dados pessoais, experiência, formação e habilidades) tinham validação local completa, mas o botão "Salvar" apenas exibia um Snackbar e voltava sem enviar nada ao backend.

### Arquivos criados

#### `app/src/main/java/com/edu/matchvagasapp/data/model/DadosPessoaisRequest.java`
Modelo de requisição para `PUT /api/perfil/dados-pessoais`. Campos: `nome`, `email`, `telefone`, `dataNascimento`, `cpf`, `genero`, `cep`, `cidade`, `estado`, `linkedin`, `portfolio`.

#### `app/src/main/java/com/edu/matchvagasapp/data/model/ExperienciaRequest.java`
Modelo de requisição para `POST /api/perfil/experiencias`. Campos: `cargo`, `empresa`, `modalidade`, `vinculo`, `cidade`, `mesInicio`, `anoInicio`, `mesSaida`, `anoSaida`, `empregoAtual`. Campos `mesSaida`/`anoSaida` são `null` quando `empregoAtual = true`.

#### `app/src/main/java/com/edu/matchvagasapp/data/model/FormacaoRequest.java`
Modelo de requisição para `POST /api/perfil/formacoes`. Campos: `instituicao`, `curso`, `grau`, `mesInicio`, `anoInicio`, `mesConclusao`, `anoConclusao`, `aindaCursando`. Campos de conclusão são `null` quando `aindaCursando = true`.

#### `app/src/main/java/com/edu/matchvagasapp/data/model/HabilidadesRequest.java`
Modelo de requisição para `PUT /api/perfil/habilidades`. Campo: `habilidades` (lista com os nomes originais extraídos dos chips).

#### `app/src/main/java/com/edu/matchvagasapp/data/repository/PerfilRepository.java`
Repositório com interface `PerfilCallback` (`onSuccess()` / `onError(String)`) e quatro métodos:
- `atualizarDadosPessoais(request, callback)` → `PUT /api/perfil/dados-pessoais`
- `adicionarExperiencia(request, callback)` → `POST /api/perfil/experiencias`
- `adicionarFormacao(request, callback)` → `POST /api/perfil/formacoes`
- `atualizarHabilidades(request, callback)` → `PUT /api/perfil/habilidades`
Trata `401/403` como sessão expirada e falha de rede separadamente.

### Arquivos modificados

#### `app/src/main/java/com/edu/matchvagasapp/data/network/ApiService.java`
- Adicionados imports e endpoints:
  ```java
  @PUT("api/perfil/dados-pessoais")   Call<Void> atualizarDadosPessoais(@Body DadosPessoaisRequest);
  @POST("api/perfil/experiencias")    Call<Void> adicionarExperiencia(@Body ExperienciaRequest);
  @POST("api/perfil/formacoes")       Call<Void> adicionarFormacao(@Body FormacaoRequest);
  @PUT("api/perfil/habilidades")      Call<Void> atualizarHabilidades(@Body HabilidadesRequest);
  ```

#### Fragments `EditarDadosPessoaisFragment`, `EditarExperienciaFragment`, `EditarFormacaoFragment`, `EditarHabilidadesFragment`
- Cada um ganhou `PerfilRepository` como campo e `setLoading(boolean)` que desabilita o botão e exibe "Salvando…".
- O método de salvar agora coleta todos os campos, monta o request e chama o repositório.
- Erros exibidos via `Snackbar`; navegação de volta só ocorre após resposta bem-sucedida.
- Verificação `isAdded()` nos callbacks para evitar crash se o fragment for destruído durante a requisição.
- Em habilidades: a lista é extraída dos chips (nomes com capitalização original), não do Set interno (lowercase).

#### `app/src/main/res/values/strings.xml`
- Adicionada string `salvando` = "Salvando…".

### Endpoints backend utilizados
```
PUT  http://10.0.2.2:8080/api/perfil/dados-pessoais  (requer JWT — CANDIDATO)
POST http://10.0.2.2:8080/api/perfil/experiencias    (requer JWT — CANDIDATO)
POST http://10.0.2.2:8080/api/perfil/formacoes       (requer JWT — CANDIDATO)
PUT  http://10.0.2.2:8080/api/perfil/habilidades     (requer JWT — CANDIDATO)
```
Todas as rotas são protegidas pelo `AuthInterceptor` (JWT injetado automaticamente).

---

## [2026-05-10] Segurança — Token JWT armazenado com EncryptedSharedPreferences

### Contexto
O `TokenManager` usava `SharedPreferences` comum, que persiste dados em texto puro em `/data/data/.../shared_prefs/matchvagas_prefs.xml`. Em dispositivos com root qualquer app consegue ler esse arquivo.

### Solução
Migração para `EncryptedSharedPreferences` (Android Jetpack Security), que cifra chaves e valores usando o **Android Keystore** — a chave de criptografia fica no hardware e nunca é exposta ao processo do app.

- Esquema de chave: `AES256_GCM` via `MasterKey`
- Esquema de cifragem de chaves do prefs: `AES256_SIV`
- Esquema de cifragem de valores do prefs: `AES256_GCM`
- Fallback para `SharedPreferences` simples caso o Keystore seja inacessível (dispositivos muito antigos), com log de erro.

### Arquivos modificados

#### `gradle/libs.versions.toml`
- Adicionada versão `securityCrypto = "1.1.0-alpha06"`.
- Adicionada entrada de biblioteca `security-crypto`.

#### `app/build.gradle.kts`
- Adicionada dependência `implementation(libs.security.crypto)`.

#### `app/src/main/java/com/edu/matchvagasapp/data/local/TokenManager.java`
- Substituído `SharedPreferences` por `EncryptedSharedPreferences`.
- Lógica de criação extraída para método `buildPrefs()` para evitar inicializador redundante.
- Nome do arquivo de prefs alterado de `matchvagas_prefs` → `matchvagas_secure_prefs` (arquivo antigo não criptografado será ignorado).

### Ação necessária após merge
Fazer **Gradle Sync** no Android Studio para baixar a dependência `androidx.security:security-crypto`.

---

## [2026-05-11] Integração completa da API backend (vagas, cadastro e candidaturas)

### Contexto
O app tinha a tela de login integrada ao backend, mas o restante da aplicação ainda usava dados fictícios e não chamava nenhum endpoint real. O cadastro navegava para o Dashboard sem registrar o usuário, a tela de confirmação de candidatura exibia um dialog sem enviar nada ao servidor, e as vagas na Home eram hardcoded no layout.

### Arquivos criados

#### `app/src/main/java/com/edu/matchvagasapp/MatchVagasApp.java`
Application class que armazena o `Context` global da aplicação. Necessária para que o `RetrofitClient` (singleton) possa instanciar o `TokenManager` sem receber um `Context` por parâmetro a cada chamada.

#### `app/src/main/java/com/edu/matchvagasapp/data/network/AuthInterceptor.java`
Interceptor OkHttp que injeta o header `Authorization: Bearer {token}` em todas as requisições HTTP. Lê o token do `TokenManager`; se não houver token (usuário não autenticado), a requisição segue sem o header.

#### `app/src/main/java/com/edu/matchvagasapp/data/model/VagaResponse.java`
Modelo de resposta mapeado para `VagaResponseDTO` do backend. Campos: `id`, `titulo`, `nomeFantasiaEmpresa`, `empresaId`, `descricao`, `requisitos`, `beneficios`, `modalidadeDescricao`, `tipoVagaDescricao`, `salarioMinimo`, `salarioMaximo`, `nomeCidade`, `ufEstado`, `areaAtuacao`. Inclui helpers `getLocalFormatado()`, `getSalarioFormatado()` e `getInicialEmpresa()` para uso direto na UI.

#### `app/src/main/java/com/edu/matchvagasapp/data/model/CandidaturaRequest.java`
Modelo de requisição para `POST /api/candidaturas`. Campos: `vagaId` + 8 flags booleanas de compartilhamento de dados (`compartilharCurriculo`, `compartilharTelefone`, etc.) mapeadas para `CandidaturaRequestDTO` do backend.

#### `app/src/main/java/com/edu/matchvagasapp/data/model/CandidaturaResponse.java`
Modelo de resposta do endpoint de candidatura. Campos: `id`, `vagaId`, `tituloVaga`, `status`, `dataCandidatura`, flags de compartilhamento.

#### `app/src/main/java/com/edu/matchvagasapp/data/repository/VagaRepository.java`
Repositório para o recurso de vagas. Métodos:
- `buscarVagas(callback)` → `GET /api/vagas`
- `buscarVagaPorId(id, callback)` → `GET /api/vagas/{id}`
- `buscarVagasFiltradas(titulo, areaAtuacao, callback)` → `GET /api/vagas?titulo=...&areaAtuacao=...`

#### `app/src/main/java/com/edu/matchvagasapp/data/repository/CandidaturaRepository.java`
Repositório para candidaturas. Métodos:
- `candidatar(request, callback)` → `POST /api/candidaturas`
  - Trata `409` como "você já se candidatou a esta vaga"
  - Trata `401/403` como "sessão expirada"
- `minhasCandidaturas(callback)` → `GET /api/candidaturas/minhas`

### Arquivos modificados

#### `app/src/main/AndroidManifest.xml`
- Adicionado `android:name=".MatchVagasApp"` na tag `<application>` para registrar a Application class.

#### `app/src/main/java/com/edu/matchvagasapp/data/network/RetrofitClient.java`
- Adicionado `OkHttpClient` com `AuthInterceptor` na construção do `Retrofit`.
- O `TokenManager` é instanciado via `MatchVagasApp.getAppContext()`, eliminando a necessidade de passar `Context` externamente.

#### `app/src/main/java/com/edu/matchvagasapp/data/network/ApiService.java`
- Adicionados endpoints:
  ```java
  @GET("api/vagas")
  Call<List<VagaResponse>> buscarVagas(@Query("titulo") ..., @Query("areaAtuacao") ..., ...);

  @GET("api/vagas/{id}")
  Call<VagaResponse> buscarVagaPorId(@Path("id") Long id);

  @POST("api/candidaturas")
  Call<CandidaturaResponse> candidatar(@Body CandidaturaRequest request);

  @GET("api/candidaturas/minhas")
  Call<List<CandidaturaResponse>> minhasCandidaturas();
  ```

#### `app/src/main/java/com/edu/matchvagasapp/features/cadastro/CadastroFragment.java`
- Removido `TODO` e a navegação direta para o Dashboard.
- `finalizarCadastro()` agora:
  1. Coleta os dados do passo 1 (nome, email, senha, tipoUsuario, dataNascimento).
  2. Chama `AuthRepository.cadastrar()` → `POST /api/auth/register`.
  3. Em caso de sucesso, faz auto-login via `AuthRepository.login()`.
  4. Salva o JWT no `TokenManager` e navega para o Dashboard.
  5. Em caso de erro, exibe `Snackbar` na própria tela.
- Adicionado `getTipoUsuario()` — mapeia o chip selecionado (`chip_candidato` / `chip_empresa`) para a string esperada pelo backend (`"CANDIDATO"` / `"EMPRESA"`).
- Adicionado `formatarDataNascimento(millis)` — converte o timestamp UTC do `MaterialDatePicker` para o formato ISO `"yyyy-MM-ddT00:00:00"`.
- Adicionado `setCadastroLoading(boolean)` — desabilita o botão "Finalizar" e exibe "Aguarde..." durante a requisição.

#### `app/src/main/java/com/edu/matchvagasapp/features/vagas/DetalhesVagaFragment.java`
- Adicionada constante `ARG_VAGA_ID = "vagaId"`.
- `vagaId` lido dos argumentos e repassado ao `Bundle` de navegação para `ConfirmarCandidaturaFragment`.

#### `app/src/main/java/com/edu/matchvagasapp/features/candidatura/ConfirmarCandidaturaFragment.java`
- Adicionada constante `ARG_VAGA_ID = "vagaId"`.
- `enviarCandidatura()` agora:
  1. Lê `vagaId` dos argumentos; exibe erro se não encontrado.
  2. Lê o estado dos 6 switches de compartilhamento.
  3. Monta `CandidaturaRequest` e chama `CandidaturaRepository.candidatar()`.
  4. Exibe o dialog de sucesso apenas após resposta `201` do backend.
  5. Exibe `Snackbar` com a mensagem de erro em caso de falha.
- Adicionado `setEnvioLoading(boolean)` — desabilita o botão e exibe "Enviando..." durante a requisição.

#### `app/src/main/java/com/edu/matchvagasapp/features/home/HomeFragment.java`
- Adicionado `VagaRepository` como campo.
- `onViewCreated()` chama `carregarVagas()` assim que a view é criada.
- `carregarVagas()` faz `GET /api/vagas`, armazena os IDs das primeiras 3 vagas retornadas nos campos `vagaId1`, `vagaId2`, `vagaId3` e substitui os listeners dos cards/botões para usar dados reais da API.
- Falha na API é silenciosa — os cards ficam com o conteúdo estático do layout (fallback).
- `vagaId` é incluído no `Bundle` passado para `DetalhesVagaFragment` e `ConfirmarCandidaturaFragment`.

#### `app/src/main/java/com/edu/matchvagasapp/features/vagas/VagasFragment.java`
- `navigateToDetalhes()` recebe `vagaId` como parâmetro e o inclui no `Bundle` de navegação.

### Endpoints backend utilizados
```
POST http://10.0.2.2:8080/api/auth/register       (público)
GET  http://10.0.2.2:8080/api/vagas               (público)
GET  http://10.0.2.2:8080/api/vagas/{id}          (público)
POST http://10.0.2.2:8080/api/candidaturas        (requer JWT — CANDIDATO)
GET  http://10.0.2.2:8080/api/candidaturas/minhas (requer JWT — CANDIDATO)
```

---
