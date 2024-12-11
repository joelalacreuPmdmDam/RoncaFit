using EmptyRestAPI.Models;
using EmptyRestAPI.Resources;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;

namespace EmptyRestAPI.Controllers
{
    [Route("[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class EmpleadosController : Controller
    {
        [HttpGet("obtener")]
        public IActionResult Get_Empleados()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_Empleados";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las ocupaciones
                EmpleadoObject[]? Empleados = EmpleadosResource.ObtenerEmpleadosInfo();
                if (Empleados == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_Empleados - Sin datos");
                    Empleados = [];
                }

                // Devolvemos el resultado
                EmpleadoResponseObject EmpleadoResponse = new EmpleadoResponseObject();
                EmpleadoResponse.Empleados = Empleados;
                LoggerResource.Info(requestId, Process, "Return EmpleadoResponse");
                return Ok(EmpleadoResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }

        [HttpPost("insertar")]
        public ActionResult InsertarEmpleado([FromBody] EmpleadoObject nuevoEmpleado)
        {
            if (nuevoEmpleado == null)
            {
                return BadRequest("Empleado inválido.");
            }

            bool resultado = EmpleadosResource.InsertarEmpleado(nuevoEmpleado);
            if (resultado)
            {
                return Ok("Empleado insertado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al insertar el empleado.");
            }
        }

        [HttpPost("editar")]
        public ActionResult ActualizarEmpleado([FromBody] EmpleadoObject empleadoActualizado)
        {
            if (empleadoActualizado == null)
            {
                return BadRequest("Datos de empleado inválidos.");
            }

            bool resultado = EmpleadosResource.ActualizarEmpleado(empleadoActualizado);
            if (resultado)
            {
                return Ok("Empleado actualizado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar el empleado.");
            }
        }

        // Método DELETE para eliminar un cliente existente
        [HttpPost("eliminar")]
        public ActionResult EliminarEmpleado([FromBody] EmpleadoObject empleadoEliminar)
        {
            bool resultado = EmpleadosResource.EliminarEmpleado(empleadoEliminar);
            if (resultado)
            {
                return Ok("Empleado eliminado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al eliminar el empleado.");
            }
        }

    }
}
