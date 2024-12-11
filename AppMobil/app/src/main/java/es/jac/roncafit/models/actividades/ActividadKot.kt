package es.jac.roncafit.models.actividades

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ActividadKot(
    val id: Int,
    val idActividad: Int,
    val actividad: String,
    val completa: Boolean,
    val fecha: String,
    val inscripciones: Int,
    val limite: Int,
    val idEmpleado: Int,
    val instructor: String
) : Parcelable
