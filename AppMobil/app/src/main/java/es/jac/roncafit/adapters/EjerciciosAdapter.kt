package es.jac.roncafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.models.actividades.ActividadResponse
import es.jac.roncafit.models.ejercicios.EjerciciosResponse

class EjerciciosAdapter(
    private var ejerciciosList: MutableList<EjerciciosResponse>,
    private val onItemClickListener: (EjerciciosResponse) -> Unit
) : RecyclerView.Adapter<EjerciciosAdapter.EjerciciosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EjerciciosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EjerciciosViewHolder(layoutInflater.inflate(R.layout.ejercicio_item, parent, false))
    }

    override fun getItemCount(): Int = ejerciciosList.size

    override fun onBindViewHolder(holder: EjerciciosViewHolder, position: Int) {
        val item = ejerciciosList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener(item)
        }
    }

    fun updateData(newEjerciciosList: List<EjerciciosResponse>) {
        //ejerciciosList.clear()
        //ejerciciosList.addAll(newEjerciciosList)
        ejerciciosList = newEjerciciosList.toMutableList()
        notifyDataSetChanged()
    }

    class EjerciciosViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tv_nombreEjercicio = view.findViewById<TextView>(R.id.tv_nombreEjercicio)

        fun bind(item: EjerciciosResponse) {
            tv_nombreEjercicio.text = item.nombreEjercicio
        }
    }
}