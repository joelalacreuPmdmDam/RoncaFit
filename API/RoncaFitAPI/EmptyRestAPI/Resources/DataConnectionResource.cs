using System.Data.SqlClient;

namespace EmptyRestAPI.Resources
{
    public static class DataConnectionResource
    {
        
        //Sistemas
        public enum Sistemas { RoncaFit }//Desarrollo, MEUSPProd,

        //Cadenas de conexión para los sitemas
        //const string DesarrolloConn = "Server=isc03.improlog.com; Database=Desarrollo; User Id=usrDesarrollo; Password=aeoxi2?fpH74; MultipleActiveResultSets=True;";
        //const string MEUSPProdConn = "Server=isc04.improlog.com; Database=IIWS_MEUSP; User Id=sa; Password=868imp$; MultipleActiveResultSets=True;";
        const string RoncaFitConn = "Server=192.168.56.1; Database=RoncaFit; User Id=sa; Password=1234; MultipleActiveResultSets=True;";
        //Función que devuelve la cadena de conexión para el sistema
        private static string CadenaConn(Sistemas Sistema)
        {
            string Resultado = "";
            switch (Sistema.ToString())
            {
                /*
                case "Desarrollo":
                    Resultado = DesarrolloConn;
                    break;
                case "MEUSPProd":
                    Resultado = MEUSPProdConn;
                    break;
                */
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
