package es.jac.roncafit.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.jac.roncafit.R
import es.jac.roncafit.adapters.ActividadesTablonAdapter
import es.jac.roncafit.adapters.RegistrosSeriesAdapter
import es.jac.roncafit.databinding.FragmentRegistrosSeriesBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.models.ejercicios.EjerciciosRequest
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.ejercicios.RegistrosSeriesRequest
import es.jac.roncafit.models.ejercicios.RegistrosSeriesResponse
import es.jac.roncafit.services.actividades.TablonService
import es.jac.roncafit.services.ejercicios.RegistrosSeriesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegistrosSeriesFragment : Fragment() {

    private lateinit var binding: FragmentRegistrosSeriesBinding
    private lateinit var mAdapter: RegistrosSeriesAdapter
    private lateinit var listaRegistrosSeries: MutableList<RegistrosSeriesResponse>
    private var idEjercicioPasado: Int = 0
    private var idRutinaPasada: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrosSeriesBinding.inflate(inflater,container,false)

        // Obtener los datos del Bundle
        val ejercicioPasado = arguments?.getParcelable<EjerciciosResponse>("ejercicio")
        if (ejercicioPasado != null && ejercicioPasado.idEjercicio>0){
            idEjercicioPasado = ejercicioPasado.idEjercicio
        }
        val idRutina = arguments?.getInt("idRutina")
        if (idRutina != null && idRutina>0){
            idRutinaPasada = idRutina
        }

        setUpRecyclerView()
        obtenerRegistros()

        binding.restarPesoBtn.setOnClickListener{
            val nuevoValor: Double = restar(binding.etPeso)
            binding.etPeso.setText(nuevoValor.toString())
        }
        binding.sumarPesoBtn.setOnClickListener{
            val nuevoValor: Double = sumar(binding.etPeso)
            binding.etPeso.setText(nuevoValor.toString())
        }
        binding.restarRepsBtn.setOnClickListener{
            val nuevoValor: Int = restarReps(binding.etReps)
            binding.etReps.setText(nuevoValor.toString())
        }
        binding.sumarRepsBtn.setOnClickListener{
            val nuevoValor: Int = sumarReps(binding.etReps)
            binding.etReps.setText(nuevoValor.toString())
        }

        binding.btnSave.setOnClickListener {
            val peso: Float = binding.etPeso.text.toString().toFloatOrNull() ?: 0.0f
            val reps: Int = binding.etReps.text.toString().toIntOrNull() ?: 0
            val selectedPosition = mAdapter.getSelectedPosition()
            if (selectedPosition != -1) {
                val registroSeleccionado = listaRegistrosSeries[selectedPosition]
                actualizarRegistro(registroSeleccionado.id,selectedPosition,peso,reps)
            } else {
                val ultimoRegistro = listaRegistrosSeries.lastOrNull()
                var idSerie: Int
                if (ultimoRegistro != null){
                    idSerie = ultimoRegistro.idSerie+1
                }else{
                    idSerie = 1
                }
                insertarRegistro(idSerie,peso,reps)
            }
        }

        binding.btnClear.setOnClickListener {
            val selectedPosition = mAdapter.getSelectedPosition()
            if (selectedPosition != -1) {
                val registroSeleccionado = listaRegistrosSeries[selectedPosition]
                eliminarRegistro(registroSeleccionado.id,selectedPosition)
            } else {
                binding.etPeso.setText("0")
                binding.etReps.setText("0")
            }
        }

        return binding.root
    }

    private fun setUpRecyclerView() {
        listaRegistrosSeries = mutableListOf()
        mAdapter = RegistrosSeriesAdapter(listaRegistrosSeries) { registroSerie ->
            registroClicked(registroSerie)
        }
        binding.recyclerSeries.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerSeries.adapter = mAdapter
    }

    private fun obtenerRegistros() {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        val idCliente = sharedPreferences.getInt("auth_idCliente", 0)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        if (idCliente == 0) {
            Toast.makeText(requireContext(), "idCliente no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(RegistrosSeriesService::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val registroRequest = RegistrosSeriesRequest(idCliente = idCliente, idEjercicio = idEjercicioPasado)
                val registros = apiService.getRegistrosSerie("Bearer $token",registroRequest)
                withContext(Dispatchers.Main) {
                    val ultimoRegistro = registros.registrosSeries.lastOrNull()
                    if (ultimoRegistro != null){
                        binding.etReps.setText(ultimoRegistro.repeticiones.toString())
                        binding.etPeso.setText(ultimoRegistro.peso.toString())
                    }
                    mAdapter.updateData(registros.registrosSeries)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun insertarRegistro(idSerie: Int, peso: Float, repeticiones: Int) {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)
        val idCliente = sharedPreferences.getInt("auth_idCliente", 0)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        if (idEjercicioPasado<1) {
            Toast.makeText(requireContext(), "idEjercicio no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        if (idCliente == 0) {
            Toast.makeText(requireContext(), "idCliente no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(RegistrosSeriesService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val registroRequest = RegistrosSeriesRequest(idCliente = idCliente, idEjercicio = idEjercicioPasado, idRutina = idRutinaPasada,
                        idSerie = idSerie,peso = peso, repeticiones = repeticiones)
                val response = apiService.insertarRegistrosSerie("Bearer $token",registroRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(),"Registro insertado correctamente",Toast.LENGTH_SHORT).show()
                        obtenerRegistros()
                    } else {
                        Toast.makeText(requireContext(),"Error en el servidor: ${response.code()} - ${response.message()}",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun eliminarRegistro(idRegistro: Int, selectedPosition: Int) {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(RegistrosSeriesService::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val registroRequest = RegistrosSeriesRequest(id = idRegistro)
                val response = apiService.eliminarRegistrosSerie("Bearer $token",registroRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(),"Registro eliminado correctamente",Toast.LENGTH_SHORT).show()
                        mAdapter.removeItem(selectedPosition)
                    } else {
                        Toast.makeText(requireContext(),"Error en el servidor: ${response.code()} - ${response.message()}",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun actualizarRegistro(idRegistro: Int, selectedPosition: Int, nuevoPeso: Float, nuevasRepeticiones: Int) {

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(RegistrosSeriesService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val registroRequest = RegistrosSeriesRequest(id = idRegistro, peso = nuevoPeso, repeticiones = nuevasRepeticiones)
                val response = apiService.actualizarRegistrosSerie("Bearer $token",registroRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(),"Registro actualizado correctamente",Toast.LENGTH_SHORT).show()
                        mAdapter.updateItem(selectedPosition,nuevoPeso,nuevasRepeticiones)
                    } else {
                        Toast.makeText(requireContext(),"Error en el servidor: ${response.code()} - ${response.message()}",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_LONG).show()

                }
            }
        }
    }

    private fun registroClicked(registroSerie: RegistrosSeriesResponse){
        binding.etPeso.setText(registroSerie.peso.toString())
        binding.etReps.setText(registroSerie.repeticiones.toString())
    }



    private fun restar(editText: EditText): Double{
        val valorActualStr = editText.text.toString()
        var valorActualNum:Double
        if (valorActualStr.isEmpty()){
            valorActualNum = "0.0".toDouble()
        }else{
            if (valorActualStr.toDouble()>2.4){
                valorActualNum = valorActualStr.toDouble()-2.5
            }else{
                valorActualNum = "0.0".toDouble()
            }
        }
        return valorActualNum
    }
    private fun sumar(editText: EditText): Double{
        val valorActualStr = editText.text.toString()
        var valorActualNum:Double
        if (valorActualStr.isEmpty()){
            valorActualNum = "0.0".toDouble()
        }else{
            valorActualNum = valorActualStr.toDouble()+2.5
        }
        return valorActualNum
    }

    private fun restarReps(editText: EditText): Int{
        val valorActualStr = editText.text.toString()
        var valorActualNum:Int
        if (valorActualStr.isEmpty()){
            valorActualNum = "0".toInt()
        }else{
            if (valorActualStr.toInt()>0){
                valorActualNum = valorActualStr.toInt()-1
            }else{
                valorActualNum = "0".toInt()
            }
        }
        return valorActualNum
    }
    private fun sumarReps(editText: EditText): Int{
        val valorActualStr = editText.text.toString()
        var valorActualNum:Int
        if (valorActualStr.isEmpty()){
            valorActualNum = "0.0".toInt()
        }else{
            valorActualNum = valorActualStr.toInt()+1
        }
        return valorActualNum
    }


    companion object {
        fun newInstance(ejercicio: EjerciciosResponse, idRutina: Int): RegistrosSeriesFragment {
            val fragment = RegistrosSeriesFragment()
            val args = Bundle().apply {
                putParcelable("ejercicio", ejercicio)
                putInt("idRutina", idRutina)
            }
            fragment.arguments = args
            return fragment
        }
    }

}