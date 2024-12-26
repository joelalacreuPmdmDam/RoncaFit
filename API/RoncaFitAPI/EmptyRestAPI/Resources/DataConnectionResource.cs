using System.Data.SqlClient;

namespace EmptyRestAPI.Resources
{
    public static class DataConnectionResource
    {
        
        //Sistemas
        public enum Sistemas { RoncaFit }

        //Cadenas de conexión para los sitemas
        const string RoncaFitConn = "Server=192.168.68.101; Database=RoncaFit; User Id=sa; Password=1234; MultipleActiveResultSets=True;";
        //Función que devuelve la cadena de conexión para el sistema
        private static string CadenaConn(Sistemas Sistema)
        {
            string Resultado = "";
            switch (Sistema.ToString())
            {
                case "RoncaFit":
                    Resultado = RoncaFitConn;
                    break;
            }
            return Resultado;
        }

        //Función que crea la conexión a la BBDD
        public static System.Data.SqlClient.SqlConnection GetConnection(Sistemas Sistema)
        {
            System.Data.SqlClient.SqlConnection cn = new System.Data.SqlClient.SqlConnection();
            cn.ConnectionString = CadenaConn(Sistema);
            cn.Open();

            return cn;
        }

        //Función para leer un SQL que ya devuelve un jSON
        public static string GetJSONQuerySQL(string strSQL, Dictionary<string, object> Parametros, string DefaultNoData, Sistemas Sistema)
        {
            using (var dbConnection = GetConnection(Sistema))
            {
                using (var command = dbConnection.CreateCommand())
                {
                    command.CommandText = strSQL;
                    //command.CommandTimeout = 600;
                    command.CommandTimeout = 60;
                    foreach (var parametro in Parametros)
                    {
                        command.Parameters.AddWithValue(parametro.Key, parametro.Value ?? DBNull.Value);
                    }
                    using (var dr = command.ExecuteReader())
                    {
                        string Resultado = "";
                        while (dr.Read())
                        {
                            Resultado = Resultado + Convert.ToString(dr[0]);
                        }
                        if (Resultado == "")
                        {
                            Resultado = DefaultNoData;
                        }
                        return Resultado;
                    }
                }
            }
        }

        //Funcion para ejecutar comandos
        public static void ExecuteSQL(string strSQL, Dictionary<string, object> Parametros, Sistemas Sistema)
        {
            using (var dbConnection = GetConnection(Sistema))
            {
                using (var command = dbConnection.CreateCommand())
                {
                    command.CommandText = strSQL;
                    foreach (var parametro in Parametros)
                    {
                        command.Parameters.AddWithValue(parametro.Key, parametro.Value ?? DBNull.Value);
                    }
                    command.ExecuteNonQuery();
                }
            }
        }

    }
}
