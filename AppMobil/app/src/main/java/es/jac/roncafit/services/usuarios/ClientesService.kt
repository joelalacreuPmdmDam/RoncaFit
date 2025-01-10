package es.jac.roncafit.services.usuarios

import es.jac.roncafit.models.chat.Cliente
import es.jac.roncafit.models.usuarios.ListaClientes
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ClientesService {

    @GET("clientes/obtener")
    suspend fun getClientes(@Header("Authorization") token: String): ListaClientes

    @POST("clientes/perfil-editar")
    suspend fun actualizarPerfil(@Header("Authorization") token: String, @Body cliente: Cliente): Response<Unit>

    @GET("clientes/perfil-obtener/{idCliente}")
    suspend fun obtenerPerfil(@Header("Authorization") token: String, @Path("idCliente") idCliente: Int): Cliente
}