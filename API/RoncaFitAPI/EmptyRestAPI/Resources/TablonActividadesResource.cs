using EmptyRestAPI.Models;
using Mysqlx.Crud;
using System.Data;

namespace EmptyRestAPI.Resources
{
    public class TablonActividadesResource
    {

        public static TablonActividadesObject[]? ObtenerTablonActividadesInfo()
        {
            List<TablonActividadesObject> tablonActividades = new List<TablonActividadesObject>();

            string strSQL = @"select ta.id,ta.idActividad,a.actividad,tA.fecha,tA.completa,tA.inscripciones,a.limite,tA.idInstructor,CONCAT(e.nombre, ' ', e.apellidos) as instructor
                            from tablonActividades tA
                            inner join empleados e on e.idEmpleado = tA.idInstructor
                            inner join actividades a on a.idActividad = tA.idActividad
                            where fecha> GETDATE()
                            order by tA.fecha asc
            ";
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
                                TablonActividadesObject actividadTablon = new TablonActividadesObject
                                {
                                    id = reader.GetInt32(reader.GetOrdinal("id")),
                                    idActividad = reader.GetInt32(reader.GetOrdinal("idActividad")),
                                    actividad = reader.GetString(reader.GetOrdinal("actividad")),
                                    completa = reader.GetBoolean(reader.GetOrdinal("completa")),
                                    fecha = reader.GetDateTime(reader.GetOrdinal("fecha")),
                                    inscripciones = reader.GetInt32(reader.GetOrdinal("inscripciones")),
                                    limite = reader.GetInt32(reader.GetOrdinal("limite")),
                                    idEmpleado = reader.GetInt32(reader.GetOrdinal("idInstructor")),
                                    instructor = reader.GetString(reader.GetOrdinal("instructor")),
                                };
                                tablonActividades.Add(actividadTablon);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            return tablonActividades.ToArray();
        }

        public static bool InsertarTablonActividad(TablonActividadesObject actividadTablonNueva)
        {
            string strSQL = "INSERT INTO tablonActividades (idActividad, completa, inscripciones,fecha, idInstructor) VALUES (@idActividad, 0, 0,@fecha, 13)";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idActividad", actividadTablonNueva.idActividad);
                        command.Parameters.AddWithValue("@fecha", actividadTablonNueva.fecha);

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

        public static bool ActualizarTablonActividad(TablonActividadesObject actividadTablonActualizada)
        {
            string strSQL = "UPDATE tablonActividades SET idInstructor = @idInstructor WHERE id = @id";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@id", actividadTablonActualizada.id);
                        command.Parameters.AddWithValue("@idInstructor", actividadTablonActualizada.idEmpleado);

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

        public static bool EliminarTablonActividad(TablonActividadesObject tablonActividad)
        {
            string strSQL = "DELETE FROM tablonActividades WHERE id = @id";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@id", tablonActividad.id);

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
