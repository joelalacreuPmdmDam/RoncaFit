package es.jac.roncafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.models.actividades.ActividadResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class ActividadesTablonAdapter(
    private var actividadesList: MutableList<ActividadResponse>,
    private val onItemClickListener: (ActividadResponse) -> Unit
) : RecyclerView.Adapter<ActividadesTablonAdapter.ActividadesTablonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActividadesTablonViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ActividadesTablonViewHolder(layoutInflater.inflate(R.layout.tablon_item, parent, false))
    }

    override fun getItemCount(): Int = actividadesList.size

    override fun onBindViewHolder(holder: ActividadesTablonViewHolder, position: Int) {
        val item = actividadesList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener(item)
        }
    }

    fun updateData(newActividadesList: List<ActividadResponse>) {
        actividadesList.clear()
        actividadesList.addAll(newActividadesList)
        notifyDataSetChanged()
    }

    class ActividadesTablonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tv_nombreActividad = view.findViewById<TextView>(R.id.tv_nombreActividad)
        private val tv_fechaActividad = view.findViewById<TextView>(R.id.tv_fechaAct)
        private val tv_inscripcionesAct = view.findViewById<TextView>(R.id.tv_inscripcionesAct)

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

        fun bind(item: ActividadResponse) {
            tv_nombreActividad.text = item.actividad.uppercase()
            tv_inscripcionesAct.text = "Inscripciones: ${item.inscripciones}/${item.limite}"
            tv_fechaActividad.text = formatDate(item.fecha)
        }
    }
}
