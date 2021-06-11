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

class LavaRapidoAdapter(var listaLavaRapido: List<TBLavaRapido>, var listener: LavaRapidoAdapterListener): RecyclerView.Adapter<LavaRapidoAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtNomeLava: TextView = view.findViewById(R.id.edtNomeLava)
        val txtCEP: TextView = view.findViewById(R.id.edtCEP)
        val txtValor: TextView = view.findViewById(R.id.edtValor)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluirLavaRapido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_lavarapido, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.txtNomeLava.text = listaLavaRapido[position].nome
        holder.txtCEP.text = listaLavaRapido[position].cep
        holder.txtValor.text = listaLavaRapido[position].valor

        holder.btnExcluir.setOnClickListener(){
            listener.excluirLavaRapido(listaLavaRapido[position])
        }
    }

    override fun getItemCount(): Int {
        return listaLavaRapido.size
    }

    fun refreshListAgendamento(lavarapidoAtualizado: List<TBLavaRapido>){
        listaLavaRapido = lavarapidoAtualizado
        notifyDataSetChanged()
    }
}