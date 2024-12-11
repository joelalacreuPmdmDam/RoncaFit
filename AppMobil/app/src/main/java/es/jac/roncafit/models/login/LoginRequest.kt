package es.jac.roncafit.models.login

data class LoginRequest(
    val mail: String,
    val contrasenya: String,
    val audience: String
)
