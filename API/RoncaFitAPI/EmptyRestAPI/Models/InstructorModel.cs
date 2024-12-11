namespace EmptyRestAPI.Models
{
    public class InstructorObject
    {
        public int? idEmpleado {  get; set; }
        public String? dni {  get; set; }
        public String? nombre { get; set; }
        public Boolean? actividades { get; set; }
    }

    public class InstructorResponseObject
    {
        public InstructorObject[] Instructores { get; set; }
    }
}
