package es.jac.roncafit.models.chat

data class Cliente(
    var idCliente: Int,
    var nombreCliente: String,
    var imagen: String = "",
    var nombre: String = "",
    var apellidos: String = "",
    var mail: String = "",
    var dni: String = ""
)
