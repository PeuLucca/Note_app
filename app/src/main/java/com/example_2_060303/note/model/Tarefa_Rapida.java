package com.example_2_060303.note.model;

import java.io.Serializable;

public class Tarefa_Rapida implements Serializable {

    private Long id;
    private String conteudo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }
}
