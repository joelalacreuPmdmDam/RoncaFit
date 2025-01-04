using EmptyRestAPI.Models;
using Newtonsoft.Json;

namespace EmptyRestAPI.Resources
{
    public class RegistrosSeriesResource
    {
        public static RegistroSerieObject[]? ObtenerRegistrosSeries(RegistroSerieObject registroRequest)
        {
            RegistroSerieObject[]? registrosSeries = null;

            string strSQL = @"select *
                            from usuariosRutinas 
                            where idCliente=@idCliente
                            and idEjercicio=@idEjercicio
                            and convert(date,xfec)=convert(date,getdate())
                            order by idSerie asc
                            for json path";
            Dictionary<string, object> Parametros = new Dictionary<string, object> 
            { 
                { "@idCliente", registroRequest.idCliente },
                { "@idEjercicio", registroRequest.idEjercicio } 
            };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);

            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                registrosSeries = JsonConvert.DeserializeObject<RegistroSerieObject[]>(Resultado);
            }
            return registrosSeries;
        }

        public static bool InsertarRegistroSerie(RegistroSerieObject registro)
        {
            string strSQL = @"insert into usuariosRutinas (idCliente,idRutina,idEjercicio,idSerie,peso,repeticiones,xfec)
                            values(@idCliente,@idRutina,@idEjercicio,@idSerie,@peso,@repeticiones,getdate())";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idCliente", registro.idCliente ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@idRutina", registro.idRutina ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@idEjercicio", registro.idEjercicio ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@idSerie", registro.idSerie ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@peso", registro.peso ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@repeticiones", registro.repeticiones ?? (object)DBNull.Value);

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
        }

        public static bool ActualizarRegistroSerie(RegistroSerieObject registro)
        {
            string strSQL = "update usuariosRutinas set peso=@peso,repeticiones=@repeticiones where id=@id";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@id", registro.id);
                        command.Parameters.AddWithValue("@peso", registro.peso ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@repeticiones", registro.repeticiones ?? (object)DBNull.Value);

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
        }

        public static bool EliminarRegistroSerie(RegistroSerieObject registro)
        {
            string strSQL = "delete usuariosRutinas where id=@id";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@id", registro.id);

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
        }
    }
}
