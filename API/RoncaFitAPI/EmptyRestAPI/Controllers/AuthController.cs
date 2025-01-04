using EmptyRestAPI.Models;
using EmptyRestAPI.Resources;
using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using System.Text;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;

namespace EmptyRestAPI.Controllers
{
    [Route("[controller]")]
    [ApiController]
    public class AuthController : Controller
    {
        [HttpPost("login")]
        public IActionResult Login([FromBody] LoginObject login)
        {
            login.mail = login.mail != null ? login.mail : "";
            //login.nombreUsuario = login.nombreUsuario != null ? login.nombreUsuario : "";
            login.contrasenya = login.contrasenya != null ? login.contrasenya : "";
            var cliente = LoginResource.VerificarCredenciales(login.mail, login.contrasenya);

            if (cliente != null && login.audience == "RoncaFit")
            {
                var token = GenerateJwtToken(cliente.dni,login.audience);
                return Ok(new { token, cliente.nombreUsuario, cliente.idCliente });
            }
            return Unauthorized();
        }

        private string GenerateJwtToken(string dni, string audiencia)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("ZDAfOFKPPGsf5E4L6YqnpHkRvJ2N3P8K"));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);

            var claims = new[]
            {
                new Claim(JwtRegisteredClaimNames.Sub, dni),
                new Claim(JwtRegisteredClaimNames.Aud, audiencia),
                new Claim(JwtRegisteredClaimNames.Jti, Guid.NewGuid().ToString())
            };

            var token = new JwtSecurityToken(
                issuer: audiencia+"AuthSystem",
                audience: audiencia,
                claims: claims,
                expires: DateTime.Now.AddMinutes(240),
                signingCredentials: credentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }
    }
}
