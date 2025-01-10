package es.jac.roncafit.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import es.jac.roncafit.adapters.ClientesAdapter
import es.jac.roncafit.databinding.FragmentClientesChatBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.usuarios.ClienteResponse
import es.jac.roncafit.services.usuarios.ClientesService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ClientesChatFragment : Fragment() {

    private lateinit var binding: FragmentClientesChatBinding
    private lateinit var mAdapter: ClientesAdapter
    private lateinit var listaClientesCompleta: MutableList<ClienteResponse>
    private lateinit var listaClientesFiltrada: MutableList<ClienteResponse>
    private lateinit var mListener: ClientesChatFragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ClientesChatFragmentListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface ClientesChatFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClientesChatBinding.inflate(inflater,container,false)

        setUpRecyclerView()
        obtenerClientes()
        setUpSearchView()

        binding.btnChatGlobal.setOnClickListener {
            mListener.onChatGlobalBtnPressed()
        }

        return binding.root
    }

    private fun setUpRecyclerView() {
        listaClientesCompleta = mutableListOf()
        listaClientesFiltrada = mutableListOf()
        mAdapter = ClientesAdapter(listaClientesCompleta) { cliente ->
            mListener.onClienteClicked(cliente)
        }
        binding.recyclerClientes.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
        )
        binding.recyclerClientes.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerClientes.adapter = mAdapter
    }

    private fun obtenerClientes() {
        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(ClientesService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val listaClientes = apiService.getClientes("Bearer $token")
                withContext(Dispatchers.Main) {
                    listaClientesCompleta.addAll(listaClientes.clientes)
                    listaClientesFiltrada.addAll(listaClientes.clientes)
                    mAdapter.updateData(listaClientes.clientes)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filtrarLista(newText.orEmpty())
                return true
            }
        })
    }

    private fun filtrarLista(texto: String) {
        val nuevaLista: MutableList<ClienteResponse>
        if (texto.isEmpty()) {
            nuevaLista = listaClientesCompleta
        } else {
            nuevaLista = listaClientesCompleta.filter { cliente ->
                cliente.nombre.contains(texto, ignoreCase = true)
            }.toMutableList()
        }
        listaClientesFiltrada.clear()
        listaClientesFiltrada.addAll(nuevaLista)
        mAdapter.updateData(listaClientesFiltrada)
    }

    interface ClientesChatFragmentListener{
        fun onClienteClicked(cliente: ClienteResponse)
        fun onChatGlobalBtnPressed()
    }


}