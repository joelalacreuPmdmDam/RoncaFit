namespace EmptyRestAPI.Resources
{
    public static class LoggerResource
    {
        public static void Info(string requestId, string Process, string Log = "", string Values = "")
        {
            if (Log != "") Log = @",""Log"":""" + Log + @"""";
            if (Values != "") Values = @",""Values"":" + Values;
            Console.WriteLine(
                @"{" +
                @"""Type"":""Info""," +
                @"""DateTime"":""" + DateTime.Now + @"""," +
                @"""requestId"":""" + requestId + @"""," +
                @"""Process"":""" + Process + @"""" +
                Log +
                Values +
                @"}"
            );
        }

        public static void Warning(string requestId, string Process, string Log = "", string Values = "")
        {
            if (Log != "") Log = @",""Log"":""" + Log + @"""";
            if (Values != "") Values = @",""Values"":" + Values;
            Console.WriteLine(
                @"{" +
                @"""Type"":""Warning""," +
                @"""DateTime"":""" + DateTime.Now + @"""," +
                @"""requestId"":""" + requestId + @"""," +
                @"""Process"":""" + Process + @"""" +
                Log +
                Values +
                @"}"
            );

        }

        public static void Error(string requestId, string Process, string Log = "", string Values = "")
        {
            if (Log != "") Log = @",""Log"":""" + Log + @"""";
            if (Values != "") Values = @",""Values"":" + Values;
            Console.WriteLine(
                @"{" +
                @"""Type"":""Error""," +
                @"""DateTime"":""" + DateTime.Now + @"""," +
                @"""requestId"":""" + requestId + @"""," +
                @"""Process"":""" + Process + @"""" +
                Log +
                Values +
                @"}"
            );

        }
    }
}
