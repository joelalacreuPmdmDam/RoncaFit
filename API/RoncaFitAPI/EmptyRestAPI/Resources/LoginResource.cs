using EmptyRestAPI.Models;

namespace EmptyRestAPI.Resources
{
    public class LoginResource
    {

        public static ClienteObject? VerificarCredenciales(string mail, string contrasenya)
        {
            string strSQL = "SELECT idCliente, dni, nombre, apellidos, mail, nombreUsuario FROM clientes WHERE mail = @mail AND contrasenya = @contrasenya";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@mail", mail);
                        command.Parameters.AddWithValue("@contrasenya", contrasenya);

                        using (var reader = command.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                return new ClienteObject
                                {
                                    idCliente = reader.GetInt32(reader.GetOrdinal("idCliente")),
                                    dni = reader.GetString(reader.GetOrdinal("dni")),
                                    nombreUsuario = reader.GetString(reader.GetOrdinal("nombreUsuario"))
                                };
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
            return null;
        }
    }
}
