package com.edu.matchvagasapp.data.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class VagaResponse {

    @SerializedName("id")
    private Long id;

    @SerializedName("titulo")
    private String titulo;

    @SerializedName("nomeFantasiaEmpresa")
    private String nomeFantasiaEmpresa;

    @SerializedName("empresaId")
    private Long empresaId;

    @SerializedName("descricao")
    private String descricao;

    @SerializedName("requisitos")
    private String requisitos;

    @SerializedName("beneficios")
    private String beneficios;

    @SerializedName("modalidadeDescricao")
    private String modalidadeDescricao;

    @SerializedName("tipoVagaDescricao")
    private String tipoVagaDescricao;

    @SerializedName("salarioMinimo")
    private BigDecimal salarioMinimo;

    @SerializedName("salarioMaximo")
    private BigDecimal salarioMaximo;

    @SerializedName("nomeCidade")
    private String nomeCidade;

    @SerializedName("ufEstado")
    private String ufEstado;

    @SerializedName("areaAtuacao")
    private String areaAtuacao;

    @SerializedName("statusDescricao")
    private String statusDescricao;

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getNomeFantasiaEmpresa() { return nomeFantasiaEmpresa; }
    public Long getEmpresaId() { return empresaId; }
    public String getDescricao() { return descricao; }
    public String getRequisitos() { return requisitos; }
    public String getBeneficios() { return beneficios; }
    public String getModalidadeDescricao() { return modalidadeDescricao; }
    public String getTipoVagaDescricao() { return tipoVagaDescricao; }
    public BigDecimal getSalarioMinimo() { return salarioMinimo; }
    public BigDecimal getSalarioMaximo() { return salarioMaximo; }
    public String getNomeCidade() { return nomeCidade; }
    public String getUfEstado() { return ufEstado; }
    public String getAreaAtuacao() { return areaAtuacao; }
    public String getStatusDescricao() { return statusDescricao; }

    public String getLocalFormatado() {
        if (nomeCidade != null && ufEstado != null) return nomeCidade + ", " + ufEstado;
        if (nomeCidade != null) return nomeCidade;
        if (modalidadeDescricao != null) return modalidadeDescricao;
        return "";
    }

    public String getSalarioFormatado() {
        if (salarioMinimo == null && salarioMaximo == null) return "A negociar";
        if (salarioMinimo != null && salarioMaximo != null) {
            return "R$ " + salarioMinimo.toPlainString() + " – R$ " + salarioMaximo.toPlainString();
        }
        if (salarioMinimo != null) return "A partir de R$ " + salarioMinimo.toPlainString();
        return "Até R$ " + salarioMaximo.toPlainString();
    }

    public String getInicialEmpresa() {
        if (nomeFantasiaEmpresa != null && !nomeFantasiaEmpresa.isEmpty()) {
            return String.valueOf(nomeFantasiaEmpresa.charAt(0)).toUpperCase();
        }
        return "?";
    }
}
