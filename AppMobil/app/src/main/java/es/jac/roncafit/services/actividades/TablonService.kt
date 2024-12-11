package es.jac.roncafit.services.actividades

import es.jac.roncafit.models.login.LoginRequest
import es.jac.roncafit.models.tablon.TablonResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TablonService {

    @GET("tablonActividades/obtener")
    suspend fun getTablon(@Header("Authorization") token: String): TablonResponse
}