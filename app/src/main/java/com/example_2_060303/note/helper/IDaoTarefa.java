package com.example_2_060303.note.helper;

import android.content.Context;

import com.example_2_060303.note.model.Tarefa;
import com.example_2_060303.note.model.Tarefa_Rapida;

import java.util.List;

public interface IDaoTarefa {

    public boolean salvar( Tarefa tarefa );

    public boolean deletar( Tarefa tarefa );

    public boolean atualizar( Tarefa tarefa );
    public boolean atualizarFav( Tarefa tarefa );
    public boolean atualizarSenha( Tarefa tarefa );

    public List<Tarefa>listar( String ordenarPor );
    public List<Tarefa>listarSttsConcluido();
    public List<Tarefa>listarSttsNaoConcluido();
    public List<Tarefa>listarSemStts();
    public List<Tarefa>listarFav();
    public List<Tarefa>listarData();
    public List<Tarefa>listarTitulo();
    public List<Tarefa>listarTarefasSemSenha();
    public List<Tarefa>listarTarefasComSenha();

    public List<Tarefa> verificarTarefa( String titulo, String conteudo );
    public boolean verificarFav( String titulo, String conteudo );
    public String verificarSenha( String titulo, String conteudo );

}
