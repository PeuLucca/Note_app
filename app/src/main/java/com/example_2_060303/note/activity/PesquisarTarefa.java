package com.example_2_060303.note.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example_2_060303.note.R;
import com.example_2_060303.note.adapter.AdapterTarefa;
import com.example_2_060303.note.adapter.RecycleViewItemClickListener;
import com.example_2_060303.note.helper.DaoTarefa;
import com.example_2_060303.note.model.Tarefa;

import java.util.ArrayList;
import java.util.List;

public class PesquisarTarefa extends AppCompatActivity {

    private List<Tarefa> lista = new ArrayList<>();
    private AdapterTarefa adap;
    private RecyclerView recyclerView;
    private Tarefa tarefaSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesquisar_tarefa);
        recyclerView = findViewById(R.id.recycler_Pesquisar_Usuario);

        lista = (List<Tarefa>) getIntent().getSerializableExtra("tarefa");

        adap = new AdapterTarefa(lista,getApplicationContext());

        carregarLista();

        recyclerView.addOnItemTouchListener(new RecycleViewItemClickListener(getApplicationContext(), recyclerView, new RecycleViewItemClickListener.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }

            @Override
            public void onItemClick(View view, int position) {

                Tarefa tarefaSelecionada = lista.get( position );
                Intent intent = new Intent( PesquisarTarefa.this, AddTarefa.class );
                intent.putExtra( "tarefaSelecionada" , tarefaSelecionada );
                startActivity( intent );
            }

            @Override
            public void onLongItemClick(View view, int position) {
                tarefaSelected = lista.get( position );

                AlertDialog.Builder dialog = new AlertDialog.Builder(PesquisarTarefa.this);
                dialog.setTitle( getApplicationContext().getResources().getString(R.string.confExclusaoPT) +  "\n" );
                dialog.setMessage( getApplicationContext().getResources().getString(R.string.desejaExcluirAnotacaoPT) + tarefaSelected.getTitulo() + "?" );
                dialog.setPositiveButton(R.string.simPT, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DaoTarefa daoDeletar = new DaoTarefa( getApplicationContext() );
                        if( daoDeletar.deletar( tarefaSelected ) ){
                            // tarefa deletada
                            carregarLista();
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    R.string.erroAoExcluirTarefaPT,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog.setNegativeButton(R.string.naoPT, null);
                dialog.create().show();
            }
        }
        ));

    }

    @Override
    protected void onStart() {
        super.onStart();
        carregarLista();
    }

    @Override
    protected void onStop() {
        super.onStop();
        carregarLista();
    }

    public void carregarLista(){

        // listar as tarefas salvas
        DaoTarefa dao = new DaoTarefa(getApplicationContext());
        lista = dao.listar( "status" );

        adap = new AdapterTarefa(lista,getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getApplicationContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter( adap );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adap.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}