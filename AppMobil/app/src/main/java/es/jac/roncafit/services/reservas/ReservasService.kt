package es.jac.roncafit.services.reservas

import es.jac.roncafit.models.reserva.ReservaRequest
import es.jac.roncafit.models.tablon.TablonResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ReservasService {

    @POST("reservas/insertar")
    suspend fun insertarReserva(@Header("Authorization") token: String,@Body reservaRequest: ReservaRequest): Response<Unit>

    @POST("reservas/eliminar")
    suspend fun eliminarReserva(@Header("Authorization") token: String,@Body reservaRequest: ReservaRequest): Response<Unit>

    @GET("reservas/obtener/{idCliente}")
    suspend fun getReservas(@Header("Authorization") token: String, @Path("idCliente") idCliente: Int): TablonResponse
}