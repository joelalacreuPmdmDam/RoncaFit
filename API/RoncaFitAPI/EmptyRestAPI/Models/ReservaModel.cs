namespace EmptyRestAPI.Models
{
    public class ReservaObject
    {
        public int? idReserva {  get; set; }
        public int ? idCliente { get; set; }
        public string? cliente { get; set; }
        public int? idActividad { get; set; }
        public string? actividad { get; set; }
        public DateTime? fecha { get; set; }
        public int? idActividadTablon { get; set; }
    }

    public class ReservaResponseObject
    {
        public ReservaObject[] Reservas { get; set; }
    }
}
