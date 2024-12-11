using EmptyRestAPI.Models;
using EmptyRestAPI.Resources;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using Org.BouncyCastle.Asn1.Ocsp;
using System.Diagnostics;

namespace EmptyRestAPI.Controllers
{
    [Route("[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class InstructoresController : Controller
    {
        [HttpGet("obtener")]
        public IActionResult Get_InstructoresInfo()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_InstructoresInfo";
            try
            {
                LoggerResource.Info(requestId, Process);

                InstructorObject[]? Instructores = InstructoresResource.ObtenerInstructoresInfo();
                if (Instructores == null)
                {
                    LoggerResource.Warning(requestId, Process, "Instructores Info - Sin datos");
                    Instructores = [];
                }

                // Devolvemos el resultado
                InstructorResponseObject InstructoresResponse = new InstructorResponseObject();
                InstructoresResponse.Instructores = Instructores;
                LoggerResource.Info(requestId, Process, "Return InstructoresResponse");
                return Ok(InstructoresResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }


        [HttpGet("{idEmpleado}/actividades")]
        public IActionResult Get_ActividadesByInstructor(int idEmpleado)
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = $"Get_ActividadesByInstructor_{idEmpleado}";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las actividades del instructor
                ActividadObject[]? Actividades = InstructoresResource.GetInstructoresActs(idEmpleado);
                if (Actividades == null)
                {
                    LoggerResource.Warning(requestId, Process, "Actividades Info - Sin datos");
                    Actividades = [];
                }

                // Devolvemos el resultado
                ActividadResponseObject ActividadesResponse = new ActividadResponseObject();
                ActividadesResponse.Actividades = Actividades;
                LoggerResource.Info(requestId, Process, "Return ActividadesResponse");
                return Ok(ActividadesResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject { Mensaje = ex.Message });
            }
        }

        [HttpPost("{idEmpleado}/actividades")]
        public IActionResult Editar_InstructorActs(int idEmpleado, [FromBody] ActividadObject[] nuevasActividades)
        {
            if (nuevasActividades == null)
            {
                return BadRequest("Datos de las actividades inválidas.");
            }

            bool resultado = InstructoresResource.ActualizarActsInstructor(idEmpleado,nuevasActividades);
            if (resultado)
            {
                return Ok("Actividades del instructor actualizadas correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar las actividades  del instructor.");
            }
        }

        [HttpPost("disponibles")]
        public IActionResult Get_InstructoresDisponibles([FromBody] TablonActividadesObject actTablon)
        {

            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_InstructoresDisponibles";
            try
            {
                if (actTablon == null || actTablon.idActividad == null || actTablon.fecha == null)
                {
                    return BadRequest("Datos de las actividades inválidas.");
                }

                InstructorObject[]? Instructores = InstructoresResource.GetInstructoresDisponibles(actTablon);
                if (Instructores == null)
                {
                    LoggerResource.Warning(requestId, Process, "Instructores Info - Sin datos");
                    Instructores = [];
                }

                // Devolvemos el resultado
                InstructorResponseObject InstructoresResponse = new InstructorResponseObject();
                InstructoresResponse.Instructores = Instructores;
                LoggerResource.Info(requestId, Process, "Return InstructoresResponse");
                return Ok(InstructoresResponse);
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
