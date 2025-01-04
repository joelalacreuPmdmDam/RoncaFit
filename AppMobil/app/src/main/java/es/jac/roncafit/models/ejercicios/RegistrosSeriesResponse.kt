package es.jac.roncafit.models.ejercicios

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class RegistrosSeriesResponse(
    val id: Int,
    val idCliente: Int,
    val idRutina: Int,
    val idEjercicio: Int,
    val idSerie: Int,
    var peso: Float,
    var repeticiones: Int,
    val xfec: String
) : Parcelable
