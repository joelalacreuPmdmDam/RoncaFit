package es.jac.roncafit.services.ejercicios

import es.jac.roncafit.models.ejercicios.ListaRutinas
import retrofit2.http.GET
import retrofit2.http.Header

interface RutinasService {

    @GET("rutinas/obtener")
    suspend fun getRutinas(@Header("Authorization") token: String): ListaRutinas
}