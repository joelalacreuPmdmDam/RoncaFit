using EmptyRestAPI.Models;

namespace EmptyRestAPI.Resources
{
    public class ReservasResource
    {


        public static ReservaObject[]? ObtenerReservasInfo()
        {
            List<ReservaObject> reservas = new List<ReservaObject>();

            string strSQL = @"select r.idReserva,r.idCliente,concat(c.nombre,' ',c.apellidos) as cliente,t.idActividad,a.actividad,t.fecha
                            from reservas r
                            inner join clientes c on c.idCliente = r.idCliente
                            inner join tablonActividades t on t.id = r.idActividadTablon
                            inner join actividades a on a.idActividad = t.idActividad
                            order by fecha desc
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
                                ReservaObject reserva = new ReservaObject
                                {
                                    idReserva = reader.GetInt32(reader.GetOrdinal("idReserva")),
                                    idCliente = reader.GetInt32(reader.GetOrdinal("idCliente")),
                                    cliente = reader.GetString(reader.GetOrdinal("cliente")),
                                    idActividad = reader.GetInt32(reader.GetOrdinal("idActividad")),
                                    actividad = reader.GetString(reader.GetOrdinal("actividad")),
                                    fecha = reader.GetDateTime(reader.GetOrdinal("fecha"))
                                };
                                reservas.Add(reserva);
                            }
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }

            return reservas.ToArray();
        }

        public static bool InsertarReserva(ReservaObject reservaNueva)
        {
            string strSQL = "INSERT INTO reservas (idCliente, idActividadTablon) VALUES (@idCliente,@idActividadTablon)";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idCliente", reservaNueva.idCliente);
                        command.Parameters.AddWithValue("@idActividadTablon", reservaNueva.idActividadTablon);

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

        public static bool ActualizarReserva(ReservaObject reservaActualizada)
        {
            string strSQL = "UPDATE reservas SET idCliente = @idCliente WHERE idReserva = @idReserva";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idReserva", reservaActualizada.idReserva);
                        command.Parameters.AddWithValue("@idCliente", reservaActualizada.idCliente);

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

        public static bool EliminarReserva(ReservaObject reserva)
        {
            string strSQL = "DELETE FROM reservas WHERE idReserva = @idReserva";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idReserva", reserva.idReserva);

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

        public static bool PuedeReservar(int idCliente, int idActividadTablon)
        {
            string strSQL = "SELECT dbo.permitirReserva(@idCliente, @idActividadTablon) AS PermitirReserva";
            try
            {
                using (var dbConnection = DataConnectionResource.GetConnection(DataConnectionResource.Sistemas.RoncaFit))
                {
                    using (var command = dbConnection.CreateCommand())
                    {
                        command.CommandText = strSQL;
                        command.Parameters.AddWithValue("@idCliente", idCliente);
                        command.Parameters.AddWithValue("@idActividadTablon", idActividadTablon);

                        // Ejecutar la consulta y obtener el resultado
                        var result = command.ExecuteScalar();
                        if (result != null && result is bool permitirReserva)
                        {
                            return permitirReserva; // Devuelve true si la reserva es permitida (valor 1), false si no (valor 0)
                        }
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Error: {ex.Message}");
            }
            return false; // Si ocurre un error o no se puede comprobar, no permitimos la reserva
        }

        public static TablonActividadesObject[]? ObtenerReservasCliente(int idCliente)
        {
            List<TablonActividadesObject> tablonActividades = new List<TablonActividadesObject>();

            string strSQL = @"select ta.id,ta.idActividad,a.actividad,ta.fecha,ta.completa,ta.inscripciones,a.limite,ta.idInstructor,CONCAT(e.nombre, ' ', e.apellidos) as instructor,r.idReserva
                            from tablonActividades ta
                            inner join empleados e on e.idEmpleado = ta.idInstructor
                            inner join actividades a on a.idActividad = ta.idActividad
							inner join reservas r on r.idActividadTablon=ta.id and r.idCliente = @idCliente
                            where fecha> GETDATE()
                            order by tA.fecha asc
            ";
            Dictionary<string, object> Parametros = new Dictionary<string, object>
            {
                { "@idCliente", idCliente }
            };

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
                                    idReserva = reader.GetInt32(reader.GetOrdinal("idReserva")),
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

    }
}
