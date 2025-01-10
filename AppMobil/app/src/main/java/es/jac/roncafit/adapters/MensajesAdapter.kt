package es.jac.roncafit.adapters

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.models.chat.Cliente
import es.jac.roncafit.models.chat.MensajeFlow

class MensajesAdapter (private val context: Context,
                        private val items: MutableList<MensajeFlow>,
                        private val nombresChat: MutableList<Cliente>) :
    RecyclerView.Adapter<MensajesAdapter.ViewHolder>() {

    private lateinit var vista: View


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        vista = LayoutInflater.from(context).inflate(R.layout.mensaje_item, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindItem(item,vista,nombresChat)
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNombreUsuario: TextView = view.findViewById(R.id.tvRemitente)
        private val tvContenido: TextView = view.findViewById(R.id.tvContent)
        private val cardView: CardView = view.findViewById(R.id.cardView_mensaje)


        fun bindItem(item: MensajeFlow, view: View, nombresChat: MutableList<Cliente>) {


            val remitente = nombresChat.find { it.idCliente == item.idRemitente }
            if (remitente != null) {
                if (!remitente.nombreCliente.isNullOrBlank()){
                    tvNombreUsuario.text = remitente.nombreCliente
                }else{
                    tvNombreUsuario.text = "IdUsuario: ${item.idRemitente}"
                }
            } else {
                tvNombreUsuario.text = "IdUsuario: ${item.idRemitente}"
            }

            val sharedPreferences = view.context.getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
            val idCliente = sharedPreferences.getInt("auth_idCliente", 0)
            tvContenido.text = item.mensaje
            if (item.idRemitente == idCliente){
                tvContenido.gravity = Gravity.END
                tvNombreUsuario.gravity = Gravity.END
                var colorRemitente : Int = view.context.getColor(R.color.colorPrimary)
                cardView.setCardBackgroundColor(colorRemitente)

                val layoutParams = cardView.layoutParams as RelativeLayout.LayoutParams
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START, 0)
                cardView.layoutParams = layoutParams
            }else{
                //Poner el color por defecto
                val colorPorDefecto: Int = view.context.getColor(R.color.gris)
                cardView.setCardBackgroundColor(colorPorDefecto)
                tvContenido.gravity = Gravity.START
                tvNombreUsuario.gravity = Gravity.START
                //Poner el menssaje en su sitio por defecto
                val layoutParams = cardView.layoutParams as RelativeLayout.LayoutParams
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END, 0)
                cardView.layoutParams = layoutParams

            }
        }
    }

}