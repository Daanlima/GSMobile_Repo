package com.danielfortes.globalsolution_carcontrol

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import java.net.URI

class AgendamentoAdapter(var listaAgendamento: List<TBAgendamento>, var listener: AgendamentoAdapterListener): RecyclerView.Adapter<AgendamentoAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtNome: TextView = view.findViewById(R.id.edtNome)
        val txtData: TextView = view.findViewById(R.id.edtData)
        val txtCPF: TextView = view.findViewById(R.id.edtCPF)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluirAgendamento)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_agendamento, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.txtNome.text = listaAgendamento[position].nome
        holder.txtData.text = listaAgendamento[position].data
        holder.txtCPF.text = listaAgendamento[position].cpf

        holder.btnExcluir.setOnClickListener(){
            listener.excluirAgendamento(listaAgendamento[position])
        }
    }

    override fun getItemCount(): Int {
        return listaAgendamento.size
    }

    fun refreshListAgendamento(agendamentoAtualizado: List<TBAgendamento>){
        listaAgendamento = agendamentoAtualizado
        notifyDataSetChanged()
    }
}