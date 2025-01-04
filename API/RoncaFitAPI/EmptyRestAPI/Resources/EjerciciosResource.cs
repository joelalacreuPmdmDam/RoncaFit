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

        public static Boolean InsertarSerieEjercicio(SerieObject serie)
        {
            /*
            string strSQL = @"INSERT INTO usuariosRutinas (idCliente,idRutina,idEjercicio,idSerie,peso,repeticiones,xfec) 
                            VALUES (@idCliente,@idRutina,@idEjercicio,@idSerie,@peso,@repeticiones,GETDATE())";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@dni", serie.dni ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombre", serie.nombre ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@apellidos", serie.apellidos ?? (object)DBNull.Value);

                        int rowsAffected = command.ExecuteNonQuery();
                        return rowsAffected > 0;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
                return false;
            }
            */
            return false;
        }
    }
}
