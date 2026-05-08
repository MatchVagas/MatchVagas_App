package com.edu.matchvagasapp.data.model;

public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private Integer idade;
    private Boolean ativo;
    private String tipoUsuario;
    private String dataCadastro;

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public Integer getIdade() { return idade; }
    public Boolean getAtivo() { return ativo; }
    public String getTipoUsuario() { return tipoUsuario; }
    public String getDataCadastro() { return dataCadastro; }
}
