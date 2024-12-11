using EmptyRestAPI.Models;

namespace EmptyRestAPI.Resources
{
    public class EmpleadosResource
    {


        public static EmpleadoObject[]? ObtenerEmpleadosInfo()
        {
            List<EmpleadoObject> empleados = new List<EmpleadoObject>();

            // Obtenemos los clientes
            string strSQL = "SELECT * FROM empleados";
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
                                EmpleadoObject empleado = new EmpleadoObject
                                {
                                    idEmpleado = reader.GetInt32(reader.GetOrdinal("idEmpleado")),
                                    dni = reader.GetString(reader.GetOrdinal("dni")),
                                    nombre = reader.GetString(reader.GetOrdinal("nombre")),
                                    apellidos = reader.GetString(reader.GetOrdinal("apellidos"))
                                };
                                empleados.Add(empleado);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            return empleados.ToArray();
        }

        public static bool InsertarEmpleado(EmpleadoObject empleado)
        {
            string strSQL = "INSERT INTO empleados (dni, nombre, apellidos) VALUES (@dni, @nombre, @apellidos)";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@dni", empleado.dni ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombre", empleado.nombre ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@apellidos", empleado.apellidos ?? (object)DBNull.Value);

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

        public static bool ActualizarEmpleado(EmpleadoObject empleado)
        {
            string strSQL = "UPDATE empleados SET dni = @dni, nombre = @nombre, apellidos = @apellidos WHERE idEmpleado = @idEmpleado";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idEmpleado", empleado.idEmpleado);
                        command.Parameters.AddWithValue("@dni", empleado.dni ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombre", empleado.nombre ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@apellidos", empleado.apellidos ?? (object)DBNull.Value);

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

        public static bool EliminarEmpleado(EmpleadoObject empleado)
        {
            string strSQL = "DELETE FROM empleados WHERE idEmpleado = @idEmpleado";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idEmpleado", empleado.idEmpleado);

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
