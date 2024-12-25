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
    public class ReservasController : Controller
    {

        [HttpGet("obtener")]
        public IActionResult Get_Reservas()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_Reservas";
            try
            {
                LoggerResource.Info(requestId, Process);


                ReservaObject[]? reservas = ReservasResource.ObtenerReservasInfo();
                if (reservas == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_Reservas - Sin datos");
                    reservas = [];
                }

                // Devolvemos el resultado
                ReservaResponseObject reservasResponse = new ReservaResponseObject();
                reservasResponse.Reservas = reservas;
                LoggerResource.Info(requestId, Process, "Return reservasResponse");
                return Ok(reservasResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }

        [HttpPost("insertar")]
        public ActionResult InsertarReserva([FromBody] ReservaObject nuevaReserva)
        {
            if (nuevaReserva == null || nuevaReserva.idActividadTablon == null || nuevaReserva.idCliente == null)
            {
                return BadRequest("Reserva inválida.");
            }

            // Verificar si el cliente puede realizar la reserva
            bool puedeReservar = ReservasResource.PuedeReservar(nuevaReserva.idCliente.Value, nuevaReserva.idActividadTablon.Value);

            if (!puedeReservar)
            {
                return BadRequest("El cliente ya tiene una reserva en esta fecha y hora.");
            }

            // Si puede reservar, realizar la inserción
            bool resultado = ReservasResource.InsertarReserva(nuevaReserva);
            if (resultado)
            {
                return Ok("Reserva insertada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al insertar la reserva.");
            }
        }

        [HttpPost("editar")]
        public ActionResult ActualizarReserva([FromBody] ReservaObject reservaActualizada)
        {
            if (reservaActualizada == null || reservaActualizada.idCliente == null || reservaActualizada.idReserva == null)
            {
                return BadRequest("Datos de la reserva inválidos.");
            }

            bool resultado = ReservasResource.ActualizarReserva(reservaActualizada);
            if (resultado)
            {
                return Ok("Reserva actualizada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar la reserva.");
            }
        }


        [HttpPost("eliminar")]
        public ActionResult EliminarReserva([FromBody] ReservaObject reserva)
        {
            bool resultado = ReservasResource.EliminarReserva(reserva);
            if (resultado)
            {
                return Ok("Reserva eliminada correctamente");
            }
            else
            {
                return StatusCode(500, "Error al eliminar la reserva.");
            }
        }
    }
}
