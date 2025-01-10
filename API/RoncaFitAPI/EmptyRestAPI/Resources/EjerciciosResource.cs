using EmptyRestAPI.Models;
using Newtonsoft.Json;

namespace EmptyRestAPI.Resources
{
    public class EjerciciosResource
    {

        public static EjercicioObject[]? ObtenerEjercicios()
        {
            EjercicioObject[]? Ejercicios = null;

            string strSQL = "select * from ejercicios for json path";
            Dictionary<string, object> Parametros = new Dictionary<string, object> { };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);

            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                Ejercicios = JsonConvert.DeserializeObject<EjercicioObject[]>(Resultado);
            }
            return Ejercicios;
        }

        public static EjercicioObject[]? ObtenerEjerciciosRutina(int? idRutina)
        {
            EjercicioObject[]? Ejercicios = null;

            string strSQL = @"select e.* from ejercicios e
                            inner join rutinasEjercicios r on r.idEjercicio=e.idEjercicio
                            where r.idRutina = @idRutina
                            for json path";
            Dictionary<string, object> Parametros = new Dictionary<string, object>
            {
                { "@idRutina", idRutina }
            };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);

            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                Ejercicios = JsonConvert.DeserializeObject<EjercicioObject[]>(Resultado);
            }
            return Ejercicios;
        }

        public static EjercicioObject ObtenerEjercicioPorId(int idEjercicio)
        {
            EjercicioObject Ejercicio = new EjercicioObject();

            string strSQL = "select * from ejercicios for json path";
            Dictionary<string, object> Parametros = new Dictionary<string, object> { };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);

            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                EjercicioObject[]? Ejercicios = JsonConvert.DeserializeObject<EjercicioObject[]>(Resultado);
                Ejercicio = Ejercicios[0];

            }
            return Ejercicio;
        }
    }
}
