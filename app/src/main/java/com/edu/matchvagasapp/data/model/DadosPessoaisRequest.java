package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Mapeia para CandidatoRequestDTO do backend (PUT /api/candidatos/meu-perfil).
 * Campos genero, linkedin e portfolio não existem no backend e foram omitidos.
 */
public class DadosPessoaisRequest {

    @SerializedName("cpf")
    private final String cpf;

    @SerializedName("nomeCompleto")
    private final String nomeCompleto;

    @SerializedName("email")
    private final String email;

    /** Formato ISO: yyyy-MM-dd */
    @SerializedName("dataNascimento")
    private final String dataNascimento;

    /** Mapeado para resumoProfissional no backend */
    @SerializedName("resumoProfissional")
    private final String resumoProfissional;

    @SerializedName("disponibilidade")
    private final String disponibilidade;

    @SerializedName("pretensaoSalarial")
    private final BigDecimal pretensaoSalarial;

    /** null → backend não altera o telefone existente */
    @SerializedName("telefone")
    private final TelefoneRequest telefone;

    /** null → backend não altera o endereço existente */
    @SerializedName("localizacao")
    private final LocalizacaoRequest localizacao;

    public DadosPessoaisRequest(String cpf, String nomeCompleto, String email,
                                String dataNascimento, String resumoProfissional,
                                String disponibilidade, BigDecimal pretensaoSalarial,
                                TelefoneRequest telefone, LocalizacaoRequest localizacao) {
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.resumoProfissional = resumoProfissional;
        this.disponibilidade = disponibilidade;
        this.pretensaoSalarial = pretensaoSalarial;
        this.telefone = telefone;
        this.localizacao = localizacao;
    }

    // ── Nested ───────────────────────────────────────────────────────────────

    public static class TelefoneRequest {
        @SerializedName("numero")
        private final String numero;

        @SerializedName("tipoTelefoneId")
        private final long tipoTelefoneId;

        @SerializedName("wpp")
        private final boolean wpp;

        public TelefoneRequest(String numero, long tipoTelefoneId, boolean wpp) {
            this.numero = numero;
            this.tipoTelefoneId = tipoTelefoneId;
            this.wpp = wpp;
        }
    }

    public static class LocalizacaoRequest {
        @SerializedName("logradouro")
        private final String logradouro;

        @SerializedName("numero")
        private final String numero;

        @SerializedName("complemento")
        private final String complemento;

        @SerializedName("bairro")
        private final String bairro;

        @SerializedName("cep")
        private final String cep;

        @SerializedName("cidade")
        private final String cidade;

        @SerializedName("estado")
        private final String estado;

        public LocalizacaoRequest(String logradouro, String numero, String complemento,
                                  String bairro, String cep, String cidade, String estado) {
            this.logradouro = logradouro;
            this.numero = numero;
            this.complemento = complemento;
            this.bairro = bairro;
            this.cep = cep;
            this.cidade = cidade;
            this.estado = estado;
        }
    }
}
