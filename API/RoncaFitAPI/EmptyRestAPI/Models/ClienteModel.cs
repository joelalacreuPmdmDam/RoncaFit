﻿namespace EmptyRestAPI.Models
{
    public class ClienteObject
    {
        public int? idCliente { get; set; }
        public String? dni{ get; set; }
        public String? nombre { get; set; }
        public String? apellidos { get; set; }
        public String? mail { get; set; }
        public String? nombreUsuario { get; set; }
        public String? contrasenya { get; set; }
        public String? infoCliente { get; set; }
        public String? imagen { get; set; }

    }

    public class APKClienteRequest
    {
        public int? idCliente { get; set; }
        public String? dni { get; set; }
        public String? nombre { get; set; }
        public String? apellidos { get; set; }
        public String? mail { get; set; }
        public String? imagen { get; set; }
        public String? nombreCliente { get; set; }
    }

    public class ClienteResponseObject
    {
        public ClienteObject[] Clientes { get; set; }
    }
}
