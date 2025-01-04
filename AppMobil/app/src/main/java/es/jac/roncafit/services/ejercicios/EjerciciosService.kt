package es.jac.roncafit.services.ejercicios

import es.jac.roncafit.models.ejercicios.EjerciciosRequest
import es.jac.roncafit.models.ejercicios.EjerciciosResponse
import es.jac.roncafit.models.ejercicios.ListaEjercicios
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface EjerciciosService {

    @POST("ejercicios/obtener")
    suspend fun getEjercicios(@Header("Authorization") token: String,@Body reservaRequest: EjerciciosRequest): ListaEjercicios
}