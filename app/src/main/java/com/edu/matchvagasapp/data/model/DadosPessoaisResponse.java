package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Mapeia a resposta completa de GET /api/candidatos/meu-perfil
 * (CandidatoResponseDTO do backend).
 */
public class DadosPessoaisResponse {

    // Campos diretos
    @SerializedName("nome")                  private String nome;
    @SerializedName("email")                 private String email;
    @SerializedName("cpf")                   private String cpf;
    @SerializedName("dataNascimento")        private String dataNascimento;
    @SerializedName("objetivoProfissional")  private String objetivoProfissional;
    @SerializedName("disponibilidade")       private String disponibilidade;
    @SerializedName("pretensaoSalarial")     private BigDecimal pretensaoSalarial;

    // Objetos aninhados
    @SerializedName("telefone")              private TelefoneInfo telefone;
    @SerializedName("localizacao")           private LocalizacaoInfo localizacao;

    // ── Getters diretos ───────────────────────────────────────────────────────
    public String getNome()                  { return nome; }
    public String getEmail()                 { return email; }
    public String getCpf()                   { return cpf; }
    public String getDataNascimento()        { return dataNascimento; }
    public String getObjetivoProfissional()  { return objetivoProfissional; }
    public String getDisponibilidade()       { return disponibilidade; }
    public BigDecimal getPretensaoSalarial() { return pretensaoSalarial; }
    public TelefoneInfo getTelefone()        { return telefone; }
    public LocalizacaoInfo getLocalizacao()  { return localizacao; }

    // ── Helpers de conveniência (compatibilidade com PerfilFragment) ──────────
    public String getTelefoneNumero()  { return telefone != null ? telefone.getNumero() : null; }
    public String getCep()             { return localizacao != null ? localizacao.getCep() : null; }
    public String getCidade()          { return localizacao != null ? localizacao.getCidade() : null; }
    public String getEstado()          { return localizacao != null ? localizacao.getEstado() : null; }
    public String getLogradouro()      { return localizacao != null ? localizacao.getLogradouro() : null; }
    public String getNumeroEnd()       { return localizacao != null ? localizacao.getNumero() : null; }
    public String getComplemento()     { return localizacao != null ? localizacao.getCompleto() : null; }
    public String getBairro()          { return localizacao != null ? localizacao.getBairro() : null; }

    // ── Inner classes ─────────────────────────────────────────────────────────

    public static class TelefoneInfo {
        @SerializedName("numero")          private String numero;
        @SerializedName("tipoTelefoneId")  private Long tipoTelefoneId;
        @SerializedName("wpp")             private boolean wpp;

        public String getNumero()          { return numero; }
        public Long getTipoTelefoneId()    { return tipoTelefoneId; }
        public boolean isWpp()             { return wpp; }
    }

    public static class LocalizacaoInfo {
        @SerializedName("logradouro")      private String logradouro;
        @SerializedName("numero")          private String numero;
        @SerializedName("completo")        private String completo;
        @SerializedName("bairro")          private String bairro;
        @SerializedName("cep")             private String cep;
        @SerializedName("cidade")          private String cidade;
        @SerializedName("estado")          private String estado;

        public String getLogradouro()      { return logradouro; }
        public String getNumero()          { return numero; }
        public String getCompleto()        { return completo; }
        public String getBairro()          { return bairro; }
        public String getCep()             { return cep; }
        public String getCidade()          { return cidade; }
        public String getEstado()          { return estado; }
    }
}
