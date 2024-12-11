namespace EmptyRestAPI.Models
{
    public class BadRequestObject
    {
        public bool Error { get; set; } = true;
        public string Mensaje { get; set; } = "";
    }
}
