package es.jac.roncafit.fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import es.jac.roncafit.adapters.MensajesAdapter
import es.jac.roncafit.databinding.FragmentChatBinding
import es.jac.roncafit.managers.FirestoreChatManager
import es.jac.roncafit.models.chat.Cliente
import es.jac.roncafit.models.chat.MensajeFlow
import es.jac.roncafit.models.usuarios.ClienteResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var clientePasado: ClienteResponse
    private lateinit var mensajesLista: MutableList<MensajeFlow>
    private lateinit var nombresChat: MutableList<Cliente>
    private lateinit var mAdapter: MensajesAdapter
    private lateinit var mListener: ChatFragmenListener
    private val firestoreManager: FirestoreChatManager by lazy { FirestoreChatManager() }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ChatFragmenListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface ChatFragmenListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val idCliente = sharedPreferences.getInt("auth_idCliente", 0)
        var userName = sharedPreferences.getString("auth_nombreUsuario", null)
        if (userName.isNullOrEmpty()){
            userName = "Not found"
        }

        binding.toolbarChat.setOnClickListener {
            if (!binding.tvNombreChat.text.equals("CHAT GLOBAL")){
                mListener.onToolbarClicked(clientePasado.idCliente?: idCliente)
            }

        }

        // Obtener los datos del Bundle
        val cliente = arguments?.getParcelable<ClienteResponse>("cliente")
        if (cliente != null){
            clientePasado = cliente
            binding.tvNombreChat.text = cliente.nombreUsuario
            //Toast.makeText(requireContext(),"IdCliente ${cliente.idCliente}", Toast.LENGTH_SHORT).show()
        }else{
            clientePasado = ClienteResponse(idCliente = 0)
            binding.tvNombreChat.text = "CHAT GLOBAL"
            //Toast.makeText(requireContext(),"IdCliente nulo", Toast.LENGTH_SHORT).show()
        }



        binding.btnEnviar.setOnClickListener{
            var mensaje = binding.etMensaje.text.toString()
            if (!mensaje.isNullOrBlank()){
                lifecycleScope.launch(Dispatchers.IO) {
                    firestoreManager.addMensaje(MensajeFlow(mensaje = mensaje, idDestinatario = clientePasado.idCliente, idRemitente = idCliente, infoRemitente = userName))
                    withContext(Dispatchers.Main){
                        binding.etMensaje.text.clear()
                        if(mensajesLista.size>1){
                            binding.recyclerMensajes.smoothScrollToPosition(mensajesLista.size-1)
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                }
            }

        }
        setRecyclerView()


        return binding.root
    }

    private fun setRecyclerView(){
        mensajesLista = mutableListOf()
        nombresChat = mutableListOf()
        binding.recyclerMensajes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mAdapter = MensajesAdapter(requireContext(), mensajesLista, nombresChat)
        binding.recyclerMensajes.adapter = mAdapter
        lifecycleScope.launch (Dispatchers.IO){
            getNombreChats()
            firestoreManager.getMensajesFlow(clientePasado.idCliente)
                .collect{ mensajes ->
                    mensajesLista.clear()
                    mensajesLista.addAll(mensajes)
                    withContext(Dispatchers.Main){
                        mAdapter.notifyDataSetChanged()
                        binding.recyclerMensajes.scrollToPosition(mensajesLista.size-1)
                    }
                }
        }
    }


    private fun getNombreChats(){
        lifecycleScope.launch (Dispatchers.IO){
            firestoreManager.getNombresChat()
                .collect{ nombresUpdated ->
                    nombresChat.clear()
                    nombresChat.addAll(nombresUpdated)
                    withContext(Dispatchers.Main){
                        mAdapter.notifyDataSetChanged()
                        binding.recyclerMensajes.scrollToPosition(mensajesLista.size-1)
                    }
                }

        }
    }

    private fun setBase64ToImageView(base64String: String) {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.imgChat.setImageBitmap(bitmap)
    }

    interface ChatFragmenListener{
        fun onToolbarClicked(idCliente: Int)
    }

    companion object {
        fun newInstance(cliente: ClienteResponse): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle().apply {
                putParcelable("cliente", cliente)
            }
            fragment.arguments = args
            return fragment
        }
    }
}