package com.example_2_060303.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.helper.DaoTarefa;
import com.example_2_060303.note.helper.DaoTarefaRapida;
import com.example_2_060303.note.model.Tarefa;
import com.example_2_060303.note.model.Tarefa_Rapida;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

public class AddTarefaRapida extends AppCompatActivity {

    private EditText titulo, descricao, conteudo;
    private String StringTitulo,StringConteudo;
    private Tarefa_Rapida tarefaAtual;
    private String cont="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tarefa);

        // Google Ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = findViewById(R.id.adView);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdRequest adRequest = new AdRequest.Builder().build();
                adView.loadAd(adRequest);
            }
        }, 1000);

        titulo = findViewById(R.id.txtTitulo);
        descricao = findViewById(R.id.txtDescricao);
        conteudo = findViewById(R.id.txtConteudo);

        titulo.setVisibility( View.GONE );
        descricao.setVisibility( View.GONE );

        tarefaAtual = (Tarefa_Rapida) getIntent().getSerializableExtra("tarefaSelecionada");

        if( tarefaAtual!=null ){

            conteudo.setText( tarefaAtual.getConteudo() );
            cont = tarefaAtual.getConteudo();
        }

    }

    @Override
    public void onBackPressed() {

        if( !conteudo.getText().toString().equals("") ){

            if( conteudo.getText().toString().equals( cont ) ) {

                finish();

            }else {

                if( tarefaAtual == null ){ // tarefa nova

                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(AddTarefaRapida.this);
                    dialog2.setTitle(R.string.atencaoPT);
                    dialog2.setMessage(R.string.salvarAlteracoesAntesDeSairPT);
                    dialog2.setNegativeButton(R.string.naoPT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

                    dialog2.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            salvar();
                        }
                    });
                    dialog2.create().show();

                }

                if( !conteudo.getText().toString().equals( cont ) ){
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(AddTarefaRapida.this);
                    dialog2.setTitle(R.string.atencaoPT);
                    dialog2.setMessage(R.string.salvarAlteracoesAntesDeSairPT);
                    dialog2.setNegativeButton(R.string.naoPT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });

                    dialog2.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            salvar();
                        }
                    });
                    dialog2.create().show();
                }

            }

        }else {
            finish();
        }

    }

    public boolean verificarInputs(){

        if( conteudo.getText().toString().equals("") ){

            Toast.makeText(getApplicationContext(),
                    R.string.insiraCamposObrigatoriosPT, Toast.LENGTH_SHORT).show();

        }else {
            return true;
        }

        return false;
    }

    public void salvar(){

        DaoTarefaRapida daoTarefaRapida = new DaoTarefaRapida( getApplicationContext() );

        StringConteudo = conteudo.getText().toString();

        if( tarefaAtual == null ){ // para salvar

            Tarefa_Rapida tarefa_rapida = new Tarefa_Rapida();

            if( StringConteudo.equals( "" ) ){
                tarefa_rapida.setConteudo( "" );
            }else {
                tarefa_rapida.setConteudo( StringConteudo );
            }

            if( daoTarefaRapida.salvar( tarefa_rapida ) ){
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.tarefaSalvaComSucessoPT, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),
                        R.string.erroAoSalvarTarefaPT, Toast.LENGTH_LONG).show();
            }

        }else { // para atualizar

            Tarefa_Rapida tarefa_rapida = new Tarefa_Rapida();
            tarefa_rapida.setConteudo( StringConteudo );

            tarefa_rapida.setId( tarefaAtual.getId() );

            if( daoTarefaRapida.atualizar( tarefa_rapida ) ){
                finish();
                Toast.makeText(getApplicationContext(),
                        R.string.tarefaAtualizadaComSucessoPT, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),
                        R.string.erroAoSalvarTarefaPT, Toast.LENGTH_LONG).show();
            }

        }

    }

    public void deletar(){

        DaoTarefaRapida dao = new DaoTarefaRapida( getApplicationContext() );

        if( !conteudo.getText().toString().equals("") ){

            List<Tarefa_Rapida> lista = new ArrayList<>();

            lista = dao.verificarTarefa( conteudo.getText().toString() );

            if( !lista.isEmpty() ) {

                AlertDialog.Builder dialog = new AlertDialog.Builder( AddTarefaRapida.this );
                dialog.setTitle( getApplicationContext().getResources().getString(R.string.confExclusaoPT) + "\n" );
                dialog.setMessage( R.string.desejaExcluirAnotacaoPT2 );

                dialog.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if( dao.deletar( tarefaAtual ) ){

                            finish();
                            Toast.makeText(getApplicationContext(),
                                    R.string.tarefaExcluidaPT,Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getApplicationContext(),
                                    R.string.erroAoExcluirTarefaPT,Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.setNegativeButton(R.string.naoPT, null);

                dialog.create().show();

            }
            else { // se a tarefa nao existir
                Toast.makeText(getApplicationContext(),
                        R.string.paraDeletarPrecisaSalvarPT,
                        Toast.LENGTH_SHORT).show();
            }
        }
        else { // se a tarefa nao existir
            Toast.makeText(getApplicationContext(),
                    R.string.paraDeletarPrecisaSalvarPT,
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void compartilhar(){

        if( conteudo.getText().toString().isEmpty() ){

            Toast.makeText(getApplicationContext(),
                    R.string.necessarioTerAnotacaoSalvaParaCompartilharPT,
                    Toast.LENGTH_SHORT).show();

        }else {

            Intent sendIntent = new Intent ();
            sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sendIntent.setAction (Intent.ACTION_SEND);

            sendIntent.putExtra (Intent.EXTRA_TEXT, getApplicationContext().getResources().getString(R.string.compartilhadoAtravesdoNotePT) + "\n\n" +
                    getApplicationContext().getResources().getString(R.string.conteudoPT) + " \n" + conteudo.getText().toString() +
                    "\n\n\n" + getApplicationContext().getResources().getString(R.string.baixeAgoraEmPT) );

            sendIntent.setType ("text / plain");
            startActivity (sendIntent);

        }

    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate( R.menu.menu_add_tarefa_rapida, menu );
        return super.onCreatePanelMenu(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){

            case R.id.itemSalvar:

                if( verificarInputs() ){
                    salvar();
                }

                break;

            case R.id.itemDelete:

                deletar();
                break;

            case R.id.itemCompartilhar:

                compartilhar();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}