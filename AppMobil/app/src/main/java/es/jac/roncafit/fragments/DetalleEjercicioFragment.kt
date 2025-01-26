package es.jac.roncafit.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import es.jac.roncafit.databinding.FragmentDetalleEjercicioBinding
import es.jac.roncafit.models.ejercicios.EjerciciosResponse


class DetalleEjercicioFragment : Fragment() {

    private lateinit var binding: FragmentDetalleEjercicioBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleEjercicioBinding.inflate(inflater,container,false)

        // Obtener los datos del Bundle
        val ejercicio = arguments?.getParcelable<EjerciciosResponse>("ejercicio")

        ejercicio?.let {
            binding.tvNombreEjercicio.text = it.nombreEjercicio
            binding.tvSeries.text = "Series: ${it.series}"
            binding.tvRepeticiones.text = "Repeticiones: ${it.reps}"
            binding.tvDescripcion.text = "Descripción: ${it.descripcion}"
            binding.tvInstrucciones.text = "Instrucciones: ${it.instrucciones}"
            if (!it.urlVideo.isNullOrBlank()){
                val player = ExoPlayer.Builder(requireContext()).build()
                binding.vvVideo.player = player
                val mediaItem = MediaItem.fromUri(it.urlVideo)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
            }


        }


        return binding.root
    }

    companion object {
        fun newInstance(ejercicio: EjerciciosResponse): DetalleEjercicioFragment {
            val fragment = DetalleEjercicioFragment()
            val args = Bundle().apply {
                putParcelable("ejercicio", ejercicio)
            }
            fragment.arguments = args
            return fragment
        }
    }
}