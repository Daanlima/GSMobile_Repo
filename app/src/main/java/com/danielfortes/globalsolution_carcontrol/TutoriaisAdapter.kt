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

class TutoriaisAdapter(var listaTutoriais: List<TBTutoriais>, var listener: TutoriaisAdapterListener): RecyclerView.Adapter<TutoriaisAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtTitulo: TextView = view.findViewById(R.id.edtTitulo)
        val txtCorpo: TextView = view.findViewById(R.id.edtCorpo)
        val txtLink: TextView = view.findViewById(R.id.edtLink)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluirTutorial)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tutorial, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.txtTitulo.text = listaTutoriais[position].titulo
        holder.txtCorpo.text = listaTutoriais[position].corpotutorial
        holder.txtLink.text = listaTutoriais[position].link

        holder.btnExcluir.setOnClickListener(){
            listener.excluirTutorial(listaTutoriais[position])
        }
    }

    override fun getItemCount(): Int {
        return listaTutoriais.size
    }

    fun refreshListTutorial(tutorialAtualizado: List<TBTutoriais>){
        listaTutoriais = tutorialAtualizado
        notifyDataSetChanged()
    }
}