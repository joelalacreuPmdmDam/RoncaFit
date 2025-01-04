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
    public class RegistrosSeriesController : Controller
    {

        [HttpPost("obtener")]
        public IActionResult Get_RegistrosSeries([FromBody] RegistroSerieObject? registroRequest)
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_RegistrosSeries";
            try
            {
                LoggerResource.Info(requestId, Process);
                RegistroSerieObject[]? registrosSeries;
                if (registroRequest.idCliente == null || registroRequest.idEjercicio == null)
                {
                    return BadRequest();
                }
                else
                {
                    registrosSeries = RegistrosSeriesResource.ObtenerRegistrosSeries(registroRequest);
                }

                if (registrosSeries == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_RegistrosSeries - Sin datos");
                    registrosSeries = [];
                }

                // Devolvemos el resultado
                RegistroSerieResponseObject registrosResponse = new RegistroSerieResponseObject();
                registrosResponse.registrosSeries = registrosSeries;
                LoggerResource.Info(requestId, Process, "Return registrosResponse");
                return Ok(registrosResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return StatusCode(500,"Error del servidor: " + ex.Message);
            }
        }


        [HttpPost("insertar")]
        public ActionResult InsertarRegistroSerie([FromBody] RegistroSerieObject nuevoRegistroSerie)
        {
            if (nuevoRegistroSerie == null || nuevoRegistroSerie.idSerie == null || nuevoRegistroSerie.peso == null || nuevoRegistroSerie.repeticiones == null || nuevoRegistroSerie.idCliente == null | nuevoRegistroSerie.idEjercicio == null)
            {
                return BadRequest("Nuevo registroSerie inválido.");
            }

            bool resultado = RegistrosSeriesResource.InsertarRegistroSerie(nuevoRegistroSerie);
            if (resultado)
            {
                return Ok("Registro insertado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al insertar el registro.");
            }
        }

        [HttpPost("editar")]
        public ActionResult ActualizarRegistroSerie([FromBody] RegistroSerieObject registroSerieActualizado)
        {
            if (registroSerieActualizado == null || registroSerieActualizado.id == null || registroSerieActualizado.peso == null || registroSerieActualizado.repeticiones == null )
            {
                return BadRequest("Datos del registro inválidos.");
            }

            bool resultado = RegistrosSeriesResource.ActualizarRegistroSerie(registroSerieActualizado);
            if (resultado)
            {
                return Ok("Registro actualizado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar el registro.");
            }
        }

        // Método DELETE para eliminar un cliente existente
        [HttpPost("eliminar")]
        public ActionResult EliminarRegistroSerie([FromBody] RegistroSerieObject registroSerieEliminar)
        {

            if (registroSerieEliminar == null || registroSerieEliminar.id == null)
            {
                return BadRequest("Datos del registro inválidos.");
            }

            bool resultado = RegistrosSeriesResource.EliminarRegistroSerie(registroSerieEliminar);
            if (resultado)
            {
                return Ok("Registro eliminado correctamente"); 
            }
            else
            {
                return StatusCode(500, "Error al eliminar el registro.");
            }
        }

    }
}
