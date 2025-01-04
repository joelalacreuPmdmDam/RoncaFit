package es.jac.roncafit.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import es.jac.roncafit.adapters.RutinasAdapter
import es.jac.roncafit.databinding.FragmentListaRutinasBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.ejercicios.EjerciciosRequest
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.ejercicios.RutinaResponse
import es.jac.roncafit.services.ejercicios.EjerciciosService
import es.jac.roncafit.services.ejercicios.RutinasService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ListaRutinasFragment : Fragment() {

    private lateinit var binding: FragmentListaRutinasBinding
    private lateinit var mAdapter: RutinasAdapter
    private lateinit var listaRutinas: MutableList<RutinaResponse>
    private lateinit var mListener: ListaRutinasFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ListaRutinasFragmentListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface ListaRutinasFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaRutinasBinding.inflate(inflater,container,false)
        setUpRecyclerView()
        obtenerRutinas()
        return binding.root
    }

    private fun setUpRecyclerView() {
        listaRutinas = mutableListOf()
        mAdapter = RutinasAdapter(listaRutinas) { rutina ->
            mListener.onRutinaClicked(rutina)
        }
        binding.recyclerRutinas.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        binding.recyclerRutinas.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerRutinas.adapter = mAdapter
    }

    private fun obtenerRutinas() {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(RutinasService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listaRutinas = apiService.getRutinas("Bearer $token")
                withContext(Dispatchers.Main) {
                    mAdapter.updateData(listaRutinas.rutinas)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    interface ListaRutinasFragmentListener{
        fun onRutinaClicked(rutina: RutinaResponse)
    }

}