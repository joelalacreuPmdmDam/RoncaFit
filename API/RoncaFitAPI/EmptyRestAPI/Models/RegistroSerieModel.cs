namespace EmptyRestAPI.Models
{
    public class RegistroSerieObject
    {
        public int? id { get; set; }
        public int? idCliente { get; set; }
        public int? idRutina { get; set; }
        public int? idEjercicio { get; set; }
        public int? idSerie { get; set; }
        public Decimal? peso { get; set; }
        public int? repeticiones { get; set; }
        public DateTime? xfec { get; set; }
    }

    public class RegistroSerieResponseObject
    {
        public RegistroSerieObject[] registrosSeries { get; set; }
    }
}
