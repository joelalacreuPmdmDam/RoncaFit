package es.jac.roncafit.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.models.chat.Cliente
import es.jac.roncafit.models.chat.MensajeFlow
import java.text.SimpleDateFormat
import java.util.Locale

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
        private val tvCreatedData: TextView = view.findViewById(R.id.tv_createdData)
        private val cardView: CardView = view.findViewById(R.id.cardView_mensaje)
        private val imgChat: ImageView = view.findViewById(R.id.img_chat)

        /*
        fun bindItem(item: MensajeFlow, view: View, nombresChat: MutableList<Cliente>) {


            val remitente = nombresChat.find { it.idCliente == item.idRemitente }
            if (remitente != null) {
                if (!remitente.nombreCliente.isNullOrBlank()){
                    tvNombreUsuario.text = remitente.nombreCliente
                    val decodedString = Base64.decode(remitente.imagen, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                    imgChat.setImageBitmap(bitmap)
                }else{
                    tvNombreUsuario.text = "IdUsuario: ${item.idRemitente}"
                }
            } else {
                tvNombreUsuario.text = "IdUsuario: ${item.idRemitente}"
            }

            val sharedPreferences = view.context.getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
            val idCliente = sharedPreferences.getInt("auth_idCliente", 0)
            tvContenido.text = item.mensaje
            tvCreatedData.text = item.createdData.toString()
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
        */
        fun bindItem(item: MensajeFlow, view: View, nombresChat: MutableList<Cliente>) {
            val remitente = nombresChat.find { it.idCliente == item.idRemitente }
            if (remitente != null) {
                if (!remitente.nombreCliente.isNullOrBlank()) {
                    tvNombreUsuario.text = remitente.nombreCliente
                    if (!remitente.imagen.isNullOrEmpty()) {
                        val decodedString = Base64.decode(remitente.imagen, Base64.DEFAULT)
                        val bitmap =
                        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
                        imgChat.setImageBitmap(bitmap)
                    }
                } else {
                    tvNombreUsuario.text = "IdUsuario: ${item.idRemitente}"
                }
            } else {
                tvNombreUsuario.text = "IdUsuario: ${item.idRemitente}"
            }

            val sharedPreferences = view.context.getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
            val idCliente = sharedPreferences.getInt("auth_idCliente", 0)
            tvContenido.text = item.mensaje
            tvCreatedData.text = formatearFecha(item.createdData.toString())

            // Obtener el contenedor del nombre e imagen
            val layoutContainer = itemView.findViewById<RelativeLayout>(R.id.linearLayout)
            val layoutParamsContainer = layoutContainer.layoutParams as RelativeLayout.LayoutParams
            val layoutParamsCard = cardView.layoutParams as RelativeLayout.LayoutParams

            if (item.idRemitente == idCliente) {
                // Mensaje del usuario actual (alinear a la derecha)
                tvContenido.gravity = Gravity.END
                tvNombreUsuario.gravity = Gravity.END
                val colorRemitente: Int = view.context.getColor(R.color.colorPrimary)
                cardView.setCardBackgroundColor(colorRemitente)

                layoutParamsContainer.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParamsContainer.removeRule(RelativeLayout.ALIGN_PARENT_START)
                layoutContainer.layoutParams = layoutParamsContainer

                layoutParamsCard.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParamsCard.removeRule(RelativeLayout.ALIGN_PARENT_START)
                cardView.layoutParams = layoutParamsCard
            } else {
                // Mensaje de otro usuario (alinear a la izquierda)
                val colorPorDefecto: Int = view.context.getColor(R.color.gris)
                cardView.setCardBackgroundColor(colorPorDefecto)
                tvContenido.gravity = Gravity.START
                tvNombreUsuario.gravity = Gravity.START

                layoutParamsContainer.addRule(RelativeLayout.ALIGN_PARENT_START)
                layoutParamsContainer.removeRule(RelativeLayout.ALIGN_PARENT_END)
                layoutContainer.layoutParams = layoutParamsContainer

                layoutParamsCard.addRule(RelativeLayout.ALIGN_PARENT_START)
                layoutParamsCard.removeRule(RelativeLayout.ALIGN_PARENT_END)
                cardView.layoutParams = layoutParamsCard
            }
        }


        fun formatearFecha(fechaTexto: String): String {
            try {
                // Parsear la fecha desde el formato actual
                val formatoEntrada = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
                val fecha = formatoEntrada.parse(fechaTexto)

                // Formatear la fecha al nuevo formato deseado
                val formatoSalida = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                return formatoSalida.format(fecha)
            } catch (e: Exception) {
                e.printStackTrace()
                return ""
            }
        }

    }

}