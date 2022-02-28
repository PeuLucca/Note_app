package com.example_2_060303.note.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example_2_060303.note.model.Tarefa;
import com.example_2_060303.note.model.Tarefa_Rapida;

import java.util.ArrayList;
import java.util.List;

public class DaoTarefaRapida implements IDaoTarefaRapida{

    private SQLiteDatabase escreve,le;

    public DaoTarefaRapida(Context context) {

        DbHelper dbHelper = new DbHelper( context );
        escreve = dbHelper.getWritableDatabase();
        le = dbHelper.getReadableDatabase();
    }

    @Override
    public boolean salvar( Tarefa_Rapida tarefa_rapida ) {

        ContentValues cv = new ContentValues();
        cv.put( "conteudo" , tarefa_rapida.getConteudo() );

        try{
            escreve.insert( DbHelper.TABELA_TAREFA_RAPIDA , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }

    @Override
    public boolean deletar(Tarefa_Rapida tarefa_rapida) {

        String[] args = { tarefa_rapida.getId().toString() };

        try {
            escreve.delete( DbHelper.TABELA_TAREFA_RAPIDA,"id=?",args);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }

    @Override
    public boolean atualizar(Tarefa_Rapida tarefa_rapida) {

        ContentValues cv = new ContentValues();
        cv.put( "conteudo" , tarefa_rapida.getConteudo() );

        try{
            String[] args = { tarefa_rapida.getId().toString() };
            escreve.update( DbHelper.TABELA_TAREFA_RAPIDA, cv, "id=?" , args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;

    }

    @Override
    public List<Tarefa_Rapida> listar() {

        List<Tarefa_Rapida> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA_RAPIDA + ";";
        Cursor cursor = le.rawQuery( sqlListar , null );

        while( cursor.moveToNext() ){

            Tarefa_Rapida tarefa_rapida = new Tarefa_Rapida();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );

            tarefa_rapida.setId( id );
            tarefa_rapida.setConteudo( conteudo );

            tarefaList.add( tarefa_rapida );
        }

        return tarefaList;
    }

    @Override
    public List<Tarefa_Rapida> verificarTarefa( String conteudo ){

        List<Tarefa_Rapida> lista = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA_RAPIDA +
                " WHERE conteudo = '" + conteudo + "';";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") String conteudo2 = cursor.getString( cursor.getColumnIndex( "conteudo" ) );

            Tarefa_Rapida tarefa = new Tarefa_Rapida();
            tarefa.setId( id );
            tarefa.setConteudo( conteudo2 );

            lista.add( tarefa );
        }

        return lista; // se nao existir esta tarefa
    }

}
