package com.danielfortes.globalsolution_carcontrol

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VeiculosAdapter(var listaVeiculos: List<TBVeiculos>, var listener: VeiculosAdapterListener): RecyclerView.Adapter<VeiculosAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtPlaca: TextView = view.findViewById(R.id.edtPlaca)
        val txtModelo: TextView = view.findViewById(R.id.edtModelo)
        val txtMarca: TextView = view.findViewById(R.id.edtMarca)
        val txtAno: TextView = view.findViewById(R.id.edtAno)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluirVeiculo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_veiculo, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        holder.txtPlaca.text = listaVeiculos[position].placa
        holder.txtModelo.text = listaVeiculos[position].modelo
        holder.txtMarca.text = listaVeiculos[position].marca
        holder.txtAno.text = listaVeiculos[position].ano

        holder.btnExcluir.setOnClickListener(){
            listener.excluirVeiculo(listaVeiculos[position])
        }
    }

    override fun getItemCount(): Int {
        return listaVeiculos.size
    }

    fun refreshListVeiculos(veiculosAtualizado: List<TBVeiculos>){
        listaVeiculos = veiculosAtualizado
        notifyDataSetChanged()
    }
}