package es.jac.roncafit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import es.jac.roncafit.R
import es.jac.roncafit.databinding.FragmentAlertasBinding


class AlertasFragment : Fragment() {

    private lateinit var binding: FragmentAlertasBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlertasBinding.inflate(inflater,container,false)

        return binding.root
    }

}