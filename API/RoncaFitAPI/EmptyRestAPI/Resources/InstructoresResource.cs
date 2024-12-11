using EmptyRestAPI.Models;
using Newtonsoft.Json;
using System.Data.SqlClient;
using static EmptyRestAPI.Resources.DataConnectionResource;

namespace EmptyRestAPI.Resources
{
    
    public class InstructoresResource
    {
        public static InstructorObject[]? ObtenerInstructoresInfo()
        {
            InstructorObject[]? Instructores = null;

            string strSQL = "exec sp_RoncaFit_getEmpleadosActividades";
            Dictionary<string, object> Parametros = new Dictionary<string, object> { };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);
            
            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                Instructores = JsonConvert.DeserializeObject<InstructorObject[]>(Resultado);
            }
            return Instructores;
        }

        public static ActividadObject[]? GetInstructoresActs(int idEmpleado)
        {
            List<ActividadObject> actividades = new List<ActividadObject>();

            // Obtenemos los clientes
            string strSQL = "SELECT a.* FROM instructoresAct i INNER JOIN actividades a ON a.idActividad = i.idActividad WHERE idEmpleado = @idEmpleado";

            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idEmpleado", idEmpleado);
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

        public static bool ActualizarActsInstructor(int idEmpleado, ActividadObject[] actividades)
        {
            string deleteSQL = "DELETE FROM instructoresAct WHERE idEmpleado = @idEmpleado";
            string insertSQL = "INSERT INTO instructoresAct (idEmpleado, idActividad) VALUES (@idEmpleado, @idActividad)";

            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {

                    // Iniciar una transacción
                    using (var transaction = dbConnection.BeginTransaction())
                    {
                        try
                        {
                            // Eliminar las actividades actuales del instructor
                            using (var deleteCommand = dbConnection.CreateCommand())
                            {
                                deleteCommand.Transaction = transaction;
                                deleteCommand.CommandText = deleteSQL;
                                deleteCommand.Parameters.AddWithValue("@idEmpleado", idEmpleado);
                                deleteCommand.ExecuteNonQuery();
                            }

                            // Insertar las nuevas actividades
                            foreach (var actividad in actividades)
                            {
                                using (var insertCommand = dbConnection.CreateCommand())
                                {
                                    insertCommand.Transaction = transaction;
                                    insertCommand.CommandText = insertSQL;
                                    insertCommand.Parameters.AddWithValue("@idEmpleado", idEmpleado);
                                    insertCommand.Parameters.AddWithValue("@idActividad", actividad.idActividad);
                                    insertCommand.ExecuteNonQuery();
                                }
                            }

                            // Confirmar la transacción
                            transaction.Commit();
                        }
                        catch (Exception ex)
                        {
                            // Revertir la transacción en caso de error
                            transaction.Rollback();
                            Console.WriteLine($"Error: {ex.Message}");
                            return false;
                        }
                    }
                }
            }
            catch (SqlException sqlEx)
            {
                Console.WriteLine($"SQL Error: {sqlEx.Message}");
                return false;
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
                return false;
            }

            return true;
        }

        public static InstructorObject[]? GetInstructoresDisponibles(TablonActividadesObject actTablon)
        {
            InstructorObject[]? Instructores = null;

            string strSQL = "exec sp_RoncaFit_getInstructoresDisponibles @idActividad, @fecha";
            Dictionary<string, object> Parametros = new Dictionary<string, object>
            {
                { "@idActividad", actTablon.idActividad },
                { "@fecha", actTablon.fecha }
            };
            string Resultado = DataConnectionResource.GetJSONQuerySQL(strSQL, Parametros, "", DataConnectionResource.Sistemas.RoncaFit);

            if (!string.IsNullOrWhiteSpace(Resultado))
            {
                Instructores = JsonConvert.DeserializeObject<InstructorObject[]>(Resultado);
            }
            return Instructores;
        }

    }
}
