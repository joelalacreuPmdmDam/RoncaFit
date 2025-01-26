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
import es.jac.roncafit.databinding.FragmentInicioBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.actividades.ActividadKot
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.services.actividades.TablonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class InicioFragment : Fragment() {

    private lateinit var binding: FragmentInicioBinding
    private lateinit var mAdapter: ActividadesTablonAdapter
    private lateinit var mListener: InicioFragmentListener
    private lateinit var listaActTablon: MutableList<ActividadResponse>
    val list = mutableListOf<CarouselItem>()


    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is InicioFragmentListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface InicioFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater,container,false)
        setUpCarrusel()
        setUpRecyclerView()
        obtenerActividades()
        return binding.root
    }



    private fun setUpCarrusel(){
        list.add(CarouselItem(
            "https://storage.googleapis.com/download/storage/v1/b/usc-pro-uscweb-live-media/o/de-live%2FvenueHome_1024x576_35377565_1021132074711000_1343829841681580032_n_1578563980605859_admin.jpg?alt=media",
            "Nuestras instalaciones"
        ))
        list.add(CarouselItem(
            "https://lh3.googleusercontent.com/proxy/2dx99X_hv-JPs782DtnjyWpf24J-PJmZTG5Z1NhmGRFZM9hpCv82lHNPNaIHUPJMCv4ssyjB25XY8t1K6--zf8CIy9BwyTPncrcKf3Vi-pzE3Vy3w5Kkqj0ia5R3gAIbnA",
            "Horario temporada 24-25"
        ))
        /*list.add(CarouselItem(
            "https://lh3.googleusercontent.com/proxy/2UGcYG4oSTExDammi7EdKPLGjNJEmvbbe4PTnw3xBc3qosgdQ00sQ66yO3gIijMh_wHBE1coXojmClY-5JNY7BoPRSTIspvvosxsDYfd7NndFWAG5oVQ4CJLyozzjEE2vQupLwNGdBA2mRh9ptltKXr6K4YyzF9wUCNiEmpaSr1U",
            "Próximamente clases de padel!!!"
        ))*/
        list.add(CarouselItem(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTv7o203XLk2mulGW2uzzo7quLDRCCrsBmg-Q&s",
            "Festivos"
        ))
        val carousel: ImageCarousel = binding.carousel
        carousel.addData(list)
    }


    private fun setUpRecyclerView() {
        listaActTablon = mutableListOf()
        mAdapter = ActividadesTablonAdapter(listaActTablon) { actividad ->
            mListener.onActividadClickedInicio(actividad)
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
        val apiService = RetrofitObject.getInstance().create(TablonService::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val tablonActividades = apiService.getTablon("Bearer $token",userId)
                if(tablonActividades.tablonActividades.isNotEmpty()){
                   binding.tvTituloListaActs.text = "Actividades"
                }else{
                    binding.tvTituloListaActs.text = "No hay actividades disponibles"
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

    interface InicioFragmentListener{
        fun onActividadClickedInicio(actividad: ActividadResponse)
    }



}