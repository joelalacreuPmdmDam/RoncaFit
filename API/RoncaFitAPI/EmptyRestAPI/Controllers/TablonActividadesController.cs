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
    public class TablonActividadesController : Controller
    {

        [HttpGet("obtener")]
        public IActionResult Get_TablonActividades()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_TablonActividades";
            try
            {
                LoggerResource.Info(requestId, Process);


                TablonActividadesObject[]? tablonActs = TablonActividadesResource.ObtenerTablonActividadesInfo();
                if (tablonActs == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_TablonActividades - Sin datos");
                    tablonActs = [];
                }

                // Devolvemos el resultado
                TablonActividadesResponseObject tablonActsResponse = new TablonActividadesResponseObject();
                tablonActsResponse.TablonActividades = tablonActs;
                LoggerResource.Info(requestId, Process, "Return tablonActsResponse");
                return Ok(tablonActsResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }

        [HttpPost("insertar")]
        public ActionResult InsertarActTablon([FromBody] TablonActividadesObject nuevaActTablon)
        {
            if (nuevaActTablon == null || nuevaActTablon.idActividad == null ||  nuevaActTablon.fecha == null)
            {
                return BadRequest("ActTablon inválida.");
            }

            bool resultado = TablonActividadesResource.InsertarTablonActividad(nuevaActTablon);
            if (resultado)
            {
                return Ok("ActTablon insertada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al insertar la actTablon.");
            }
        }

        [HttpPost("editar")]
        public ActionResult ActualizarActTablon([FromBody] TablonActividadesObject actTablonActualizada)
        {
            if (actTablonActualizada == null || actTablonActualizada.idEmpleado == null ||  actTablonActualizada.id == null)
            {
                return BadRequest("Datos de la actTablon inválidos.");
            }

            bool resultado = TablonActividadesResource.ActualizarTablonActividad(actTablonActualizada);
            if (resultado)
            {
                return Ok("ActTablon actualizado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar el actTablon.");
            }
        }

        [HttpPost("eliminar")]
        public ActionResult EliminarActividad([FromBody] TablonActividadesObject actTablonEliminar)
        {
            bool resultado = TablonActividadesResource.EliminarTablonActividad(actTablonEliminar);
            if (resultado)
            {
                return Ok("ActTablon eliminada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al eliminar la actividad.");
            }
        }

        [HttpGet("obtener/{idCliente}")]
        public IActionResult Get_TablonActividadesCliente([FromRoute] int idCliente)
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_TablonActividadesCliente";
            try
            {
                LoggerResource.Info(requestId, Process);

                if(idCliente == null || idCliente < 1)
                {
                    return BadRequest("IdCliente erroneo");
                }


                TablonActividadesObject[]? tablonActs = TablonActividadesResource.ObtenerTablonActividadesDispoCliente(idCliente);
                if (tablonActs == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_TablonActividadesCliente - Sin datos");
                    tablonActs = [];
                }

                // Devolvemos el resultado
                TablonActividadesResponseObject tablonActsResponse = new TablonActividadesResponseObject();
                tablonActsResponse.TablonActividades = tablonActs;
                LoggerResource.Info(requestId, Process, "Return tablonActsResponse");
                return Ok(tablonActsResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }
    }
}
