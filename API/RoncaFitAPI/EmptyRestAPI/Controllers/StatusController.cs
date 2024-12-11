using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using EmptyRestAPI.Models;
using EmptyRestAPI.Resources;

namespace EmptyRestAPI.Controllers
{
    [Route("[controller]")]
    [Produces("application/json")]
    [ApiController]
    public class StatusController : ControllerBase
    {
        // GET
        [HttpGet]
        public IActionResult Get()
        {
            string requestId = HttpContext.TraceIdentifier;
            LoggerResource.Info(requestId, "GET - status");
            StatusResponse status = new StatusResponse();
            return Ok(status);
        }

        // POST
        [HttpPost]
        //[Authorize]
        public IActionResult Post()
        {
            string requestId = HttpContext.TraceIdentifier;
            LoggerResource.Info(requestId, "POST - status");
            StatusResponse status = new StatusResponse();
            return Ok(status);
        }
    }
}
