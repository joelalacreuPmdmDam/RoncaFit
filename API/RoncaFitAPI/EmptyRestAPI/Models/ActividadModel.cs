namespace EmptyRestAPI.Models
{
    public class ActividadObject
    {
        public int? idActividad {  get; set; }
        public String? actividad { get; set; }
        public int? limite { get; set; }
    }

    public class ActividadResponseObject
    {
        public ActividadObject[] Actividades { get; set; }
    }
}
