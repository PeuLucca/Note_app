package com.example_2_060303.note.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.regex.Pattern;

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

        titulo = findViewById(R.id.txtTitulo);
        descricao = findViewById(R.id.txtDescricao);
        conteudo = findViewById(R.id.txtConteudo);

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
                tarefa.setSenha("");
                tarefa.setDicaSenha("");

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
                    tarefa.setSenha( tarefaAtual.getSenha() );
                    tarefa.setDicaSenha( tarefaAtual.getDicaSenha() );

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

                if( tarefaAtual.getSenha().equals("") ){

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
                            R.string.nPossivelFavoritar,Toast.LENGTH_LONG).show();
                }

            }else {
                Toast.makeText(getApplicationContext(),
                        R.string.salveTarefaFav_DesfPT,Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean verificarSenha(){

        if( titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty() ){
            Toast.makeText(getApplicationContext(),
                    R.string.salvaTarefaAntesDefinirSenha,Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

    public void salvarSenha( String myPassword, String myPasswordHint ){

        DaoTarefa dao = new DaoTarefa( getApplicationContext() );

        Tarefa tarefa = new Tarefa();
        tarefa.setSenha( myPassword );
        tarefa.setDicaSenha( myPasswordHint );
        tarefa.setId( tarefaAtual.getId() );

        if( dao.atualizarSenha( tarefa ) ){

            if( myPassword.equals("")  && myPasswordHint.equals("") ){

                Toast.makeText(getApplicationContext(),
                        R.string.senhaRemovidaComSucesso,Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getApplicationContext(),
                        R.string.senhaSalvaComSucesso,Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(getApplicationContext(),
                        R.string.erroAoSalvarSenha,Toast.LENGTH_LONG).show();
        }


    }

    public void dialogSenha(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(AddTarefa.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.criar_senha, null);
        view.setPadding(5,5,5,5);
        mydialog.setCustomTitle( view );

        View inflatedView = getLayoutInflater().inflate(R.layout.senha_edittext, null);

        TextView txtDica = inflatedView.findViewById(R.id.txtDica);
        txtDica.setVisibility(View.GONE);

        EditText dicaSenha = inflatedView.findViewById(R.id.txtDicaSenha);
        dicaSenha.setVisibility(View.VISIBLE);

        EditText text = inflatedView.findViewById(R.id.textId);
        mydialog.setView( inflatedView );

        mydialog.setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String myPassword, myPasswordHint;
                myPassword = text.getText().toString();
                myPasswordHint = dicaSenha.getText().toString();

                if( myPassword.equals("") || myPassword.isEmpty() || myPassword.length() < 5 || myPasswordHint.equals("")){


                    if( !myPassword.equals("") && myPasswordHint.equals("")  ){
                        Toast.makeText(getApplicationContext(),
                                R.string.insiraDicaSenha, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                R.string.insiraQuantidadeDigitosNecessarios, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    salvarSenha( myPassword, myPasswordHint );
                }
            }
        });

        mydialog.setNegativeButton(R.string.cancelarPT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        mydialog.setCancelable(false);
        mydialog.show();
    }

    public void dialogTrocarSenha(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(AddTarefa.this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.criar_senha, null);
        view.setPadding(5,5,5,5);
        mydialog.setCustomTitle( view );

        View inflatedView = getLayoutInflater().inflate(R.layout.senha_edittext, null);

        TextView txtDica = inflatedView.findViewById(R.id.txtDica);
        txtDica.setVisibility(View.GONE);

        EditText dicaSenha = inflatedView.findViewById(R.id.txtDicaSenha);
        dicaSenha.setVisibility(View.VISIBLE);

        EditText text = inflatedView.findViewById(R.id.textId);
        mydialog.setView( inflatedView );

        mydialog.setPositiveButton(R.string.salvar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String myPassword, myPasswordHint;
                myPassword = text.getText().toString();
                myPasswordHint = dicaSenha.getText().toString();

                if( myPassword.equals("") || myPassword.isEmpty() || myPassword.length() < 5 || myPasswordHint.equals("")){

                    if( myPasswordHint.equals("") ){
                        Toast.makeText(getApplicationContext(),
                                R.string.insiraQuantidadeDigitosNecessariosESenha, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getApplicationContext(),
                                R.string.insiraQuantidadeDigitosNecessarios, Toast.LENGTH_SHORT).show();
                    }

                }else {
                    salvarSenha( myPassword, myPasswordHint );
                }
            }
        });

        mydialog.setNegativeButton(R.string.cancelarPT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        mydialog.setCancelable(false);
        mydialog.show();

    }

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i)
            {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                {
                    return "";
                }
            }

            return null;
        }
    };

    public void compartilhar(){

        List<Tarefa> lista;
        DaoTarefa daoTarefa = new DaoTarefa( getApplicationContext() );
        lista = daoTarefa.verificarTarefa( title, cont );

        if( !lista.isEmpty() ){

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
        }else {
            Toast.makeText(getApplicationContext(),
                    R.string.necessarioTerAnotacaoSalvaParaCompartilharPT,
                    Toast.LENGTH_SHORT).show();
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

            case R.id.itemSenha:
                if( verificarSenha() ){ // se pode adicionar uma senha

                    DaoTarefa daoTarefa = new DaoTarefa( getApplicationContext() );

                    String lista="";

                    if( tarefaAtual == null ){ // se nao tem nada salvo

                        Toast.makeText(getApplicationContext(),
                                R.string.salvaTarefaAntesDefinirSenha, Toast.LENGTH_SHORT).show();

                    }else { // se tem algo salvo
                        lista = daoTarefa.verificarSenha( title,cont );

                        if( lista.equals("") ){ // se nao existir senha salva
                            dialogSenha();
                        }else { // se ja existir senha salva

                            // trocar senha
                            AlertDialog.Builder dialog = new AlertDialog.Builder( AddTarefa.this );
                            dialog.setTitle( R.string.atencao );
                            dialog.setMessage( R.string.desejaRemoverSenhaOuTrocarSenha);

                            dialog.setPositiveButton(R.string.trocarDeSenha, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogTrocarSenha();
                                }
                            });

                            dialog.setNegativeButton(R.string.removerSenha, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    salvarSenha("","");

                                }
                            });
                            dialog.setNeutralButton(R.string.cancelarPT,null);

                            dialog.create().show();

                        }

                    }

                }
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

                    if( tarefaAtual == null ){ // entao nao tem nada salvo

                        lista = dao2.verificarTarefa( titulo.getText().toString(),conteudo.getText().toString() );

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

                    }else {

                        lista = dao2.verificarTarefa( title, cont );

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

                }
                else { // se a tarefa nao existir
                    Toast.makeText(getApplicationContext(),
                            R.string.paraDeletarPrecisaSalvarPT,
                            Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.itemCompartilhar:

                if( titulo.getText().toString().isEmpty() || conteudo.getText().toString().isEmpty()){

                    Toast.makeText(getApplicationContext(),
                            R.string.necessarioTerAnotacaoSalvaParaCompartilharPT,
                            Toast.LENGTH_SHORT).show();

                }else {

                    List<Tarefa> lista;
                    DaoTarefa daoTarefa = new DaoTarefa( getApplicationContext() );
                    lista = daoTarefa.verificarTarefa( title, cont );

                    if( !lista.isEmpty() ){

                        if( tarefaAtual.getSenha().equals("") ){
                            compartilhar();
                        }
                        else {

                            AlertDialog.Builder dialog = new AlertDialog.Builder( AddTarefa.this );
                            dialog.setTitle( R.string.atencaoPT );
                            dialog.setMessage( R.string.desejaCompartilharTarefaSalvaComSenha );

                            dialog.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    compartilhar();
                                }
                            });

                            dialog.setNegativeButton(R.string.naoPT, null);

                            dialog.create().show();

                        }
                    } else {
                        Toast.makeText(getApplicationContext(),
                                R.string.necessarioTerAnotacaoSalvaParaCompartilharPT,
                                Toast.LENGTH_SHORT).show();
                    }

                }

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}