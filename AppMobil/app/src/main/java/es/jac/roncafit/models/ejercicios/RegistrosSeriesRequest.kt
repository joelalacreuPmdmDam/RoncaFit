package es.jac.roncafit.models.ejercicios

data class RegistrosSeriesRequest(
    val id: Int? = null,
    val idCliente: Int? = null,
    val idRutina: Int? = null,
    val idEjercicio: Int? = null,
    val idSerie: Int? = null,
    val peso: Float? = null,
    val repeticiones: Int? = null
)
