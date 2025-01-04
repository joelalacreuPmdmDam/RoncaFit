namespace EmptyRestAPI.Models
{
    public class EjercicioObject
    {
        public int? idRutina {  get; set; }
        public int? idEjercicio {  get; set; }
        public string? nombreEjercicio { get; set; }
        public int? series {  get; set; }
        public string? reps { get; set; }
        public string? descripcion { get; set; }
        public string? instrucciones { get; set; }
        public string? urlVideo { get; set; }
    }

    public class EjercicioResponseObject
    {
        public EjercicioObject[] ejercicios {  get; set; }
    }

    public class SerieObject
    {
        public int? idCliente { get; set; }
        public int? idEjercicio { get; set; }
        public int? idSerie { get; set; }
        public int? idRutina { get; set; }
        public int? reps { get; set; }
        public Decimal? peso { get; set; }
    }
}
