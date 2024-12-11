namespace EmptyRestAPI.Models
{
    public class RutinaObject
    {
        public int? idRutina { get; set; }
        public string? nombreRutina { get; set; }
        public string? descripcion { get; set; }
    }

    public class RutinaResponseObject
    {
        public RutinaObject[] rutinas { get; set; }
    }
}
