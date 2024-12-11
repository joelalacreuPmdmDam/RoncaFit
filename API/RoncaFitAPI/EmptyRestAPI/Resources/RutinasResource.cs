using EmptyRestAPI.Models;
using Newtonsoft.Json;

namespace EmptyRestAPI.Resources
{
    public class RutinasResource
    {

        public static RutinaObject[]? ObtenerRutinas()
        {
            RutinaObject[]? Rutinas = null;

            string strSQL = "select * from rutinas for json path";
            Dictionary<string, object> Parametros = new Dictionary<string, object> { };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);

            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                Rutinas = JsonConvert.DeserializeObject<RutinaObject[]>(Resultado);
            }
            return Rutinas;
        }
    }
}
