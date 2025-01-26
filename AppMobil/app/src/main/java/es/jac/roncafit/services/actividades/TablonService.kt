package es.jac.roncafit.services.actividades

import es.jac.roncafit.models.tablon.TablonResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface TablonService {

    @GET("tablonActividades/obtener/{idCliente}")
    suspend fun getTablon(@Header("Authorization") token: String, @Path("idCliente") idCliente: Int): TablonResponse
}