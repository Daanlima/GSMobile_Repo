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

class Veiculos : Fragment(), VeiculosAdapterListener {

    lateinit var edtPlaca: EditText
    lateinit var edtModelo: EditText
    lateinit var edtMarca: EditText
    lateinit var edtAno: EditText
    lateinit var btnIncluirVeiculos: ImageButton
    lateinit var rvveiculo: RecyclerView
    lateinit var veiculosAdapter: VeiculosAdapter
    lateinit var preferenciasApp: SharedPreferences

    var db: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_veiculos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvveiculo = view.findViewById(R.id.rvVeiculo)
        btnIncluirVeiculos = view.findViewById(R.id.btnIncluirVeiculos)
        edtPlaca = view.findViewById(R.id.edtPlaca)
        edtModelo = view.findViewById(R.id.edtModelo)
        edtMarca = view.findViewById(R.id.edtMarca)
        edtAno = view.findViewById(R.id.edtAno)

        preferenciasApp = requireActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        btnIncluirVeiculos.setOnClickListener() {
            if (edtPlaca.text.toString().isNotEmpty() && edtMarca.text.toString().isNotEmpty() && edtModelo.text.toString().isNotEmpty() && edtAno.text.toString().isNotEmpty()){
                adicionarVeiculo(edtPlaca.text.toString(), edtMarca.text.toString(), edtModelo.text.toString(), edtAno.text.toString())
                edtMarca.text.clear()
                edtModelo.text.clear()
                edtAno.text.clear()
                edtPlaca.text.clear()

                val editor = preferenciasApp.edit()
                editor.remove("PLACA")
                editor.remove("MARCA")
                editor.remove("MODELO")
                editor.remove("ANO")
                editor.apply()
            } else {
                edtPlaca.error = "Insira um texto válido!"
                edtMarca.error = "Insira um texto válido!"
                edtModelo.error = "Insira um texto válido!"
                edtAno.error = "Insira um texto válido!"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarVeiculos()
    }

    override fun onPause() {
        super.onPause()
        if (edtPlaca.text.toString().isNotEmpty() && edtModelo.text.toString().isNotEmpty() && edtMarca.text.toString().isNotEmpty() && edtAno.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("PLACA", edtPlaca.text.toString())
            editor.putString("MODELO", edtModelo.text.toString())
            editor.putString("MARCA", edtMarca.text.toString())
            editor.putString("ANO", edtAno.text.toString())

            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        edtPlaca.setText(preferenciasApp.getString("PLACA", null))
        edtModelo.setText(preferenciasApp.getString("MODELO", null))
        edtMarca.setText(preferenciasApp.getString("MARCA", null))
        edtAno.setText(preferenciasApp.getString("ANO", null))
    }

    fun adicionarVeiculo(placaVeiculo: String, marcaVeiculo: String, modeloVeiculo: String, anoVeiculo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.VeiculosDAO()?.addVeiculo(TBVeiculos(placa = placaVeiculo, marca = marcaVeiculo, modelo = modeloVeiculo, ano = anoVeiculo))

            val veiculosDAO = db?.VeiculosDAO()

            val resposta = veiculosDAO?.getVeiculos()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    veiculosAdapter.refreshListVeiculos(resposta)
                }
            }
        }
    }

    fun recuperarVeiculos(){
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            val veiculosDAO = db?.VeiculosDAO()

            val resposta = veiculosDAO?.getVeiculos()

            withContext(Dispatchers.Main){
                resposta?.let{
                    veiculosAdapter = VeiculosAdapter(it, this@Veiculos)

                    rvveiculo.adapter = veiculosAdapter

                    rvveiculo.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    override fun excluirVeiculo(veiculo: TBVeiculos) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.VeiculosDAO()?.deleteVeiculo(veiculo)

            val veiculosDAO = db?.VeiculosDAO()

            val resposta = veiculosDAO?.getVeiculos()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    veiculosAdapter.refreshListVeiculos(resposta)

                    Toast.makeText(requireContext(), "Veiculo excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
