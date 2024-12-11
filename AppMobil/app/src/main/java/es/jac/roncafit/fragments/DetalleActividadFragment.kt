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

        // Mostrar los detalles de la actividad
        actividad?.let {
            binding.tvDetNombreAct.text = it.actividad
            binding.tvDetInstructorAct.text = it.instructor
            binding.tvDetFechaAct.text = formatDate(it.fecha)
            binding.tvDetInscripcionesAct.text = "${it.inscripciones}/${it.limite} reservadas"
        }

        binding.btnReservarAct.setOnClickListener {
            reservarActividad(actividad?.id)
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

                    apiService.insertarReserva("Bearer $token",reservaRequest)

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(),"Actividad reservada!",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("aaaaaaa",e.toString())
                        Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()

                    }
                }
            }
        }
    }


    companion object {
        fun newInstance(actividad: ActividadResponse): DetalleActividadFragment {
            val fragment = DetalleActividadFragment()
            val args = Bundle().apply {
                putParcelable("actividad", actividad)
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