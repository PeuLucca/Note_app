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
import android.widget.EditText;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.helper.DaoTarefa;
import com.example_2_060303.note.model.Tarefa;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddTarefa extends AppCompatActivity {

    private EditText titulo, descricao, conteudo;
    private String tituloString, descricaoString, conteudoString;
    private Tarefa tarefaAtual;
    private Menu menU;

    private String title = "",desc = "",cont = "";

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

        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        if( tarefaAtual!=null ){

            titulo.setText( tarefaAtual.getTitulo() );
            descricao.setText( tarefaAtual.getDescricao() );
            conteudo.setText( tarefaAtual.getConteudo() );

            title = tarefaAtual.getTitulo();
            desc = tarefaAtual.getDescricao();
            cont = tarefaAtual.getConteudo();
        }
    }

    @Override
    public void onBackPressed() {

        if(!titulo.getText().toString().equals("") &&
                !conteudo.getText().toString().equals("") ){

            if( titulo.getText().toString().equals(title) &&
                    descricao.getText().toString().equals(desc) &&
                    conteudo.getText().toString().equals(cont)
            ) {

                finish();

            }else {

                if( !titulo.getText().toString().equals(title) ||
                        !descricao.getText().toString().equals(desc) ||
                        !conteudo.getText().toString().equals(cont)
                ){
                    AlertDialog.Builder dialog2 = new AlertDialog.Builder(AddTarefa.this);
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
                            if( title.equals("") && cont.equals("") ){
                                salvar(100L);
                            }else {
                                salvar(tarefaAtual.getStatus());
                            }
                        }
                    });
                    dialog2.create().show();
                }


            }

        }else {
            finish();
        }
    }

    public void salvar(Long stts){

        tituloString = titulo.getText().toString();
        descricaoString = descricao.getText().toString();
        conteudoString = conteudo.getText().toString();

        DaoTarefa dao = new DaoTarefa(getApplicationContext());

        if( tarefaAtual == null ) { // se for para salvar e nao atualizar
            if ( tituloString.isEmpty() || conteudoString.isEmpty() ) {
                Toast.makeText(getApplicationContext(),
                        R.string.insiraCamposObrigatoriosPT, Toast.LENGTH_SHORT).show();
            } else {
                Tarefa tarefa = new Tarefa();
                tarefa.setTitulo(tituloString);
                tarefa.setDescricao(descricaoString);
                tarefa.setConteudo(conteudoString);
                tarefa.setStatus(stts);

                Date date = new Date();
                java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                dateFormat.format(date); // data atual

                Date d = new Date();
                SimpleDateFormat sdf= new SimpleDateFormat("hh:mm a");
                String currentDateTimeString = sdf.format(d); // horario atual

                tarefa.setData( dateFormat.format(date) );
                tarefa.setHorario( currentDateTimeString );

                if (dao.salvar(tarefa)) {
                    finish();
                    Toast.makeText(getApplicationContext(),
                            R.string.tarefaSalvaComSucessoPT, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.erroAoSalvarTarefaPT, Toast.LENGTH_LONG).show();
                }
            }
        }else{ // se for para atualizar

            if ( tarefaAtual.getTitulo().isEmpty() || tarefaAtual.getConteudo().isEmpty() ) {
                Toast.makeText(getApplicationContext(),
                        R.string.insiraCamposObrigatoriosPT, Toast.LENGTH_SHORT).show();
            }else {

                if( titulo.getText().toString().equals("") || conteudo.getText().toString().equals("") ){
                    Toast.makeText(getApplicationContext(),
                            R.string.insiraCamposObrigatoriosPT, Toast.LENGTH_SHORT).show();
                }else {
                    Tarefa tarefa = new Tarefa();
                    tarefa.setTitulo(tituloString);
                    tarefa.setDescricao(descricaoString);
                    tarefa.setConteudo(conteudoString);

                    Date date = new Date();
                    java.text.DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                    dateFormat.format(date); // data atual

                    Date d = new Date();
                    SimpleDateFormat sdf= new SimpleDateFormat("hh:mm a");
                    String currentDateTimeString = sdf.format(d); // horario atual

                    tarefa.setData( dateFormat.format(date) );
                    tarefa.setHorario( currentDateTimeString );
                    tarefa.setStatus(stts);
                    tarefa.setFavorito( tarefaAtual.getFavorito() );

                    tarefa.setId( tarefaAtual.getId() );

                    if (dao.atualizar(tarefa)) {
                        finish();
                        Toast.makeText(getApplicationContext(),
                                R.string.tarefaAtualizadaComSucessoPT, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.erroAtualizarTarefaPT, Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }

    public void favoritarTarefa(){

        if( titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty() ){
            Toast.makeText(getApplicationContext(),
                    R.string.salveAnotacaoParaFavoritarPT,Toast.LENGTH_SHORT).show();
        }else {
            DaoTarefa dao = new DaoTarefa(getApplicationContext());

            if( dao.verificarFav(titulo.getText().toString(),conteudo.getText().toString()) ){
                Tarefa tarefa = new Tarefa();

                if( tarefaAtual.getFavorito().equals(1L) ){
                    tarefa.setFavorito(0L);
                }else if( tarefaAtual.getFavorito().equals(0L) ){
                    tarefa.setFavorito(1L);
                }
                tarefa.setId( tarefaAtual.getId() );

                if( dao.atualizarFav(tarefa) ){
                    if( tarefaAtual.getFavorito().equals(0L) ){
                        Toast.makeText(getApplicationContext(),R.string.favoritadoPT,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),R.string.desfavoritadoPT,Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(getApplicationContext(),R.string.erroAoFav_DesfPT,Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(),
                        R.string.salveTarefaFav_DesfPT,Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        getMenuInflater().inflate( R.menu.menu_add_tarefa, menu );

        this.menU = menu;

        return super.onCreatePanelMenu(featureId, menU);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.itemSalvar:
                if( title.equals("") && cont.equals("") ){
                    salvar(100L);
                }else {
                    salvar( tarefaAtual.getStatus() );
                }
                break;

            case R.id.itemFavoritar:
                favoritarTarefa();
                break;

            case R.id.itemStatusPositivo:

                if( titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(),
                            R.string.salveTarefaAntesDeAlterarPT, Toast.LENGTH_SHORT).show();
                }else {
                    salvar(1L);
                }

                break;

            case R.id.itemStatusNegativo:

                if( titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty() ){
                    Toast.makeText(getApplicationContext(),
                            R.string.salveTarefaAntesDeAlterarPT, Toast.LENGTH_SHORT).show();
                }else {
                    salvar(0L);
                }

                break;

            case R.id.itemSemStatus:

                // salvar sem status
                salvar(100L);

                break;

            case R.id.itemDelete:

                DaoTarefa dao2 = new DaoTarefa(getApplicationContext());
                if(!titulo.getText().toString().equals("") && !conteudo.getText().toString().equals("")){

                    List<Tarefa> lista = new ArrayList<>();
                    lista = dao2.verificarTarefa(titulo.getText().toString(),conteudo.getText().toString());

                    if( !lista.isEmpty() ) {

                        AlertDialog.Builder dialog = new AlertDialog.Builder( AddTarefa.this );
                        dialog.setTitle( getApplicationContext().getResources().getString(R.string.confExclusaoPT) + "\n" );
                        dialog.setMessage( R.string.desejaExcluirAnotacaoPT2);

                        dialog.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                if( dao2.deletar( tarefaAtual ) ){

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

                break;

            case R.id.itemCompartilhar:

                if(titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),
                            R.string.necessarioTerAnotacaoSalvaParaCompartilharPT,
                            Toast.LENGTH_SHORT).show();

                }else {

                    Intent sendIntent = new Intent ();
                    sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    sendIntent.setAction (Intent.ACTION_SEND);
                    if( !descricao.getText().toString().isEmpty() ){
                        sendIntent.putExtra (Intent.EXTRA_TEXT, getApplicationContext().getResources().getString(R.string.compartilhadoAtravesdoNotePT) + "\n\n" +
                                getApplicationContext().getResources().getString(R.string.tituloPT) + titulo.getText().toString() +
                                "\n\n" + getApplicationContext().getResources().getString(R.string.descricaoPT) +  " \n" + descricao.getText().toString() +
                                "\n\n" + getApplicationContext().getResources().getString(R.string.conteudoPT) + " \n" + conteudo.getText().toString() +
                                "\n\n\n" + getApplicationContext().getResources().getString(R.string.baixeAgoraEmPT));
                    }else {
                        sendIntent.putExtra (Intent.EXTRA_TEXT, getApplicationContext().getResources().getString(R.string.compartilhadoAtravesdoNotePT) + "\n\n" +
                                getApplicationContext().getResources().getString(R.string.tituloPT) + titulo.getText().toString() +
                                "\n\n" +getApplicationContext().getResources().getString(R.string.conteudoPT) + " \n" + conteudo.getText().toString() +
                                "\n\n\n" + getApplicationContext().getResources().getString(R.string.baixeAgoraEmPT) );
                    }
                    sendIntent.setType ("text / plain");
                    startActivity (sendIntent);

                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}