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
import es.jac.roncafit.R
import es.jac.roncafit.databinding.FragmentDetalleActividadBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.actividades.ActividadKot
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.models.reserva.ReservaRequest
import es.jac.roncafit.services.actividades.TablonService
import es.jac.roncafit.services.reservas.ReservasService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale


class DetalleActividadFragment : Fragment() {

    private lateinit var binding: FragmentDetalleActividadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleActividadBinding.inflate(inflater,container,false)

        // Obtener los datos del Bundle
        val actividad = arguments?.getParcelable<ActividadResponse>("actividad")
        val opcion = arguments?.getInt("opcion")
        if (opcion == 1){
            binding.btnReservarAct.text = "RESERVAR"
        }else if (opcion == 0){
            binding.btnReservarAct.text = "ELIMINAR RESERVA"
        }else{
            binding.btnReservarAct.visibility = View.INVISIBLE
        }

        // Mostrar los detalles de la actividad
        actividad?.let {
            binding.tvDetNombreAct.text = it.actividad
            binding.tvDetInstructorAct.text = it.instructor
            binding.tvDetFechaAct.text = formatDate(it.fecha)
            binding.tvDetInscripcionesAct.text = "${it.inscripciones}/${it.limite} reservadas"
        }

        binding.btnReservarAct.setOnClickListener {
            if (opcion == 1){
                reservarActividad(actividad?.id)
            }else{
                eliminarReserva(actividad?.idReserva)
            }

        }

        return binding.root
    }

    fun reservarActividad(idActividad: Int?){
        if (idActividad != null){
            // Información del usuario
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
            val reservaRequest = ReservaRequest(idActividadTablon = idActividad, idCliente = userId)
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = apiService.insertarReserva("Bearer $token",reservaRequest)
                    if (response.isSuccessful){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(),"Actividad reservada!",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        if(response.code() == 409){
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(),"El cliente tiene una reserva que causa conflictos.",Toast.LENGTH_SHORT).show()
                            }
                        }else{
                            withContext(Dispatchers.Main) {
                                Toast.makeText(requireContext(),"Error en el servidor, contacte con un administrador",Toast.LENGTH_SHORT).show()
                                Log.d("Error en DetalleActividadFragment","${response.message()} ${response.code()}")
                            }
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error, contacte con el administrador", Toast.LENGTH_SHORT).show()
                        Log.d("Error en DetalleActividadFragment",e.message.toString())
                    }
                }
            }
        }
    }

    fun eliminarReserva(idReserva: Int?){
        if (idReserva != null){
            // Información del usuario
            val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", null)

            if (token == null) {
                Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
                return
            }
            val apiService = RetrofitObject.getInstance().create(ReservasService::class.java)
            val reservaRequest = ReservaRequest(idReserva = idReserva)
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = apiService.eliminarReserva("Bearer $token",reservaRequest)
                    if (response.isSuccessful){
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(),"Reserva eliminada!",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(),"Error en el servidor, contacte con un administrador",Toast.LENGTH_SHORT).show()
                            Log.d("Error en DetalleActividadFragment","${response.message()} ${response.code()}")
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Error, contacte con el administrador", Toast.LENGTH_SHORT).show()
                        Log.d("Error en DetalleActividadFragment",e.message.toString())
                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(actividad: ActividadResponse, opcion: Int): DetalleActividadFragment {
            val fragment = DetalleActividadFragment()
            val args = Bundle().apply {
                putParcelable("actividad", actividad)
                putInt("opcion", opcion)
            }
            fragment.arguments = args
            return fragment
        }
    }

    fun formatDate(inputDate: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }


}