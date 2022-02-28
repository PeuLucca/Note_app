package com.example_2_060303.note.model;

import java.io.Serializable;

public class Tarefa implements Serializable {

    private Long id;
    private Long status;
    private String titulo;
    private String descricao;
    private String conteudo;
    private String data;
    private String horario;
    private Long favorito;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Long getFavorito() {
        return favorito;
    }

    public void setFavorito(Long favorito) {
        this.favorito = favorito;
    }
}
