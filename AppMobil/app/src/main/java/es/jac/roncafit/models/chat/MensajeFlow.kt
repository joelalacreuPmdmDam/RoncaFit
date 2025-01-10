package es.jac.roncafit.models.chat

import java.util.Date

data class MensajeFlow(
    var idRemitente: Int,
    var idDestinatario: Int? = null,
    var infoRemitente: String,
    var mensaje: String,
    var createdData: Date? = null
)
