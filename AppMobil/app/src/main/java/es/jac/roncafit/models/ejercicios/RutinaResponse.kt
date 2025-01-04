package es.jac.roncafit.models.ejercicios

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class RutinaResponse(
    val idRutina: Int,
    val nombreRutina: String,
    val descripcion: String
) : Parcelable
