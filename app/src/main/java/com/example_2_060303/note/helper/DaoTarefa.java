package com.example_2_060303.note.helper;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.example_2_060303.note.model.Tarefa;
import com.example_2_060303.note.model.Tarefa_Rapida;

import java.util.ArrayList;
import java.util.List;

public class DaoTarefa implements IDaoTarefa {

    private SQLiteDatabase escreve,le;

    public DaoTarefa(Context context) {
            // toda vez ao salvar, deletar, atualizar ou listar
            DbHelper dbHelper = new DbHelper( context );
            escreve = dbHelper.getWritableDatabase();
            le = dbHelper.getReadableDatabase();
        }

    @Override
    public boolean salvar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );
        cv.put( "titulo" , tarefa.getTitulo() );
        cv.put( "descricao" , tarefa.getDescricao() );
        cv.put( "conteudo" , tarefa.getConteudo() );
        cv.put( "data" , tarefa.getData() );
        cv.put( "horario" , tarefa.getHorario() );

        try{
            escreve.insert( DbHelper.TABELA_TAREFA , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean salvarSemStatus(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );
        cv.put( "titulo" , tarefa.getTitulo() );
        cv.put( "descricao" , tarefa.getDescricao() );
        cv.put( "conteudo" , tarefa.getConteudo() );
        cv.put( "data" , tarefa.getData() );
        cv.put( "horario" , tarefa.getHorario() );

        try{
            escreve.insert( DbHelper.TABELA_TAREFA , null, cv );
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {

        String[] args = { tarefa.getId().toString() };

        try {
            escreve.delete( DbHelper.TABELA_TAREFA,"id=?",args);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );
        cv.put( "titulo" , tarefa.getTitulo() );
        cv.put( "descricao" , tarefa.getDescricao() );
        cv.put( "conteudo" , tarefa.getConteudo() );
        cv.put( "data" , tarefa.getData() );
        cv.put( "horario" , tarefa.getHorario() );
        cv.put( "favoritar" , tarefa.getFavorito() );

        try{
            String[] args = { tarefa.getId().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "id=?" , args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarStatus(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "status" , tarefa.getStatus() );

        try{
            String[] args = { tarefa.getId().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "id=?" , args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean atualizarFav(Tarefa tarefa) {

        ContentValues cv = new ContentValues();
        cv.put( "favoritar" , tarefa.getFavorito() );

        try{
            String[] args = { tarefa.getId().toString() };
            escreve.update( DbHelper.TABELA_TAREFA, cv, "id=?" , args );

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @SuppressLint("Range")
    @Override
    public List<Tarefa> listar(String ordenarPor) {

        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " ORDER BY " + ordenarPor + ";";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );
            @SuppressLint("Range") String data = cursor.getString( cursor.getColumnIndex( "data" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") Long favoritar = cursor.getLong( cursor.getColumnIndex( "favoritar" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefa.setData( data );
            tarefa.setHorario( horario );
            tarefa.setFavorito(favoritar);
            tarefa.setFavorito( favoritar );

            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public List<Tarefa> listarSttsConcluido() {
        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " WHERE status = 1";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );
            @SuppressLint("Range") String data = cursor.getString( cursor.getColumnIndex( "data" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") Long favoritar = cursor.getLong( cursor.getColumnIndex( "favoritar" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefa.setData( data );
            tarefa.setHorario( horario );
            tarefa.setFavorito( favoritar );

            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public List<Tarefa> listarSttsNaoConcluido() {
        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " WHERE status = 0";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );
            @SuppressLint("Range") String data = cursor.getString( cursor.getColumnIndex( "data" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") Long favoritar = cursor.getLong( cursor.getColumnIndex( "favoritar" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefa.setData( data );
            tarefa.setHorario( horario );
            tarefa.setFavorito( favoritar );

            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public List<Tarefa> listarSemStts() {
        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " WHERE status = 100";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );
            @SuppressLint("Range") String data = cursor.getString( cursor.getColumnIndex( "data" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") Long favoritar = cursor.getLong( cursor.getColumnIndex( "favoritar" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefa.setData( data );
            tarefa.setHorario( horario );
            tarefa.setFavorito( favoritar );

            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public List<Tarefa> listarFav() {

        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " WHERE favoritar = 1";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );
            @SuppressLint("Range") String data = cursor.getString( cursor.getColumnIndex( "data" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") Long favoritar = cursor.getLong( cursor.getColumnIndex( "favoritar" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefa.setData( data );
            tarefa.setHorario( horario );
            tarefa.setFavorito( favoritar );

            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public List<Tarefa> verificarTarefa(String titulo, String conteudo){

        List<Tarefa> lista = new ArrayList<>();
        int resposta = 1;

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA +
                " WHERE titulo = '" + titulo + "' AND conteudo = '" + conteudo + "';";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") String titulo2 = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String conteudo2 = cursor.getString( cursor.getColumnIndex( "conteudo" ) );

            Tarefa tarefa = new Tarefa();
            tarefa.setId( id );
            tarefa.setTitulo( titulo2 );
            tarefa.setConteudo( conteudo2 );

            lista.add( tarefa );
        }

        return lista; // se nao existir esta tarefa
    }

    @Override
    public boolean verificarExistencia(String titulo) {

        List<Tarefa> listTarefa = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFA +
                " WHERE titulo = '" + titulo + "';";
        Cursor cursor = le.rawQuery( sql, null );

        while(cursor.moveToNext()){
            @SuppressLint("Range") String title = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            Tarefa tarefa = new Tarefa();
            tarefa.setTitulo(title);

            listTarefa.add(tarefa);
        }

        return !listTarefa.isEmpty();
    }

    @SuppressLint("Range")
    @Override
    public List<Tarefa> listarTitulo() {

        List<Tarefa> tarefaList = new ArrayList<>();

        String sqlListar = "SELECT * FROM " + DbHelper.TABELA_TAREFA + " ORDER BY titulo;";
        Cursor cursor = le.rawQuery( sqlListar, null );

        while(cursor.moveToNext()){

            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = cursor.getLong( cursor.getColumnIndex("id") );
            @SuppressLint("Range") Long status = cursor.getLong( cursor.getColumnIndex( "status" ) );
            @SuppressLint("Range") String titulo = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") String descricao = cursor.getString( cursor.getColumnIndex( "descricao" ) );
            @SuppressLint("Range") String conteudo = cursor.getString( cursor.getColumnIndex( "conteudo" ) );
            @SuppressLint("Range") String data = cursor.getString( cursor.getColumnIndex( "data" ) );
            @SuppressLint("Range") String horario = cursor.getString( cursor.getColumnIndex( "horario" ) );
            @SuppressLint("Range") Long favoritar = cursor.getLong( cursor.getColumnIndex( "favoritar" ) );

            tarefa.setId( id );
            tarefa.setStatus( status );
            tarefa.setTitulo( titulo );
            tarefa.setDescricao( descricao );
            tarefa.setConteudo( conteudo );
            tarefa.setData( data );
            tarefa.setHorario( horario );
            tarefa.setFavorito(favoritar);

            tarefaList.add( tarefa );
        }

        return tarefaList;
    }

    @Override
    public Tarefa verificarStatus(String titulo) {

        Tarefa tarefa = new Tarefa();
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFA +
                " WHERE titulo = '" + titulo + "';";
        Cursor cursor = le.rawQuery( sql, null );

        while(cursor.moveToNext()){
            @SuppressLint("Range") String title = cursor.getString( cursor.getColumnIndex( "titulo" ) );
            @SuppressLint("Range") Long status = cursor.getLong(cursor.getColumnIndex( "status" ) );

            tarefa.setTitulo(title);
            tarefa.setStatus( status );
        }

        return tarefa;
    }

    @Override
    public boolean verificarFav(String titulo, String conteudo){

        Tarefa tarefa = new Tarefa();
        String sql = "SELECT * FROM " + DbHelper.TABELA_TAREFA +
                " WHERE titulo = '" + titulo + "' AND conteudo = '" + conteudo + "';";
        Cursor cursor = le.rawQuery( sql, null );

        try{
            while(cursor.moveToNext()) {
                @SuppressLint("Range") Long favoritar = cursor.getLong(cursor.getColumnIndex("favoritar"));
                return true;
            }
        }catch (Exception e){
            return false;
        }

        return false;
    }

}