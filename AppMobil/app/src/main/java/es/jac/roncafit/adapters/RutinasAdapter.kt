package es.jac.roncafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.models.ejercicios.RutinaResponse

class RutinasAdapter (private var rutinasList: MutableList<RutinaResponse>,
private val onItemClickListener: (RutinaResponse) -> Unit
) : RecyclerView.Adapter<RutinasAdapter.RutinasViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RutinasViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RutinasViewHolder(layoutInflater.inflate(R.layout.rutina_item, parent, false))
    }

    override fun getItemCount(): Int = rutinasList.size

    override fun onBindViewHolder(holder: RutinasViewHolder, position: Int) {
        val item = rutinasList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener(item)
        }
    }

    fun updateData(newRutinasList: List<RutinaResponse>) {
        rutinasList.clear()
        rutinasList.addAll(newRutinasList)
        notifyDataSetChanged()
    }

    class RutinasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tv_nombreRutina = view.findViewById<TextView>(R.id.tv_nombreRutina)


        fun bind(item: RutinaResponse) {
            tv_nombreRutina.text = item.nombreRutina.uppercase()
        }
    }
}