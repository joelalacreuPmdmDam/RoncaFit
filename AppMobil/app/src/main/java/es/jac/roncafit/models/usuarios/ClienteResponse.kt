package es.jac.roncafit.models.usuarios

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClienteResponse(
    val idCliente: Int? = null,
    val nombre: String = "",
    val apellidos: String = "",
    val nombreUsuario: String = ""
) : Parcelable
