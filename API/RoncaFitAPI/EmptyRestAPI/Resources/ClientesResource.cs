using EmptyRestAPI.Models;
using Newtonsoft.Json;

namespace EmptyRestAPI.Resources
{
    public class ClientesResource
    {

        public static ClienteObject[]? ObtenerClientesInfo()
        {
            List<ClienteObject> clientes = new List<ClienteObject>();

            // Obtenemos los clientes
            string strSQL = "SELECT idCliente, dni, nombre, apellidos, mail, nombreUsuario, CONCAT(dni,' - ',nombre,' ',apellidos) as infoCliente FROM clientes";
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
                                ClienteObject cliente = new ClienteObject
                                {
                                    idCliente = reader.GetInt32(reader.GetOrdinal("idCliente")),
                                    dni = reader.GetString(reader.GetOrdinal("dni")),
                                    nombre = reader.GetString(reader.GetOrdinal("nombre")),
                                    apellidos = reader.GetString(reader.GetOrdinal("apellidos")),
                                    mail = reader.GetString(reader.GetOrdinal("mail")),
                                    nombreUsuario = reader.GetString(reader.GetOrdinal("nombreUsuario")),
                                    infoCliente = reader.GetString(reader.GetOrdinal("infoCliente"))
                                };
                                clientes.Add(cliente);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            return clientes.ToArray();
        }

        public static bool InsertarCliente(ClienteObject cliente)
        {
            string strSQL = "INSERT INTO clientes (dni, nombre, apellidos, mail, nombreUsuario, contrasenya) VALUES (@dni, @nombre, @apellidos, @mail, @nombreUsuario,@contrasenya)";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@dni", cliente.dni ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombre", cliente.nombre ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@apellidos", cliente.apellidos ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@mail", cliente.mail ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombreUsuario", cliente.nombreUsuario ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@contrasenya", cliente.contrasenya ?? (object)DBNull.Value);

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

        public static bool ActualizarCliente(ClienteObject cliente)
        {
            string strSQL = "UPDATE clientes SET dni = @dni, nombre = @nombre, apellidos = @apellidos, mail = @mail, nombreUsuario = @nombreUsuario WHERE idCliente = @idCliente";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idCliente", cliente.idCliente);
                        command.Parameters.AddWithValue("@dni", cliente.dni ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombre", cliente.nombre ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@apellidos", cliente.apellidos ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@mail", cliente.mail ?? (object)DBNull.Value);
                        command.Parameters.AddWithValue("@nombreUsuario", cliente.nombreUsuario ?? (object)DBNull.Value);

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

        public static bool EliminarCliente(ClienteObject cliente)
        {
            string strSQL = "DELETE FROM clientes WHERE idCliente = @idCliente";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idCliente", cliente.idCliente);

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
