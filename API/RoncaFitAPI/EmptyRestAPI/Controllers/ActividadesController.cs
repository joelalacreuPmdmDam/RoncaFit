using EmptyRestAPI.Models;
using EmptyRestAPI.Resources;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;

namespace EmptyRestAPI.Controllers
{
    [Route("[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class ActividadesController : Controller
    {

        [HttpGet("obtener")]
        public IActionResult Get_Actividades()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_Actividades";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las ocupaciones
                ActividadObject[]? Actividades = ActividadesResource.ObtenerActividadesInfo();
                if (Actividades == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_Actividades - Sin datos");
                    Actividades = [];
                }

                // Devolvemos el resultado
                ActividadResponseObject ActividadResponse = new ActividadResponseObject();
                ActividadResponse.Actividades = Actividades;
                LoggerResource.Info(requestId, Process, "Return ActividadResponse");
                return Ok(ActividadResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }

        [HttpPost("insertar")]
        public ActionResult InsertarActividad([FromBody] ActividadObject nuevaActividad)
        {
            if (nuevaActividad == null || string.IsNullOrEmpty(nuevaActividad.actividad))
            {
                return BadRequest("Actividad inválida.");
            }

            bool resultado = ActividadesResource.InsertarActividad(nuevaActividad);
            if (resultado)
            {
                return Ok("Actividad insertada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al insertar la actividad.");
            }
        }

        [HttpPost("editar")]
        public ActionResult ActualizarActividad([FromBody] ActividadObject actividadActualizada)
        {
            if (actividadActualizada == null || actividadActualizada.idActividad == null || string.IsNullOrEmpty(actividadActualizada.actividad))
            {
                return BadRequest("Datos de la actividad inválidos.");
            }

            bool resultado = ActividadesResource.ActualizarActividad(actividadActualizada);
            if (resultado)
            {
                return Ok("Actividad actualizada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar la actividad.");
            }
        }

        [HttpPost("eliminar")]
        public ActionResult EliminarActividad([FromBody] ActividadObject actividadEliminar)
        {
            bool resultado = ActividadesResource.EliminarActividad(actividadEliminar);
            if (resultado)
            {
                return Ok("Actividad eliminada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al eliminar la actividad.");
            }
        }
    }
}
