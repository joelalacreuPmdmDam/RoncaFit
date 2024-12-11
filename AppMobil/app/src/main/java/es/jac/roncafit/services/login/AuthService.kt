package es.jac.roncafit.services.login
import es.jac.roncafit.models.login.LoginRequest
import es.jac.roncafit.models.login.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}