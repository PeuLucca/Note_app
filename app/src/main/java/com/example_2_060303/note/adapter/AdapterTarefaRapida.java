package com.example_2_060303.note.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example_2_060303.note.R;
import com.example_2_060303.note.model.Tarefa_Rapida;

import java.util.List;

public class AdapterTarefaRapida extends RecyclerView.Adapter<AdapterTarefaRapida.MyViewHolder> {

    private List<Tarefa_Rapida> listTarefa_Rapida;

    public AdapterTarefaRapida( List<Tarefa_Rapida> list ) {
        this.listTarefa_Rapida = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_adapter_tarefa_rapida ,parent,false);

        return new AdapterTarefaRapida.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tarefa_Rapida tarefa_rapida = listTarefa_Rapida.get( position );
        holder.conteudo.setText( tarefa_rapida.getConteudo() );

    }

    @Override
    public int getItemCount() {
        return listTarefa_Rapida.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView conteudo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            conteudo = itemView.findViewById(R.id.txtConteudoTarefaRapida);

        }
    }

}
