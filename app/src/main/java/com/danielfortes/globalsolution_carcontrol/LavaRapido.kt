package com.danielfortes.globalsolution_carcontrol

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LavaRapido : Fragment(), LavaRapidoAdapterListener {

    lateinit var edtNomeLava: EditText
    lateinit var edtValor: EditText
    lateinit var edtCEP: EditText
    lateinit var btnIncluirLavaRapido: ImageButton
    lateinit var rvlavarapido: RecyclerView
    lateinit var lavaRapidoAdapter: LavaRapidoAdapter
    lateinit var preferenciasApp: SharedPreferences

    var db: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lavarapido, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvlavarapido = view.findViewById(R.id.rvLavaRapido)
        btnIncluirLavaRapido = view.findViewById(R.id.btnIncluirLavaRapido)
        edtNomeLava = view.findViewById(R.id.edtNomeLava)
        edtValor = view.findViewById(R.id.edtValor)
        edtCEP = view.findViewById(R.id.edtCEP)

        preferenciasApp = requireActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        btnIncluirLavaRapido.setOnClickListener() {
            if (edtNomeLava.text.toString().isNotEmpty() && edtCEP.text.toString().isNotEmpty() && edtValor.text.toString().isNotEmpty()){
                adicionarLavaRapido(edtNomeLava.text.toString(), edtCEP.text.toString(), edtValor.text.toString())
                edtNomeLava.text.clear()
                edtCEP.text.clear()
                edtValor.text.clear()

                val editor = preferenciasApp.edit()
                editor.remove("NOME")
                editor.remove("DATA")
                editor.remove("CPF")
                editor.apply()
            } else {
                edtNomeLava.error = "Insira um texto válido!"
                edtCEP.error = "Insira um texto válido!"
                edtValor.error = "Insira um texto válido!"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarLavaRapido()
    }

    override fun onPause() {
        super.onPause()
        if (edtNomeLava.text.toString().isNotEmpty() && edtValor.text.toString().isNotEmpty() && edtCEP.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("NOME", edtNomeLava.text.toString())
            editor.putString("VALOR", edtValor.text.toString())
            editor.putString("CEP", edtCEP.text.toString())

            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        edtNomeLava.setText(preferenciasApp.getString("NOME", null))
        edtValor.setText(preferenciasApp.getString("VALOR", null))
        edtCEP.setText(preferenciasApp.getString("CEP", null))
    }

    fun adicionarLavaRapido(nomeLavaRapido: String, cepLavaRapido: String, valorLavaRapido: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.LavaRapidoDAO()?.addLavaRapido(TBLavaRapido(nome = nomeLavaRapido, cep = cepLavaRapido, valor = valorLavaRapido))

            val lavarapidoDAO = db?.LavaRapidoDAO()

            val resposta = lavarapidoDAO?.getLavaRapido()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    lavaRapidoAdapter.refreshListAgendamento(resposta)
                }
            }
        }
    }

    fun recuperarLavaRapido(){
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            val lavarapidoDAO = db?.LavaRapidoDAO()

            val resposta = lavarapidoDAO?.getLavaRapido()

            withContext(Dispatchers.Main){
                resposta?.let{
                    lavaRapidoAdapter = LavaRapidoAdapter(it, this@LavaRapido)

                    rvlavarapido.adapter = lavaRapidoAdapter

                    rvlavarapido.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    override fun excluirLavaRapido(lavaRapido: TBLavaRapido) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.LavaRapidoDAO()?.deleteLavaRapido(lavaRapido)

            val lavarapidoDAO = db?.LavaRapidoDAO()

            val resposta = lavarapidoDAO?.getLavaRapido()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    lavaRapidoAdapter.refreshListAgendamento(resposta)

                    Toast.makeText(requireContext(), "Lava Rapido excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
