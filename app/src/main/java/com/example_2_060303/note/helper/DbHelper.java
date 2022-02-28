package com.example_2_060303.note.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static int VERSION_DB = 4;
    public static String NOME_DB = "db_note";

    public static String TABELA_TAREFA = "tarefa";
    public static String TABELA_TAREFA_RAPIDA = "tarefa_rapida";

    private static final String sqlCriarTarefa =
            "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFA +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "status INTEGER," +
            "titulo TEXT NOT NULL, " +
            "descricao TEXT, " +
            "conteudo TEXT NOT NULL, " +
            "data TEXT, " +
            "horario TEXT, " +
            "favoritar INTEGER);";

    private static final String sqlCriarTarefaRapida =
            "CREATE TABLE IF NOT EXISTS " + TABELA_TAREFA_RAPIDA +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "conteudo TEXT);";

    private static final String sqlAddData = "ALTER TABLE " + TABELA_TAREFA + " ADD COLUMN data TEXT;";

    private static final String sqlAddHra = "ALTER TABLE " + TABELA_TAREFA + " ADD COLUMN horario TEXT;";

    private static final String sqlAddFavorito = "ALTER TABLE " + TABELA_TAREFA + "" +
            " ADD COLUMN favoritar INTEGER;";

    public DbHelper(@Nullable Context context) {
        super(context, NOME_DB, null, VERSION_DB);
         //context.deleteDatabase(NOME_DB);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            db.execSQL( sqlCriarTarefa );
            db.execSQL( sqlCriarTarefaRapida );

        }catch (Exception e){

            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        try {
            if( oldVersion < 2 ){
                db.execSQL( sqlAddData );
                db.execSQL( sqlAddHra );
            }
            if( oldVersion < 3 ){
                db.execSQL( sqlAddFavorito );
            }
            if( oldVersion < 4 ){
                db.execSQL( sqlCriarTarefaRapida );
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
