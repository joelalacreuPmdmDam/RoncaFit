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
    public class RutinasController : Controller
    {

        [HttpGet("obtener")]
        public IActionResult Get_Rutinas()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_Rutinas";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las ocupaciones
                RutinaObject[]? Rutinas = RutinasResource.ObtenerRutinas();
                if (Rutinas == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_Rutinas - Sin datos");
                    Rutinas = [];
                }

                // Devolvemos el resultado
                RutinaResponseObject RutinasResponse = new RutinaResponseObject();
                RutinasResponse.rutinas = Rutinas;
                LoggerResource.Info(requestId, Process, "Return RutinasResponse");
                return Ok(RutinasResponse);
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
