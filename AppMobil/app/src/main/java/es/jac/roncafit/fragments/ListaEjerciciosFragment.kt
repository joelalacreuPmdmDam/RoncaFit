package es.jac.roncafit.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import es.jac.roncafit.adapters.EjerciciosAdapter
import es.jac.roncafit.databinding.FragmentListaEjerciciosBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.ejercicios.EjerciciosRequest
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.ejercicios.RutinaResponse
import es.jac.roncafit.services.ejercicios.EjerciciosService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListaEjerciciosFragment : Fragment() {

    private lateinit var binding: FragmentListaEjerciciosBinding
    private lateinit var mAdapter: EjerciciosAdapter
    private lateinit var listaEjerciciosCompleta: MutableList<EjerciciosResponse>
    private lateinit var listaEjerciciosFiltrada: MutableList<EjerciciosResponse>
    private lateinit var mListener: EjercicioFragmentListener
    private var idRutinaPasada: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is EjercicioFragmentListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface EjercicioFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaEjerciciosBinding.inflate(inflater,container,false)

        // Obtener los datos del Bundle
        val rutinaPasada = arguments?.getParcelable<RutinaResponse>("rutina")
        if (rutinaPasada != null && rutinaPasada.idRutina>0) {
            idRutinaPasada = rutinaPasada.idRutina
        }

        setUpRecyclerView()
        obtenerEjercicios()
        setUpSearchView()
        return binding.root
    }

    private fun setUpRecyclerView() {
        listaEjerciciosCompleta = mutableListOf()
        listaEjerciciosFiltrada = mutableListOf()
        val onEjercicioClickedFun: (EjerciciosResponse) -> Unit = { ejercicio ->
            mListener.onEjercicioClicked(ejercicio,idRutinaPasada)
        }

        val onInfoClickedFun: (EjerciciosResponse) -> Unit = { ejercicio ->
            mListener.onInfoClicked(ejercicio)
        }
        mAdapter = EjerciciosAdapter(listaEjerciciosCompleta,onEjercicioClickedFun,onInfoClickedFun)
        binding.recyclerEjercicios.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        binding.recyclerEjercicios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerEjercicios.adapter = mAdapter
    }

    private fun obtenerEjercicios() {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(EjerciciosService::class.java)
        val ejercicio: EjerciciosRequest
        if (idRutinaPasada == 6){
            ejercicio = EjerciciosRequest(idRutina = null)
        }else{
            ejercicio = EjerciciosRequest(idRutina = idRutinaPasada)
        }
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listaEjercicios = apiService.getEjercicios("Bearer $token",ejercicio)
                withContext(Dispatchers.Main) {
                    listaEjerciciosCompleta.addAll(listaEjercicios.ejercicios)
                    listaEjerciciosFiltrada.addAll(listaEjercicios.ejercicios)
                    mAdapter.updateData(listaEjercicios.ejercicios)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText.orEmpty())
                return true
            }
        })
    }


    private fun filtrarLista(texto: String) {
        val nuevaLista: MutableList<EjerciciosResponse>
        if (texto.isEmpty()) {
            nuevaLista = listaEjerciciosCompleta
        } else {
            nuevaLista = listaEjerciciosCompleta.filter { ejercicio ->
                ejercicio.nombreEjercicio.contains(texto, ignoreCase = true)
            }.toMutableList()
        }
        listaEjerciciosFiltrada.clear()
        listaEjerciciosFiltrada.addAll(nuevaLista)
        mAdapter.updateData(listaEjerciciosFiltrada)
    }


    interface EjercicioFragmentListener{
        fun onEjercicioClicked(ejercicio: EjerciciosResponse,idRutina: Int)
        fun onInfoClicked(ejercicio: EjerciciosResponse)
    }

    companion object {
        fun newInstance(rutina: RutinaResponse): ListaEjerciciosFragment {
            val fragment = ListaEjerciciosFragment()
            val args = Bundle().apply {
                putParcelable("rutina", rutina)
            }
            fragment.arguments = args
            return fragment
        }
    }
}