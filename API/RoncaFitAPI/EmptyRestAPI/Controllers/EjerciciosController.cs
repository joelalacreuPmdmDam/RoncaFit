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
    public class EjerciciosController : Controller
    {

        [HttpPost("obtener")]
        public IActionResult Get_Ejercicios([FromBody] EjercicioObject? ejercicio)
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_Ejercicios";
            try
            {
                LoggerResource.Info(requestId, Process);
                EjercicioObject[]? Ejercicios;
                if(ejercicio?.idRutina > 0)
                {
                    Ejercicios = EjerciciosResource.ObtenerEjerciciosRutina(ejercicio?.idRutina);
                }
                else
                {
                    Ejercicios = EjerciciosResource.ObtenerEjercicios();
                }

                // Obtenemos las ocupaciones
                
                if (Ejercicios == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_Ejercicios - Sin datos");
                    Ejercicios = [];
                }

                // Devolvemos el resultado
                EjercicioResponseObject EjerciciosResponse = new EjercicioResponseObject();
                EjerciciosResponse.ejercicios = Ejercicios;
                LoggerResource.Info(requestId, Process, "Return EjerciciosResponse");
                return Ok(EjerciciosResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }

        [HttpGet("obtener-detalle/{idEjercicio}")]
        public IActionResult Get_EjercicioDetalle([FromRoute] int idEjercicio)
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_EjercicioDetalle";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las ocupaciones
                if (idEjercicio == null)
                {
                    return BadRequest("Debes pasar el idEjercicio");
                }
                EjercicioObject? Ejercicio = EjerciciosResource.ObtenerEjercicioPorId(idEjercicio);

                if (Ejercicio.idEjercicio == null)
                {
                    return NotFound("Ejercicio no encontrado.");
                }
                return Ok(Ejercicio);
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
