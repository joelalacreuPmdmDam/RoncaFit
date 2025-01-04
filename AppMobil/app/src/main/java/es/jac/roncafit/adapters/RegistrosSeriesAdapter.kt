package es.jac.roncafit.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.models.ejercicios.RegistrosSeriesResponse

class RegistrosSeriesAdapter(
    private var registrosSeriesList: MutableList<RegistrosSeriesResponse>,
    private val onItemClickListener: (RegistrosSeriesResponse) -> Unit
) : RecyclerView.Adapter<RegistrosSeriesAdapter.RegistrosSeriesViewHolder>() {

    private var selectedPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegistrosSeriesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RegistrosSeriesViewHolder(layoutInflater.inflate(R.layout.registro_serie_item, parent, false))
    }

    override fun getItemCount(): Int = registrosSeriesList.size

    override fun onBindViewHolder(holder: RegistrosSeriesViewHolder, position: Int) {
        val item = registrosSeriesList[position]
        holder.bind(item)
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(Color.LTGRAY)
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            //NUEVO
            val previousPosition = selectedPosition
            selectedPosition = if (selectedPosition == position) -1 else position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)
            onItemClickListener(item)
        }
    }

    fun getSelectedPosition(): Int{
        return selectedPosition
    }

    fun updateData(newRegistrosSeriesList: List<RegistrosSeriesResponse>) {
        registrosSeriesList.clear()
        registrosSeriesList.addAll(newRegistrosSeriesList)
        selectedPosition = -1
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        if (position >= 0 && position < registrosSeriesList.size) {
            registrosSeriesList.removeAt(position)
            if (position == selectedPosition) selectedPosition = -1
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, registrosSeriesList.size)
        }
    }

    fun updateItem(position: Int, nuevoPeso: Float, nuevasRepeticiones: Int) {
        if (position >= 0 && position < registrosSeriesList.size) {
            val registro = registrosSeriesList[position]
            registro.peso = nuevoPeso
            registro.repeticiones = nuevasRepeticiones
            notifyItemChanged(position)
        }
    }


    class RegistrosSeriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {


        private val tv_idSerie = view.findViewById<TextView>(R.id.tv_idSerie)
        private val tv_pesoSerie = view.findViewById<TextView>(R.id.tv_pesoSerie)
        private val tv_repsSerie = view.findViewById<TextView>(R.id.tv_repeticionesSerie)

        fun bind(item: RegistrosSeriesResponse) {
            tv_idSerie.text = item.idSerie.toString()
            tv_pesoSerie.text = item.peso.toString()
            tv_repsSerie.text = item.repeticiones.toString()
        }

    }
}