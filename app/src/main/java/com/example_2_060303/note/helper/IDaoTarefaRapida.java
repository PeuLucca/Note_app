package com.example_2_060303.note.helper;

import com.example_2_060303.note.model.Tarefa;
import com.example_2_060303.note.model.Tarefa_Rapida;

import java.util.List;

public interface IDaoTarefaRapida {

    public boolean salvar (Tarefa_Rapida tarefa_rapida);
    public boolean deletar(Tarefa_Rapida tarefa_rapida);
    public boolean atualizar(Tarefa_Rapida tarefa_rapida);
    public List<Tarefa_Rapida> listar();
    public List<Tarefa_Rapida> verificarTarefa(String conteudo);
}
