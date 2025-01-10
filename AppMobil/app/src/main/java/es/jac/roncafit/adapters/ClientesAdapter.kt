package es.jac.roncafit.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.jac.roncafit.R
import es.jac.roncafit.adapters.EjerciciosAdapter.EjerciciosViewHolder
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.usuarios.ClienteResponse

class ClientesAdapter (
    private var clientesList: MutableList<ClienteResponse>,
    private val onItemClickListener: (ClienteResponse) -> Unit
) : RecyclerView.Adapter<ClientesAdapter.ClientesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClientesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ClientesViewHolder(layoutInflater.inflate(R.layout.cliente_chat_item, parent, false))
    }

    override fun getItemCount(): Int = clientesList.size

    override fun onBindViewHolder(holder: ClientesViewHolder, position: Int) {
        val item = clientesList[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClickListener(item)
        }
    }

    fun updateData(newClientesList: List<ClienteResponse>) {
        clientesList = newClientesList.toMutableList()
        notifyDataSetChanged()
    }

    class ClientesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tv_infoCliente = view.findViewById<TextView>(R.id.tv_infoCliente)

        fun bind(item: ClienteResponse) {
            tv_infoCliente.text = "${item.idCliente} - ${item.nombre} ${item.apellidos}"
        }
    }
}