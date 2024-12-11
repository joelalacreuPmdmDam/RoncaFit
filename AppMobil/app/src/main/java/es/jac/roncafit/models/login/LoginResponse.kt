package es.jac.roncafit.models.login

data class LoginResponse(
    val token: String,
    val nombreUsuario: String,
    val idCliente: Int
)
