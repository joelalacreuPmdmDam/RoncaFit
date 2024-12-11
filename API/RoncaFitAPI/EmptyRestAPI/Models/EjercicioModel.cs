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
    }

    public class EjercicioResponseObject
    {
        public EjercicioObject[] ejercicios {  get; set; }
    }
}
