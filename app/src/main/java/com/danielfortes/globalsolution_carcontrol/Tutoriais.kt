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

class Tutoriais : Fragment(), TutoriaisAdapterListener {

    lateinit var edtTitulo: EditText
    lateinit var edtCorpo: EditText
    lateinit var edtLink: EditText
    lateinit var tutorialAdapter: TutoriaisAdapter
    lateinit var btnIncluirTutorial: ImageButton
    lateinit var rvtutorial: RecyclerView
    lateinit var preferenciasApp: SharedPreferences

    var db: AppDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tutoriais, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvtutorial = view.findViewById(R.id.rvTutorial)
        btnIncluirTutorial = view.findViewById(R.id.btnIncluirTutorial)
        edtTitulo = view.findViewById(R.id.edtTitulo)
        edtCorpo = view.findViewById(R.id.edtCorpo)
        edtLink = view.findViewById(R.id.edtLink)

        preferenciasApp = requireActivity().getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        btnIncluirTutorial.setOnClickListener() {
            if (edtTitulo.text.toString().isNotEmpty() && edtCorpo.text.toString().isNotEmpty() && edtLink.text.toString().isNotEmpty()){
                adicionarTutorial(edtTitulo.text.toString(), edtCorpo.text.toString(), edtLink.text.toString())
                edtTitulo.text.clear()
                edtCorpo.text.clear()
                edtLink.text.clear()

                val editor = preferenciasApp.edit()
                editor.remove("TITULO")
                editor.remove("corpotutorial")
                editor.remove("LINK")
                editor.apply()
            } else {
                edtTitulo.error = "Insira um texto válido!"
                edtCorpo.error = "Insira um texto válido!"
                edtLink.error = "Insira um texto válido!"
            }
        }
    }

    override fun onStart() {
        super.onStart()
        recuperarTutoriais()
    }

    override fun onPause() {
        super.onPause()
        if (edtTitulo.text.toString().isNotEmpty() && edtCorpo.text.toString().isNotEmpty() && edtLink.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("TITULO", edtTitulo.text.toString())
            editor.putString("corpotutorial", edtCorpo.text.toString())
            editor.putString("LINK", edtLink.text.toString())

            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        edtTitulo.setText(preferenciasApp.getString("TITULO", null))
        edtCorpo.setText(preferenciasApp.getString("corpotutorial", null))
        edtLink.setText(preferenciasApp.getString("LINK", null))
    }

    fun adicionarTutorial(tituloTutorial: String, corpoTutorial: String, linkTutorial: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.TutoriaisDAO()?.addTutoriais(TBTutoriais(titulo = tituloTutorial, corpotutorial = corpoTutorial, link = linkTutorial))

            val tutorialDAO = db?.TutoriaisDAO()

            val resposta = tutorialDAO?.getTutoriais()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    tutorialAdapter.refreshListTutorial(resposta)
                }
            }
        }
    }

    fun recuperarTutoriais(){
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            val tutorialDAO = db?.TutoriaisDAO()
            val resposta = tutorialDAO?.getTutoriais()

            withContext(Dispatchers.Main){
                resposta?.let{
                    tutorialAdapter = TutoriaisAdapter(it, this@Tutoriais)

                    rvtutorial.adapter = tutorialAdapter

                    rvtutorial.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    override fun excluirTutorial(tutorial: TBTutoriais) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(requireContext())

            db?.TutoriaisDAO()?.deleteTutoriais(tutorial)

            val tutorialDAO = db?.TutoriaisDAO()

            val resposta = tutorialDAO?.getTutoriais()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    tutorialAdapter.refreshListTutorial(resposta)

                    Toast.makeText(requireContext(), "Tutorial excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

}
