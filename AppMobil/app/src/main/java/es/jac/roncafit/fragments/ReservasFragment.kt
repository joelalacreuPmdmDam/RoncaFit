package es.jac.roncafit.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.jac.roncafit.R
import es.jac.roncafit.adapters.ActividadesTablonAdapter
import es.jac.roncafit.databinding.FragmentReservasBinding
import es.jac.roncafit.fragments.InicioFragment.InicioFragmentListener
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.services.actividades.TablonService
import es.jac.roncafit.services.reservas.ReservasService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ReservasFragment : Fragment() {

    private lateinit var binding: FragmentReservasBinding
    private lateinit var mAdapter: ActividadesTablonAdapter
    private lateinit var mListener: ReservasFragmentListener
    private lateinit var listaActTablon: MutableList<ActividadResponse>

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ReservasFragmentListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface ReservasFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReservasBinding.inflate(inflater,container,false)
        setUpRecyclerView()
        obtenerActividades()
        return binding.root
    }

    private fun setUpRecyclerView() {
        listaActTablon = mutableListOf()
        mAdapter = ActividadesTablonAdapter(listaActTablon) { actividad ->
            mListener.onActividadClickedReservas(actividad)
        }
        binding.recyclerTablon.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerTablon.adapter = mAdapter
    }


    private fun obtenerActividades() {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("auth_idCliente", -1)
        val token = sharedPreferences.getString("auth_token", null)

        if (userId == -1) {
            Toast.makeText(requireContext(), "ID de usuario no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(ReservasService::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val tablonActividades = apiService.getReservas("Bearer $token",userId)
                if(tablonActividades.tablonActividades.isNotEmpty()){
                    binding.tvTituloListaActs.text = "Reservas"
                }else{
                    binding.tvTituloListaActs.text = "No hay reservas"
                }
                withContext(Dispatchers.Main) {
                    mAdapter.updateData(tablonActividades.tablonActividades)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud, contacte con un administrador", Toast.LENGTH_SHORT).show()
                    Log.d("Error InicioFragment",e.message.toString())
                }
            }
        }
    }

    interface ReservasFragmentListener{
        fun onActividadClickedReservas(actividad: ActividadResponse)
    }


}