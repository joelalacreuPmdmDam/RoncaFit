package es.jac.roncafit.models.tablon

import es.jac.roncafit.models.actividades.ActividadResponse

data class TablonResponse(
    val tablonActividades: List<ActividadResponse>
)
