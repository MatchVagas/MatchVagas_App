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
