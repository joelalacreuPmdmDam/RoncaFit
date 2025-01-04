package es.jac.roncafit.services.ejercicios

import es.jac.roncafit.models.ejercicios.EjerciciosRequest
import es.jac.roncafit.models.ejercicios.ListaEjercicios
import es.jac.roncafit.models.ejercicios.ListaRegistrosSeries
import es.jac.roncafit.models.ejercicios.RegistrosSeriesRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface RegistrosSeriesService {

    @POST("registrosSeries/obtener")
    suspend fun getRegistrosSerie(@Header("Authorization") token: String, @Body resgistroSerieRequest: RegistrosSeriesRequest): ListaRegistrosSeries

    @POST("registrosSeries/insertar")
    suspend fun insertarRegistrosSerie(@Header("Authorization") token: String, @Body resgistroSerieRequest: RegistrosSeriesRequest): Response<Unit>

    @POST("registrosSeries/editar")
    suspend fun actualizarRegistrosSerie(@Header("Authorization") token: String, @Body resgistroSerieRequest: RegistrosSeriesRequest): Response<Unit>

    @POST("registrosSeries/eliminar")
    suspend fun eliminarRegistrosSerie(@Header("Authorization") token: String, @Body resgistroSerieRequest: RegistrosSeriesRequest): Response<Unit>
}