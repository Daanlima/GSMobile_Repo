package com.danielfortes.globalsolution_carcontrol

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), AgendamentoAdapterListener, LavaRapidoAdapterListener, TutoriaisAdapterListener, VeiculosAdapterListener {

    lateinit var menu: TabLayout
    lateinit var visualizador: ViewPager

    lateinit var agendamentoAdapter: AgendamentoAdapter
    lateinit var lavaRapidoAdapter: LavaRapidoAdapter
    lateinit var tutorialAdapter: TutoriaisAdapter
    lateinit var veiculosAdapter: VeiculosAdapter

    lateinit var edtNome: EditText
    lateinit var edtData: EditText
    lateinit var edtCPF: EditText

    lateinit var edtPlaca: EditText
    lateinit var edtModelo: EditText
    lateinit var edtMarca: EditText
    lateinit var edtAno: EditText

    lateinit var edtNomeLava: EditText
    lateinit var edtValor: EditText
    lateinit var edtCEP: EditText

    lateinit var edtTitulo: EditText
    lateinit var edtCorpo: EditText
    lateinit var edtLink: EditText

    lateinit var btnIncluirAgendamento: ImageButton
    lateinit var btnIncluirLavaRapido: ImageButton
    lateinit var btnIncluirTutorial: ImageButton
    lateinit var btnIncluirVeiculos: ImageButton

    lateinit var preferenciasApp: SharedPreferences

    lateinit var rvagendamento: RecyclerView
    lateinit var rvlavarapido: RecyclerView
    lateinit var rvveiculo: RecyclerView
    lateinit var rvtutorial: RecyclerView

    var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menu = findViewById(R.id.tblMenu)
        visualizador = findViewById(R.id.vpgVisualizador)

        // TabLayout --> ViewPager --> Adapter

        visualizador.adapter = PagerAdapter(supportFragmentManager)

        menu.setupWithViewPager(visualizador)

        preferenciasApp = getSharedPreferences("appPreferences", Context.MODE_PRIVATE)

        rvagendamento = findViewById<RecyclerView>(R.id.rvAgendamento)
        rvlavarapido = findViewById<RecyclerView>(R.id.rvLavaRapido)
        rvveiculo = findViewById<RecyclerView>(R.id.rvVeiculo)
        rvtutorial = findViewById<RecyclerView>(R.id.rvTutorial)

        btnIncluirAgendamento = findViewById(R.id.btnIncluirAgendamento)
        btnIncluirLavaRapido = findViewById(R.id.btnIncluirLavaRapido)
        btnIncluirTutorial = findViewById(R.id.btnIncluirTutorial)
        btnIncluirVeiculos = findViewById(R.id.btnIncluirVeiculos)

        edtNome = findViewById(R.id.edtNome)
        edtData = findViewById(R.id.edtData)
        edtCPF = findViewById(R.id.edtCPF)

        edtPlaca = findViewById(R.id.edtPlaca)
        edtModelo = findViewById(R.id.edtModelo)
        edtMarca = findViewById(R.id.edtMarca)
        edtAno = findViewById(R.id.edtAno)

        edtNomeLava = findViewById(R.id.edtNomeLava)
        edtValor = findViewById(R.id.edtValor)
        edtCEP = findViewById(R.id.edtCEP)

        edtTitulo = findViewById(R.id.edtTitulo)
        edtCorpo = findViewById(R.id.edtCorpo)
        edtLink = findViewById(R.id.edtLink)

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
                editor.commit()
            } else {
                edtNome.error = "Insira um texto válido!"
                edtData.error = "Insira um texto válido!"
                edtCPF.error = "Insira um texto válido!"
            }
        }

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
                editor.commit()
            } else {
                edtNome.error = "Insira um texto válido!"
                edtData.error = "Insira um texto válido!"
                edtCPF.error = "Insira um texto válido!"
            }
        }

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
                editor.commit()
            } else {
                edtPlaca.error = "Insira um texto válido!"
                edtMarca.error = "Insira um texto válido!"
                edtModelo.error = "Insira um texto válido!"
                edtAno.error = "Insira um texto válido!"
            }
        }

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
                editor.commit()
            } else {
                edtTitulo.error = "Insira um texto válido!"
                edtCorpo.error = "Insira um texto válido!"
                edtLink.error = "Insira um texto válido!"
            }
        }

    }

    // AGENDAMENTO ==============================================================

    fun adicionarAgendamento(nomeAgendamento: String, dataAgendamento: String, cpfAgendamento: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

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
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            val agendamentoDAO = db?.AgendamentoDAO()

            val resposta = agendamentoDAO?.getAgendamentos()

            withContext(Dispatchers.Main){
                resposta?.let{
                    agendamentoAdapter = AgendamentoAdapter(it, this@MainActivity)

                    rvagendamento.adapter = agendamentoAdapter

                    rvagendamento.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    override fun excluirAgendamento(agendamento: TBAgendamento) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            db?.AgendamentoDAO()?.deleteAgendamento(agendamento)

            val agendamentoDAO = db?.AgendamentoDAO()

            val resposta = agendamentoDAO?.getAgendamentos()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    agendamentoAdapter.refreshListAgendamento(resposta)

                    Toast.makeText(this@MainActivity, "Agendamento excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // FIM AGENDAMENTO ==============================================================

    // LAVARAPIDO ==============================================================

    fun adicionarLavaRapido(nomeLavaRapido: String, cepLavaRapido: String, valorLavaRapido: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

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
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            val lavarapidoDAO = db?.LavaRapidoDAO()

            val resposta = lavarapidoDAO?.getLavaRapido()

            withContext(Dispatchers.Main){
                resposta?.let{
                    lavaRapidoAdapter = LavaRapidoAdapter(it, this@MainActivity)

                    rvlavarapido.adapter = lavaRapidoAdapter

                    rvlavarapido.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    override fun excluirLavaRapido(lavarapido: TBLavaRapido) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            db?.LavaRapidoDAO()?.deleteLavaRapido(lavarapido)

            val lavarapidoDAO = db?.LavaRapidoDAO()

            val resposta = lavarapidoDAO?.getLavaRapido()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    lavaRapidoAdapter.refreshListAgendamento(resposta)

                    Toast.makeText(this@MainActivity, "Lava Rapido excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // FIM LAVARAPIDO ==============================================================

    // VEICULO ==============================================================

    fun adicionarVeiculo(placaVeiculo: String, marcaVeiculo: String, modeloVeiculo: String, anoVeiculo: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

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
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            val veiculosDAO = db?.VeiculosDAO()

            val resposta = veiculosDAO?.getVeiculos()

            withContext(Dispatchers.Main){
                resposta?.let{
                    veiculosAdapter = VeiculosAdapter(it, this@MainActivity)

                    rvveiculo.adapter = veiculosAdapter

                    rvveiculo.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    override fun excluirVeiculo(veiculo: TBVeiculos) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            db?.VeiculosDAO()?.deleteVeiculo(veiculo)

            val veiculosDAO = db?.VeiculosDAO()

            val resposta = veiculosDAO?.getVeiculos()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    veiculosAdapter.refreshListVeiculos(resposta)

                    Toast.makeText(this@MainActivity, "Veiculo excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    // FIM VEICULO ==============================================================

    // TUTORIAL ==============================================================

    fun adicionarTutorial(tituloTutorial: String, corpoTutorial: String, linkTutorial: String) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

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
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            val tutorialDAO = db?.TutoriaisDAO()
            val resposta = tutorialDAO?.getTutoriais()

            withContext(Dispatchers.Main){
                resposta?.let{
                    tutorialAdapter = TutoriaisAdapter(it, this@MainActivity)

                    rvtutorial.adapter = tutorialAdapter

                    rvtutorial.layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }

    override fun excluirTutorial(tutorial: TBTutoriais) {
        CoroutineScope(Dispatchers.IO).launch {
            db = DatabaseBuilder.getAppDatabase(this@MainActivity)

            db?.TutoriaisDAO()?.deleteTutoriais(tutorial)

            val tutorialDAO = db?.TutoriaisDAO()

            val resposta = tutorialDAO?.getTutoriais()

            withContext(Dispatchers.Main) {
                resposta?.let {
                    tutorialAdapter.refreshListTutorial(resposta)

                    Toast.makeText(this@MainActivity, "Tutorial excluído", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    // FIM TUTORIAL ==============================================================

    override fun onStart() {
        super.onStart()

        recuperarAgendamento()
        recuperarLavaRapido()
        recuperarVeiculos()
        recuperarTutoriais()

    }

    override fun onPause() {
        super.onPause()

        if (edtNome.text.toString().isNotEmpty() && edtData.text.toString().isNotEmpty() && edtCPF.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("NOME", edtNome.text.toString())
            editor.putString("DATA", edtData.text.toString())
            editor.putString("CPF", edtCPF.text.toString())
            editor.commit()
        }

        if (edtPlaca.text.toString().isNotEmpty() && edtModelo.text.toString().isNotEmpty() && edtMarca.text.toString().isNotEmpty() && edtAno.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("PLACA", edtPlaca.text.toString())
            editor.putString("MODELO", edtModelo.text.toString())
            editor.putString("MARCA", edtMarca.text.toString())
            editor.putString("ANO", edtAno.text.toString())

            editor.commit()
        }

        if (edtNomeLava.text.toString().isNotEmpty() && edtValor.text.toString().isNotEmpty() && edtCEP.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("NOME", edtNomeLava.text.toString())
            editor.putString("VALOR", edtValor.text.toString())
            editor.putString("CEP", edtCEP.text.toString())

            editor.commit()
        }

        if (edtTitulo.text.toString().isNotEmpty() && edtCorpo.text.toString().isNotEmpty() && edtLink.text.toString().isNotEmpty()){

            val editor = preferenciasApp.edit()

            editor.putString("TITULO", edtTitulo.text.toString())
            editor.putString("corpotutorial", edtCorpo.text.toString())
            editor.putString("LINK", edtLink.text.toString())

            editor.commit()
        }
    }

    override fun onResume() {
        super.onResume()

        edtNome.setText(preferenciasApp.getString("NOME", null))
        edtData.setText(preferenciasApp.getString("DATA", null))
        edtCPF.setText(preferenciasApp.getString("CPF", null))

        edtPlaca.setText(preferenciasApp.getString("PLACA", null))
        edtModelo.setText(preferenciasApp.getString("MODELO", null))
        edtMarca.setText(preferenciasApp.getString("MARCA", null))
        edtCEP.setText(preferenciasApp.getString("ANO", null))

        edtNomeLava.setText(preferenciasApp.getString("NOME", null))
        edtValor.setText(preferenciasApp.getString("VALOR", null))
        edtCPF.setText(preferenciasApp.getString("CEP", null))

        edtNome.setText(preferenciasApp.getString("TITULO", null))
        edtData.setText(preferenciasApp.getString("corpotutorial", null))
        edtCPF.setText(preferenciasApp.getString("LINK", null))

    }

}