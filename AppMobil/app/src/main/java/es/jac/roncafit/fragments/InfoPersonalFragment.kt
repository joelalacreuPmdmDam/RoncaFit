package es.jac.roncafit.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.jac.roncafit.databinding.FragmentInfoPersonalBinding
import es.jac.roncafit.managers.FirestoreChatManager
import es.jac.roncafit.models.chat.Cliente
import es.jac.roncafit.models.usuarios.ClienteResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.util.Log
import androidx.core.content.ContextCompat
import es.jac.roncafit.R
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.ejercicios.EjerciciosRequest
import es.jac.roncafit.services.ejercicios.EjerciciosService
import es.jac.roncafit.services.usuarios.ClientesService
import kotlinx.coroutines.async


class InfoPersonalFragment : Fragment() {

    private lateinit var binding: FragmentInfoPersonalBinding
    private val PICK_IMAGE_REQUEST = 1
    private var imgBase64: String = ""
    private var idClientePasado: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoPersonalBinding.inflate(inflater,container,false)

        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val nombreCliente = sharedPreferences.getString("auth_nombreUsuario", "")

        // Obtener los datos del Bundle
        val idCliente = arguments?.getInt("idCliente")
        if(idCliente != null){
            idClientePasado = idCliente
        }
        obtenerPerfil()

        binding.btnCambiarFotoPerfil.setOnClickListener {
            openGallery()
        }

        binding.btnGuardarCambios.setOnClickListener {
            val nombreUsuario = binding.titUser.text?.toString() ?: ""
            updateName(nombreUsuario,imgBase64)
        }

        binding.btnClear.setOnClickListener {
            binding.imgFotoPerfil.setImageResource(R.drawable.ic_default_profile)
            binding.btnClear.visibility = View.INVISIBLE
            imgBase64 = ""
        }

        return binding.root
    }


    private fun obtenerPerfil() {
        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(ClientesService::class.java)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val perfilCliente = apiService.obtenerPerfil("Bearer $token",idClientePasado)
                withContext(Dispatchers.Main) {
                    crearPantalla(perfilCliente)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    fun crearPantalla(cliente: Cliente){
        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val idCliente = sharedPreferences.getInt("auth_idCliente", 0)
        binding.titUser.setText(cliente.nombreCliente)
        if (!cliente.imagen.isNullOrEmpty()){
            setBase64ToImageView(cliente.imagen)
            binding.btnClear.visibility = View.VISIBLE
        }else{
            binding.btnClear.visibility = View.INVISIBLE
        }

        binding.tvNombreApellidos.text = "${cliente.nombre} ${cliente.apellidos}"
        if(idClientePasado != idCliente){ //Si el idClientePasado no es nuestro ID, solo mostramos la información que el resto de usuarios pueda ver
            binding.tvMail.visibility = View.INVISIBLE
            binding.tvTituloMail.visibility = View.INVISIBLE
            binding.tvDni.visibility = View.INVISIBLE
            binding.tvTituloDni.visibility = View.INVISIBLE
            binding.titUser.apply {
                isFocusable = false
            }
            binding.btnGuardarCambios.visibility = View.INVISIBLE
            binding.btnCambiarFotoPerfil.visibility = View.INVISIBLE
        }else{
            binding.tvMail.visibility = View.VISIBLE
            binding.tvDni.visibility = View.VISIBLE
            binding.tvTituloMail.visibility = View.VISIBLE
            binding.tvTituloDni.visibility = View.VISIBLE
            binding.tvMail.text = cliente.mail
            binding.tvDni.text = cliente.dni

        }
    }

    fun updateName(nombreUsuario: String, imgBase64: String) {
        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val idCliente = sharedPreferences.getInt("auth_idCliente", 0)
        val cliente = Cliente(idCliente = idCliente, nombreCliente = nombreUsuario, imagen = imgBase64)

        val token = sharedPreferences.getString("auth_token", null)
        if (token == null) {
            Toast.makeText(requireContext(), "Token no disponible", Toast.LENGTH_SHORT).show()
            return
        }
        val apiService = RetrofitObject.getInstance().create(ClientesService::class.java)

        lifecycleScope.launch {
            val firestoreJob = async(Dispatchers.IO) {
                try {
                    FirestoreChatManager().updateNombreChat(cliente)
                    1
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error en la actualización Firestore: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    0
                }
            }

            val apiJob = async(Dispatchers.IO) {
                try {
                    val response = apiService.actualizarPerfil("Bearer $token", cliente)
                    if (response.isSuccessful) {
                        1
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                        }
                        0
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error en la solicitud: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                    0
                }
            }

            val actualizado = firestoreJob.await() + apiJob.await()
            withContext(Dispatchers.Main) {
                if (actualizado == 2){
                    Toast.makeText(requireContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(requireContext(), "Los datos no se actualizaron correctamente", Toast.LENGTH_SHORT).show()
                }
                if (!imgBase64.isNullOrEmpty()) {
                    binding.btnClear.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), PICK_IMAGE_REQUEST)
    }

    //Manejar el resultado de la selección de imagen
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            if (selectedImageUri != null) {
                binding.imgFotoPerfil.setImageURI(selectedImageUri)
                val imageBytes = uriToBytes(selectedImageUri)

                if (imageBytes != null) {
                    imgBase64 = bytesToBase64(imageBytes!!)
                } else {
                    Toast.makeText(requireContext(), "Error al convertir la imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uriToBytes(uri: Uri): ByteArray? {
        return try {
            val contentResolver = requireContext().contentResolver
            val inputStream = contentResolver.openInputStream(uri)
            val byteBuffer = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len: Int

            while (inputStream?.read(buffer).also { len = it ?: -1 } != -1) {
                byteBuffer.write(buffer, 0, len)
            }
            inputStream?.close()
            byteBuffer.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun bytesToBase64(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    private fun setBase64ToImageView(base64String: String) {
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        binding.imgFotoPerfil.setImageBitmap(bitmap)
    }

    companion object {
        fun newInstance(idCliente: Int): InfoPersonalFragment {
            val fragment = InfoPersonalFragment()
            val args = Bundle().apply {
                putInt("idCliente", idCliente)
            }
            fragment.arguments = args
            return fragment
        }
    }
}