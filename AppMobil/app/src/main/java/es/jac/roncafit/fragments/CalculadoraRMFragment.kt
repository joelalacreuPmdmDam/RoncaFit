package es.jac.roncafit.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import es.jac.roncafit.R
import es.jac.roncafit.databinding.FragmentCalculadoraRMBinding


class CalculadoraRMFragment : Fragment() {

    private lateinit var binding: FragmentCalculadoraRMBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculadoraRMBinding.inflate(inflater,container,false)

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
        binding.btnCalculate.setOnClickListener{
            binding.tvRmTitle.visibility = View.VISIBLE
            val peso = binding.etPeso.text.toString().toDouble()
            val reps = binding.etReps.text.toString().toInt()
            if (reps>1){
                val resultado = (peso*0.033*reps)+peso
                binding.tvResultRm.setText(resultado.toString()+"kg")
            }else if (reps==1){
                binding.tvResultRm.setText(peso.toString()+"kg")
            }else{
                binding.tvResultRm.setText("0kg")
            }

        }

        return binding.root
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

}