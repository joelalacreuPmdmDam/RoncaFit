package es.jac.roncafit.models.actividades

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class ActividadResponse(
    val id: Int,
    val idActividad: Int,
    val actividad: String,
    val completa: Boolean,
    val fecha: String,
    val inscripciones: Int,
    val limite: Int,
    val idEmpleado: Int,
    val instructor: String,
    val idReserva: Int
) : Parcelable
