package com.example_2_060303.note.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

public class SharedPref {

    private Context context;
    private static final String ARQUIVO_PREFERENCIA = "Arquivo Preferencia";
    private SharedPreferences preferences;

    /*
    - true --> não aparece mais bloco de avaliação
    - maisTarde --> aparece bloco de avaliação quando a listaTarefa > 10 itens
    - false --> aparece bloco de avaliação quando a listaTarefa > 3 itens
     */

    public SharedPref(Context c) {
        this.context = c;
    }

    public void salvarAvaliacao( String avaliacao ){

        preferences = context.getSharedPreferences( ARQUIVO_PREFERENCIA ,0 );
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("avaliacao", avaliacao );
        editor.commit(); // agora salvou os dados

    }

    public String verificarAvaliacao(){

        preferences = context.getSharedPreferences( ARQUIVO_PREFERENCIA ,0 );
        String avaliacao="";

        if( preferences.contains("avaliacao") ){
            avaliacao = preferences.getString("avaliacao", "");
        }else {
            avaliacao = "false";
        }

        return avaliacao;
    }

}
