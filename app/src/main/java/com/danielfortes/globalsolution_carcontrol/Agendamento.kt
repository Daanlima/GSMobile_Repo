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

class Agendamento : Fragment(), AgendamentoAdapterListener {

    lateinit var edtNome: EditText
    lateinit var edtData: EditText
    lateinit var edtCPF: EditText
    lateinit var btnIncluirAgendamento: ImageButton
    lateinit var rvagendamento: RecyclerView
    lateinit var preferenciasApp: SharedPreferences
    lateinit var agendamentoAdapter: AgendamentoAdapter

    var db: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agendamento, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvagendamento = view.findViewById(R.id.rvAgendamento)
        btnIncluirAgendamento = view.findViewById(R.id.btnIncluirAgendamento)
        edtNome = view.findViewById(R.id.edtNome)
        edtData = view.findViewById(R.id.edtData)
        edtCPF = view.findViewById(R.id.edtCPF)

        preferenciasApp = requireActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        btnIncluirAgendamento.setOnClickListener() {
            if (edtNome.text.toString().isNotEmpty() && edtData.text.toString().isNotEmpty() && edtCPF.text.toString().isNotEmpty()){
                adicionarAgendamento(edtNome.text.toString(), edtData.text.toString(), edtCPF.text.toString())
                edtNome.text.clear()
                edtData.text.clear()
                edtCPF.text.clear()

                val editor = preferenciasApp.edit()
                editor.remove("NOME")
                editor.remove("DATA")
                editor.remove("CPF")
                editor.apply()
            } else {
                edtNome.error = "Insira um texto válido!"
                edtData.error = "Insira um texto válido!"
                edtCPF.error = "Insira um texto válido!"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarAgendamento()
    }

    override fun onPause() {
        super.onPause()
        if (edtNome.text.toString().isNotEmpty() && edtData.text.toString().isNotEmpty() && edtCPF.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("NOME", edtNome.text.toString())
            editor.putString("DATA", edtData.text.toString())
            editor.putString("CPF", edtCPF.text.toString())
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        edtNome.setText(preferenciasApp.getString("NOME", null))
        edtData.setText(preferenciasApp.getString("DATA", null))
        edtCPF.setText(preferenciasApp.getString("CPF", null))
    }

    fun adicionarAgendamento(nomeAgendamento: String, dataAgendamento: String, cpfAgendamento: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.AgendamentoDAO()?.addAgendamento(TBAgendamento(nome = nomeAgendamento, data = dataAgendamento, cpf = cpfAgendamento))

            val agendamentoDAO = db?.AgendamentoDAO()

            val resposta = agendamentoDAO?.getAgendamentos()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    agendamentoAdapter.refreshListAgendamento(resposta)
                }
            }
        }
    }

    fun recuperarAgendamento(){
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            val agendamentoDAO = db?.AgendamentoDAO()

            val resposta = agendamentoDAO?.getAgendamentos()

            withContext(Dispatchers.Main){
                resposta?.let{
                    agendamentoAdapter = AgendamentoAdapter(it, this@Agendamento)

                    rvagendamento.adapter = agendamentoAdapter

                    rvagendamento.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    override fun excluirAgendamento(agendamento: TBAgendamento) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.AgendamentoDAO()?.deleteAgendamento(agendamento)

            val agendamentoDAO = db?.AgendamentoDAO()

            val resposta = agendamentoDAO?.getAgendamentos()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    agendamentoAdapter.refreshListAgendamento(resposta)

                    Toast.makeText(requireContext(), "Agendamento excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
