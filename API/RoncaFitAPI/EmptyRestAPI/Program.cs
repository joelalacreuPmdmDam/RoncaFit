using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.
builder.Services.AddControllers();

// Configurar cache en memoria
builder.Services.AddMemoryCache();

// Agregar servicios CORS para permitir cualquier origen
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAnyOrigin", policy =>
    {
        policy.AllowAnyOrigin()
              .AllowAnyMethod()
              .AllowAnyHeader()
              .WithExposedHeaders("Content-Disposition");
    });
});

// Configuración de la autenticación JWT
builder.Services.AddAuthentication(JwtBearerDefaults.AuthenticationScheme)
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            ValidIssuer = "RoncaFitAuthSystem",
            ValidAudience = "RoncaFit",
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes("ZDAfOFKPPGsf5E4L6YqnpHkRvJ2N3P8K"))
        };
        options.RequireHttpsMetadata = false; // Para desarrollo, cambiar a true en producción
    });

// Modificamos para que no incluya las claves con null al devolver un JSON
builder.Services.AddControllers().AddJsonOptions(options =>
{
    options.JsonSerializerOptions.DefaultIgnoreCondition = System.Text.Json.Serialization.JsonIgnoreCondition.WhenWritingNull;
});

var app = builder.Build();

app.Urls.Add("http://*:5000");

// Configure the HTTP request pipeline.
app.UseCors("AllowAnyOrigin");

// Añadir autenticación y autorización
app.UseAuthentication();
app.UseAuthorization();

app.MapControllers();

app.Run();
