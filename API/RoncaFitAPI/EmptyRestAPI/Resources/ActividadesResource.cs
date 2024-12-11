using EmptyRestAPI.Models;

namespace EmptyRestAPI.Resources
{
    public class ActividadesResource
    {

        public static ActividadObject[]? ObtenerActividadesInfo()
        {
            List<ActividadObject> actividades = new List<ActividadObject>();

            string strSQL = "SELECT * FROM actividades";
            Dictionary<string, object> Parametros = new Dictionary<string, object> { };

            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        foreach (var parametro in Parametros)
                        {
                            command.Parameters.AddWithValue(parametro.Key, parametro.Value ?? DBNull.Value);
                        }
                        using (var reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                ActividadObject actividad = new ActividadObject
                                {
                                    idActividad = reader.GetInt32(reader.GetOrdinal("idActividad")),
                                    actividad = reader.GetString(reader.GetOrdinal("actividad")),
                                    limite = reader.IsDBNull(reader.GetOrdinal("limite")) ? (int?)null : reader.GetInt32(reader.GetOrdinal("limite"))
                                };
                                actividades.Add(actividad);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            return actividades.ToArray();
        }


        public static bool InsertarActividad(ActividadObject actividad)
        {
            string strSQL = "INSERT INTO actividades (actividad, limite) VALUES (@actividad, @limite)";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@actividad", actividad.actividad ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@limite", actividad.limite ?? (object)DBNull.Value);

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

        public static bool ActualizarActividad(ActividadObject actividad)
        {
            string strSQL = "UPDATE actividades SET actividad = @actividad, limite = @limite WHERE idActividad = @idActividad";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idActividad", actividad.idActividad);
                        command.Parameters.AddWithValue("@actividad", actividad.actividad ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@limite", actividad.limite ?? (object)DBNull.Value);

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

        public static bool EliminarActividad(ActividadObject actividad)
        {
            string strSQL = "DELETE FROM actividades WHERE idActividad = @idActividad";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idActividad", actividad.idActividad);

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
