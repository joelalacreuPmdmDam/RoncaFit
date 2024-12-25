package es.jac.roncafit.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import es.jac.roncafit.databinding.FragmentLoginBinding
import es.jac.roncafit.managers.RetrofitObject
import es.jac.roncafit.models.login.LoginRequest
import es.jac.roncafit.models.login.LoginResponse
import es.jac.roncafit.services.login.AuthService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException


class LoginFragment : Fragment() {


    private lateinit var binding: FragmentLoginBinding
    private lateinit var mListener: FragmentLogInListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentLogInListener){
            mListener = context
        }else{
            throw Exception("Your fragment or activity must implement the interface FragmentLogInListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)



        binding.btnLogIn.setOnClickListener {
            val mail = binding.titUser.text.toString()
            val pass = binding.titPass.text.toString()
            //Verificamos si los campos no están vacíos
            if (mail.isEmpty() || pass.isEmpty()) {
                Toast.makeText(requireContext(), "Rellena ambos campos", Toast.LENGTH_SHORT).show()
            } else {
                // Llamar al método login
                login(mail, pass)
            }
        }
        return binding.root
    }

    private fun login(mail: String, password: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Crear la solicitud de login
                val loginRequest = LoginRequest(mail, password, "RoncaFit")
                val apiService = RetrofitObject.getInstance().create(AuthService::class.java)

                // Realizar la solicitud al servidor
                val response = apiService.login(loginRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        loginResponse?.let {
                            Toast.makeText(requireContext(), "Login exitoso. Bienvenido: ${it.nombreUsuario}", Toast.LENGTH_LONG).show()
                            saveToken(it.token, it.nombreUsuario, it.idCliente)
                            mListener.onLogInBtnClicked()
                        } ?: run {
                            // Manejar respuesta vacía del servidor
                            Toast.makeText(requireContext(), "Respuesta vacía del servidor", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Manejar errores HTTP
                        Toast.makeText(
                            requireContext(),
                            "Error en el servidor: ${response.code()} - ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: SocketTimeoutException) {
                // Manejar tiempo de espera agotado
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Tiempo de espera agotado. Verifica tu conexión a internet e inténtalo de nuevo.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            } catch (e: Exception) {
                // Manejar otros errores
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        "Ocurrió un error: ${e.localizedMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }


    private fun saveToken(token: String, nombreUsuario: String, idCliente: Int) {
        val sharedPreferences = requireContext().getSharedPreferences("es.jac.roncafit_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.putString("auth_nombreUsuario", nombreUsuario)
        editor.putInt("auth_idCliente", idCliente)
        editor.apply()
    }

    interface FragmentLogInListener{
        fun onLogInBtnClicked()
    }



}