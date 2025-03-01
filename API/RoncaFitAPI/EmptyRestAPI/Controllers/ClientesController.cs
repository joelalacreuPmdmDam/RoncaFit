﻿using EmptyRestAPI.Models;
using EmptyRestAPI.Resources;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http.HttpResults;
using Microsoft.AspNetCore.Mvc;
using MySqlX.XDevAPI;

namespace EmptyRestAPI.Controllers
{
    [Route("[controller]")]
    [Produces("application/json")]
    [ApiController]
    [Authorize]
    public class ClientesController : Controller
    {
        [HttpGet("obtener")]
        public IActionResult Get_Clientes()
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "Get_Clientes";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las ocupaciones
                ClienteObject[]? Clientes = ClientesResource.ObtenerClientesInfo();
                if (Clientes == null)
                {
                    LoggerResource.Warning(requestId, Process, "Get_Clientes - Sin datos");
                    Clientes = [];
                }

                // Devolvemos el resultado
                ClienteResponseObject ClienteResponse = new ClienteResponseObject();
                ClienteResponse.Clientes = Clientes;
                LoggerResource.Info(requestId, Process, "Return ClienteResponse");
                return Ok(ClienteResponse);
            }
            catch (Exception ex)
            {
                // Manejar excepciones no previstas
                LoggerResource.Error(requestId, Process, ex.Message);
                return BadRequest(new BadRequestObject() { Mensaje = ex.Message });
            }
        }

        [HttpPost("insertar")]
        public ActionResult InsertarCliente([FromBody] ClienteObject nuevoCliente)
        {
            if (nuevoCliente == null)
            {
                return BadRequest("Cliente inválido.");
            }

            bool resultado = ClientesResource.InsertarCliente(nuevoCliente);
            if (resultado)
            {
                return Ok("Cliente insertado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al insertar el cliente.");
            }
        }

        [HttpPost("editar")]
        public ActionResult ActualizarCliente([FromBody] ClienteObject clienteActualizado)
        {
            if (clienteActualizado == null)
            {
                return BadRequest("Datos de cliente inválidos.");
            }

            bool resultado = ClientesResource.ActualizarCliente(clienteActualizado);
            if (resultado)
            {
                return Ok("Cliente actualizado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar el cliente.");
            }
        }

        // Método DELETE para eliminar un cliente existente
        [HttpPost("eliminar")]
        public ActionResult EliminarCliente([FromBody] ClienteObject clienteEliminar)
        {
            bool resultado = ClientesResource.EliminarCliente(clienteEliminar);
            if (resultado)
            {
                return Ok("Cliente eliminado correctamente"); // 204 No Content
            }
            else
            {
                return StatusCode(500, "Error al eliminar el cliente.");
            }
        }

        [HttpPost("perfil-editar")] //Solo se llama desde la APK
        public ActionResult ActualizarPerfilCliente([FromBody] APKClienteRequest clienteActualizado)
        {
            if (clienteActualizado == null || String.IsNullOrEmpty(clienteActualizado.nombreCliente) || clienteActualizado.idCliente == null)
            {
                return BadRequest("Datos de cliente inválidos.");
            }

            bool resultado = ClientesResource.ActualizarPerfilCliente(clienteActualizado);
            if (resultado)
            {
                return Ok("Perfil del cliente actualizado correctamente");
            }
            else
            {
                return StatusCode(500, "Error al actualizar el perfil del cliente.");
            }
        }

        [HttpGet("perfil-obtener/{idCliente}")]
        public IActionResult GetPerfilCliente([FromRoute] int idCliente)
        {
            string requestId = HttpContext.TraceIdentifier;
            string Process = "GetPerfilCliente";
            try
            {
                LoggerResource.Info(requestId, Process);

                // Obtenemos las ocupaciones
                if (idCliente == null)
                {
                    return BadRequest("Debes pasar el idCliente");
                }
                APKClienteRequest? Cliente = ClientesResource.getPerfilCliente(idCliente);

                if (Cliente.idCliente == null)
                {
                    return NotFound("Cliente no encontrado.");
                }
                return Ok(Cliente);
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
