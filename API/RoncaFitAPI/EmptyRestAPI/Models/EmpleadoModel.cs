namespace EmptyRestAPI.Models
{
    public class EmpleadoObject
    {
        public int? idEmpleado { get; set; }
        public String? dni { get; set; }
        public String? nombre { get; set; }
        public String? apellidos { get; set; }
    }

    public class EmpleadoResponseObject
    {
        public EmpleadoObject[] Empleados { get; set; }
    }
}
