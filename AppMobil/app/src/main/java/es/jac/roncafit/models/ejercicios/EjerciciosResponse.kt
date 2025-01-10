package es.jac.roncafit.models.ejercicios

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EjerciciosResponse(
    val idEjercicio: Int,
    val nombreEjercicio: String,
    val series: Int,
    val reps: String,
    val descripcion: String,
    val instrucciones: String,
    val urlVideo: String
) : Parcelable