package sample;

public class JSONBuilder {
    public static String makeJsonGetReader(String card_id, String passport, String middle_name, String first_name, String last_name){
        if (card_id.equals(""))
            card_id = "null";
        if (passport.equals(""))
            passport = "null";
        else passport = "\"" + passport + "\"";
        if (first_name.equals(""))
            first_name = "null";
        else first_name = "\"" + first_name + "\"";
        if (middle_name.equals(""))
            middle_name = "null";
        else middle_name = "\"" + middle_name + "\"";
        if (last_name.equals(""))
            last_name = "null";
        else last_name = "\"" + last_name + "\"";

        String jsonString = "{" +
                "\"method\":" + " \"GetReader\"," +
                "\"card_id\":" + card_id + "," +
                "\"passport\":" + passport + "," +
                "\"first_name\":" + first_name + "," +
                "\"middle_name\":" + middle_name + "," +
                "\"last_name\":" + last_name + "," +
                "\"birthday\":" + "null" + "" +
                "}";
        return jsonString;
    }
    public static String makeJsonGetBooks(String book_id, String title, String author, String science_field, String key_words){
        if (!title.equals(""))
            title = "\"" + title + "\"";
        else
            title = "null";
        if (!author.equals(""))
            author = "\"" + author + "\"";
        else
            author = "null";
        if (!science_field.equals(""))
            science_field = "\"" + science_field + "\"";
        else
            science_field = "null";
        if (!key_words.equals(""))
            key_words = "\"" + key_words + "\"";
        else
            key_words = "null";
        if (book_id.equals(""))
            book_id = "null";
        String jsonString = "{" +
                "\"method\": \"GetBooks\"," +
                "\"title\": " + title + "," +
                "\"author\": " + author + "," +
                "\"science_field\": " + science_field + "," +
                "\"key_words\": " + key_words + "," +
                "\"book_id\": " + book_id +
                "}";
        return jsonString;
    }
    public static String makeJsonAddReader(String passport, String first_name, String middle_name, String last_name, String birthday){
        String jsonString = "{" +
                "\"method\":" + " \"AddReader\"," +
                "\"card_id\":" + "null" + "," +
                "\"passport\":\"" + passport + "\"," +
                "\"first_name\":\"" + first_name + "\"," +
                "\"middle_name\":\"" + middle_name + "\"," +
                "\"last_name\":\"" + last_name + "\"," +
                "\"birthday\":\"" + birthday + "\"" +
                "}";
        return jsonString;
    }
    public static String makeJsonGetBook(String book_id){
        String jsonString = "{" +
                "\"method\":" + " \"GetBook\"," +
                "\"book_id\":" + book_id + "" +
                "}";
        return jsonString;
    }
    public static String makeJsonGetStockpile(String book_id){
        String jsonString = "{" +
                "\"method\":" + " \"IsBookInStock\"," +
                "\"book_id\":" + book_id +
                "}";
        return jsonString;
    }
    public static String makeJsonGiveBook(String book_id, String user_id){
        String jsonString = "{" +
                "\"method\":" + " \"ChangeBookStatusOut\"," +
                "\"book_id\":" + book_id + "," +
                "\"location_id\":" + user_id +
                "}";
        return jsonString;
    }
    public static String makeJsonReturnBook(String book_id, String user_id){
        String jsonString = "{" +
                "\"method\":" + " \"ChangeBookStatusIn\"," +
                "\"book_id\":" + book_id + "," +
                "\"location_id\":" + user_id +
                "}";
        return jsonString;
    }
}
