package com.example_2_060303.note.activity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

//import com.example.note.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.adapter.AdapterTarefa;
import com.example_2_060303.note.adapter.AdapterTarefaRapida;
import com.example_2_060303.note.adapter.RecycleViewItemClickListener;
import com.example_2_060303.note.databinding.ActivityMainBinding;
import com.example_2_060303.note.helper.DaoTarefa;
import com.example_2_060303.note.helper.DaoTarefaRapida;
import com.example_2_060303.note.helper.SharedPref;
import com.example_2_060303.note.model.Alarm;
import com.example_2_060303.note.model.RateUsDialog;
import com.example_2_060303.note.model.Tarefa;
import com.example_2_060303.note.model.Tarefa_Rapida;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity{

    private List<Tarefa> tarefaList = new ArrayList<>();
    private List<Tarefa_Rapida> tarefaRapidaList = new ArrayList<>();
    private RecyclerView recyclerViewTarefa,recyclerViewTarefaRapida;
    private Tarefa tarefaSelected;
    private Tarefa_Rapida tarefaRapidaSelected;
    private int itemSelected;
    private FloatingActionMenu btnaddTarefaGeral;
    private TextView txtTarefa, txtTarefaRapida;
    private ImageView imgDivisor;
    private SharedPref sharedPref;
    private String avaliacaoStatus = "";
    private ImageView imgLogoPrincipal;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        recyclerViewTarefa = findViewById(R.id.recyclerView);
        recyclerViewTarefaRapida = findViewById(R.id.recyclerViewTarefaRapida);
        btnaddTarefaGeral = findViewById(R.id.addTarefaGeral);
        txtTarefa = findViewById(R.id.txtTarefaNormal);
        txtTarefaRapida = findViewById(R.id.txtTarefaRapida);
        imgDivisor = findViewById(R.id.imgDivisor);
        imgLogoPrincipal = findViewById(R.id.imgLogoPrincipal);

        setSupportActionBar(binding.toolbar);

        animateImage( imgLogoPrincipal );

        recyclerViewTarefaRapida.addOnItemTouchListener(new RecycleViewItemClickListener(getApplicationContext(), recyclerViewTarefaRapida, new RecycleViewItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) { // para editar

                Tarefa_Rapida tarefaSelecionada = tarefaRapidaList.get( position );
                Intent intent = new Intent( MainActivity.this, AddTarefaRapida.class );
                intent.putExtra( "tarefaSelecionada" , tarefaSelecionada );
                startActivity( intent );

            }

            @Override
            public void onLongItemClick(View view, int position) { // para deletar

                tarefaRapidaSelected = tarefaRapidaList.get( position );

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle( getApplicationContext().getResources().getString(R.string.confExclusaoPT) + "\n" );
                dialog.setMessage( getApplicationContext().getResources().getString(R.string.desejaExcluirAnotacaoPT2));

                dialog.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DaoTarefaRapida daoTarefaRapida = new DaoTarefaRapida( getApplicationContext() );
                        if( daoTarefaRapida.deletar( tarefaRapidaSelected ) ){

                            carregarLista();
                        }
                    }
                });

                dialog.setNegativeButton(R.string.naoPT, null);

                dialog.create().show();

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));


        recyclerViewTarefa.addOnItemTouchListener(new RecycleViewItemClickListener(getApplicationContext(), recyclerViewTarefa, new RecycleViewItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) { // para editar

                Tarefa tarefaSelecionada = tarefaList.get( position );
                Intent intent = new Intent( MainActivity.this, AddTarefa.class );
                intent.putExtra( "tarefaSelecionada" , tarefaSelecionada );
                startActivity( intent );
            }

            @Override
            public void onLongItemClick(View view, int position) { // para deletar

                tarefaSelected = tarefaList.get( position );

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle( getApplicationContext().getResources().getString(R.string.confExclusaoPT) + "\n" );
                dialog.setMessage( getApplicationContext().getResources().getString(R.string.desejaExcluirAnotacaoPT) + " " + tarefaSelected.getTitulo() + "?" );

                dialog.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DaoTarefa daoDeletar = new DaoTarefa( getApplicationContext() );
                        if( daoDeletar.deletar( tarefaSelected ) ){

                            if( itemSelected == 0 ){
                                carregarLista();
                            }else if( itemSelected == 1 ){

                                DaoTarefa dao = new DaoTarefa(getApplicationContext());
                                tarefaList = dao.listarTitulo();

                                AdapterTarefa adapter = new AdapterTarefa( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerViewTarefa.setLayoutManager( layoutManager );
                                recyclerViewTarefa.setHasFixedSize(true);
                                recyclerViewTarefa.setAdapter( adapter );

                            } else if( itemSelected == 2 ){

                                DaoTarefa dao = new DaoTarefa(getApplicationContext());
                                tarefaList = dao.listar( "data" ); // carregando lista baseado no status (status,titulo,horario)

                                AdapterTarefa adapter = new AdapterTarefa( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerViewTarefa.setLayoutManager( layoutManager );
                                recyclerViewTarefa.setHasFixedSize(true);
                                recyclerViewTarefa.setAdapter( adapter );
                            } else if( itemSelected == 3 ){

                                DaoTarefa dao = new DaoTarefa(getApplicationContext());
                                tarefaList = dao.listarSttsConcluido();

                                AdapterTarefa adapter = new AdapterTarefa( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerViewTarefa.setLayoutManager( layoutManager );
                                recyclerViewTarefa.setHasFixedSize(true);
                                recyclerViewTarefa.setAdapter( adapter );
                            } else if( itemSelected == 4 ){

                                DaoTarefa dao = new DaoTarefa(getApplicationContext());
                                tarefaList = dao.listarSttsNaoConcluido();

                                AdapterTarefa adapter = new AdapterTarefa( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerViewTarefa.setLayoutManager( layoutManager );
                                recyclerViewTarefa.setHasFixedSize(true);
                                recyclerViewTarefa.setAdapter( adapter );
                            } else if( itemSelected == 5 ){

                                DaoTarefa dao = new DaoTarefa(getApplicationContext());
                                tarefaList = dao.listarSemStts();

                                AdapterTarefa adapter = new AdapterTarefa( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerViewTarefa.setLayoutManager( layoutManager );
                                recyclerViewTarefa.setHasFixedSize(true);
                                recyclerViewTarefa.setAdapter( adapter );
                            }else if( itemSelected == 6 ){

                                DaoTarefa dao = new DaoTarefa(getApplicationContext());
                                tarefaList = dao.listarFav();

                                AdapterTarefa adapter = new AdapterTarefa( tarefaList,getApplicationContext() );

                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
                                recyclerViewTarefa.setLayoutManager( layoutManager );
                                recyclerViewTarefa.setHasFixedSize(true);
                                recyclerViewTarefa.setAdapter( adapter );
                            }

                        }else{
                            Toast.makeText(getApplicationContext(),
                                    R.string.erroAoExcluirTarefaPT,Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.setNegativeButton(R.string.naoPT, null);

                dialog.create().show();
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));
    }

    public void animateImage( ImageView ratingImage ){

        ScaleAnimation scaleAnimation = new ScaleAnimation( 0, 1f, 0 ,1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(500);
        ratingImage.startAnimation( scaleAnimation );

    }

    public void addTarefa(View view){
        startActivity( new Intent( getApplicationContext(), AddTarefa.class ) );
    }

    public void addTarefaRapida(View view){
        startActivity( new Intent( getApplicationContext(), AddTarefaRapida.class ) );
    }

    @Override
    protected void onStart() {
        carregarLista();

        String statusAvaliacao = "";
        sharedPref = new SharedPref( getApplicationContext() );
        statusAvaliacao = sharedPref.verificarAvaliacao();
        avaliacaoStatus = statusAvaliacao;

        if( avaliacaoStatus.equals("false") ){

            if( tarefaList.size() > 3 || tarefaRapidaList.size() > 3 ){
                avaliar();
            }
        }else if( avaliacaoStatus.equals("maisTarde7") ){

            if( tarefaList.size() > 7 || tarefaRapidaList.size() > 7 ){
                avaliar();
            }
        }else if( avaliacaoStatus.equals("maisTarde10") ){

            if( tarefaList.size() > 10 || tarefaRapidaList.size() > 10 ){
                avaliar();
            }
        }else if( avaliacaoStatus.equals("maisTarde15") ){

            if( tarefaList.size() > 15 || tarefaRapidaList.size() > 15 ){
                avaliar();
            }
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        carregarLista();
    }

    @Override
    protected void onPause() {
        super.onPause();
        carregarLista();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        int time = 129600; // 15 dias = 1.296.000 e 9 dias = 777.600 e 7 dias = 604.800 e 1,5 dia = 129.600
        Intent i = new Intent(MainActivity.this, Alarm.class);

        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),0,i,0);

        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+time*1000,pi);
    }

    public void atualizarTextosInformativos(){

        if( tarefaRapidaList.isEmpty() ){
            txtTarefaRapida.setVisibility(View.VISIBLE);
            imgDivisor.setVisibility( View.GONE );
        }else {
            txtTarefaRapida.setVisibility(View.GONE);
            imgDivisor.setVisibility( View.VISIBLE );
        }

        if( tarefaList.isEmpty() ){
            txtTarefa.setVisibility(View.VISIBLE);
        }else {
            txtTarefa.setVisibility(View.GONE);
        }

    }

    @SuppressLint("ResourceAsColor")
    public void carregarLista(){
        // listar tarefas rapidas salvas
        DaoTarefaRapida daoTarefaRapida = new DaoTarefaRapida( getApplicationContext() );
        tarefaRapidaList = daoTarefaRapida.listar();

        AdapterTarefaRapida adapterTarefaRapida = new AdapterTarefaRapida( tarefaRapidaList );

        RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        recyclerViewTarefaRapida.setLayoutManager( layoutManagerHorizontal );
        recyclerViewTarefaRapida.setHasFixedSize( true );
        recyclerViewTarefaRapida.setAdapter( adapterTarefaRapida );

        // listar as tarefas salvas
        DaoTarefa dao = new DaoTarefa(getApplicationContext());
        tarefaList = dao.listar( "status" );

        AdapterTarefa adapter = new AdapterTarefa( tarefaList, getApplicationContext() );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerViewTarefa.setLayoutManager( layoutManager );
        recyclerViewTarefa.setHasFixedSize(true);
        recyclerViewTarefa.setAdapter( adapter );


        if(tarefaList.size() >= 4){
            btnaddTarefaGeral.setAlpha(0.60f);
        }else {
            btnaddTarefaGeral.setAlpha(1f);
        }

        atualizarTextosInformativos();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){


            case R.id.pesquisar_por_nome:
                List<Tarefa> tarefa = tarefaList;
                Intent intent = new Intent( MainActivity.this, PesquisarTarefa.class );
                intent.putExtra( "tarefa" , (Serializable) tarefa);
                startActivity( intent );

                break;

            case R.id.itemOrdenarPadrao: // 0

                itemSelected = 0;
                carregarLista();

                break;

            case R.id.itemOrdenarA_Z: // 1

                itemSelected = 1;

                // listar as tarefas salvas
                DaoTarefa dao1 = new DaoTarefa(getApplicationContext());
                tarefaList = dao1.listarTitulo();

                AdapterTarefa adapter1 = new AdapterTarefa( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager( getApplicationContext() );
                recyclerViewTarefa.setLayoutManager( layoutManager1 );
                recyclerViewTarefa.setHasFixedSize(true);
                recyclerViewTarefa.setAdapter( adapter1 );

                break;

            case R.id.itemOrdenarData: // 2

                itemSelected = 2;

                // listar as tarefas salvas
                DaoTarefa dao2 = new DaoTarefa(getApplicationContext());
                tarefaList = dao2.listar( "data" ); // carregando lista baseado no status (status,titulo,horario)

                AdapterTarefa adapter2 = new AdapterTarefa( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager( getApplicationContext() );
                recyclerViewTarefa.setLayoutManager( layoutManager2 );
                recyclerViewTarefa.setHasFixedSize(true);
                recyclerViewTarefa.setAdapter( adapter2 );

                break;


            case R.id.itemOrdenarStatusConcluido: // 3

                imgDivisor.setVisibility( View.GONE );

                List<Tarefa_Rapida> l = new ArrayList<>();
                tarefaRapidaList = l;
                AdapterTarefaRapida adapterTarefaRapida = new AdapterTarefaRapida( tarefaRapidaList );

                RecyclerView.LayoutManager layoutManagerHorizontal = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );
                recyclerViewTarefaRapida.setLayoutManager( layoutManagerHorizontal );
                recyclerViewTarefaRapida.setHasFixedSize( true );
                recyclerViewTarefaRapida.setAdapter( adapterTarefaRapida );

                itemSelected = 3;

                DaoTarefa dao3 = new DaoTarefa(getApplicationContext());
                tarefaList = dao3.listarSttsConcluido();

                AdapterTarefa adapter3 = new AdapterTarefa( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager( getApplicationContext() );
                recyclerViewTarefa.setLayoutManager( layoutManager3 );
                recyclerViewTarefa.setHasFixedSize(true);
                recyclerViewTarefa.setAdapter( adapter3 );
                break;

            case R.id.itemOrdenarStatusNaoConcluido: // 4

                imgDivisor.setVisibility( View.GONE );

                List<Tarefa_Rapida> l2 = new ArrayList<>();
                tarefaRapidaList = l2;
                AdapterTarefaRapida adapterTarefaRapida2 = new AdapterTarefaRapida( tarefaRapidaList );

                RecyclerView.LayoutManager layoutManagerHorizontal2 = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );
                recyclerViewTarefaRapida.setLayoutManager( layoutManagerHorizontal2 );
                recyclerViewTarefaRapida.setHasFixedSize( true );
                recyclerViewTarefaRapida.setAdapter( adapterTarefaRapida2 );

                itemSelected = 4;

                DaoTarefa dao4 = new DaoTarefa(getApplicationContext());
                tarefaList = dao4.listarSttsNaoConcluido();

                AdapterTarefa adapter4 = new AdapterTarefa( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager( getApplicationContext() );
                recyclerViewTarefa.setLayoutManager( layoutManager4 );
                recyclerViewTarefa.setHasFixedSize(true);
                recyclerViewTarefa.setAdapter( adapter4 );
                break;

            case R.id.itemOrdenarSemStatus: // 5

                imgDivisor.setVisibility( View.GONE );

                List<Tarefa_Rapida> l3 = new ArrayList<>();
                tarefaRapidaList = l3;
                AdapterTarefaRapida adapterTarefaRapida3 = new AdapterTarefaRapida( tarefaRapidaList );

                RecyclerView.LayoutManager layoutManagerHorizontal3 = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );
                recyclerViewTarefaRapida.setLayoutManager( layoutManagerHorizontal3 );
                recyclerViewTarefaRapida.setHasFixedSize( true );
                recyclerViewTarefaRapida.setAdapter( adapterTarefaRapida3 );

                itemSelected = 5;

                DaoTarefa dao5 = new DaoTarefa(getApplicationContext());
                tarefaList = dao5.listarSemStts();

                AdapterTarefa adapter5 = new AdapterTarefa( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager( getApplicationContext() );
                recyclerViewTarefa.setLayoutManager( layoutManager5 );
                recyclerViewTarefa.setHasFixedSize(true);
                recyclerViewTarefa.setAdapter( adapter5 );
                break;

            case R.id.itemExibirFav:

                List<Tarefa_Rapida> l4 = new ArrayList<>();
                tarefaRapidaList = l4;
                AdapterTarefaRapida adapterTarefaRapida4 = new AdapterTarefaRapida( tarefaRapidaList );

                RecyclerView.LayoutManager layoutManagerHorizontal4 = new LinearLayoutManager(
                        getApplicationContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );
                recyclerViewTarefaRapida.setLayoutManager( layoutManagerHorizontal4 );
                recyclerViewTarefaRapida.setHasFixedSize( true );
                recyclerViewTarefaRapida.setAdapter( adapterTarefaRapida4 );

                itemSelected = 6;

                DaoTarefa dao6 = new DaoTarefa(getApplicationContext());
                tarefaList = dao6.listarFav();

                AdapterTarefa adapter6 = new AdapterTarefa( tarefaList,getApplicationContext() );

                RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager( getApplicationContext() );
                recyclerViewTarefa.setLayoutManager( layoutManager6 );
                recyclerViewTarefa.setHasFixedSize(true);
                recyclerViewTarefa.setAdapter( adapter6 );

                break;



            case R.id.avaliar:

                avaliar();

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void avaliar(){

        RateUsDialog rateUsDialog = new RateUsDialog(MainActivity.this );
        rateUsDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        rateUsDialog.setCancelable( false );
        rateUsDialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();*/
        return true;
    }
}