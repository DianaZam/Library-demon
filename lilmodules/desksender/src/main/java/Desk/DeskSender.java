package Desk;

import library.Book;
import library.BookStatus;
import library.Reader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import requests.AGCReader;
import requests.CGBookStatus;
import requests.GetBooks;
import requests.IsBookInStock;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class DeskSender {
    private final String host = "localhost";
    private final int portMain=9090;
    private final int portClient=8080;


    public DeskSender() {}

    public void start() throws IOException, InterruptedException, ClassNotFoundException {
        ServerSocket client;
        Socket toServer;
        Socket toClient;
        String method;
        String jsonString;


        try {
            client = new ServerSocket(portClient);
            toClient = client.accept();

            System.out.println("Клиент подключился к сокету");

            toServer = new Socket(host, portMain);
            // сокет для записи данных на сервер
            ObjectOutputStream soos = new ObjectOutputStream(toServer.getOutputStream());
            // сокет для чтения ответа с сервера
            ObjectInputStream sois = new ObjectInputStream(toServer.getInputStream());
            System.out.println("Главный сервер подключился к сокету");

            while (!toClient.isClosed() &&
                    !toServer.isClosed()) {

                // канал записи в сокет к клиенту
                ObjectOutputStream coos = new ObjectOutputStream(toClient.getOutputStream());
                // канал чтения из сокета от клиенты
                ObjectInputStream cois = new ObjectInputStream(toClient.getInputStream());

                // Читаю запрос
                //jsonString=null;
                jsonString=cois.readUTF();


                //Чисто проверка
                /*Scanner scanner = new Scanner(System.in);
                int var;
                switch (var = scanner.nextInt()){
                    case 1: {
                        jsonString="{ \"method\" : \"GetBooks\", \"title\" : \"u\", \"author\" : null, "+
                                "\"science_field\" : null, \"key_words\" : null}";
                        break;
                    }
                    case 2: {
                        jsonString="{ \"method\" : \"AddReader\", \"card_id\" : 123, \"passport\" : 1233221, "+
                                "\"first_name\" : \"Diana\", \"middle_name\" : \"Get\", " +
                                "\"last_name\" : \"Zam\", \"birthday\" : \"21.02.2001\"}";
                        break;
                    }
                    case 3: {
                        jsonString="{ \"method\" : \"GetReader\", \"card_id\" : 99981, \"passport\" : null, "+
                                "\"first_name\" : null, \"middle_name\" : null, \"last_name\" : null, \"birthday\" : null}";
                        break;
                    }
                    case 4: {
                        jsonString="{ \"method\" : \"IsBookInStock\", \"book_id\" : \"5\"}";
                        break;
                    }
                }
*/
                //

                System.out.println(jsonString);

                //парсить json
                JSONObject requestJSON = (JSONObject) JSONValue.parse(jsonString);

                //отправить method
                method = (String) requestJSON.get("method");
                soos.writeUTF(method);
                soos.flush();
                //case method, отправить класс-запрос
                //читаю объекты
                //парсю их в json
                switch(method){
                    case "GetBooks": {
                        GetBooks getBooks = new GetBooks();
                        getBooks.setAuthor((String) requestJSON.get("author"));
                        getBooks.setKey_words((String) requestJSON.get("key_words"));
                        getBooks.setMethod((String) requestJSON.get("method"));
                        getBooks.setScience_field((String) requestJSON.get("science_field"));
                        getBooks.setTitle((String) requestJSON.get("title"));
                        Long book_id = (Long) requestJSON.get("book_id");
                        if(book_id!=null) getBooks.setBook_id(book_id.intValue());

                       soos.writeObject(getBooks);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                       List<Book> books = (List<Book>) sois.readObject();
                        for (int i=0; i<books.size(); i++) System.out.println(books.get(i).toString());


                        JSONArray arrr = new JSONArray();
                        for (int i=0; i<books.size(); i++){
                            JSONObject book = new JSONObject();
                            book.put("book_id",books.get(i).getBook_id());
                            book.put("title",books.get(i).getTitle());
                            book.put("author",books.get(i).getAuthor());
                            book.put("science_field",books.get(i).getScience_field());
                            book.put("key_words",books.get(i).getKey_words());
                            book.put("publication_year",books.get(i).getPublication_year());
                            book.put("edition",books.get(i).getEdition());
                            book.put("storage_id",books.get(i).getStorage_id());
                            arrr.add(book);
                        }
                        JSONObject data = new JSONObject();
                        data.put("count", books.size());
                        data.put("books", arrr);

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("data", data);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "GetBook": {
                        GetBooks getBook = new GetBooks();
                        getBook.setAuthor(null);
                        getBook.setKey_words(null);
                        getBook.setMethod((String) requestJSON.get("method"));
                        getBook.setScience_field(null);
                        getBook.setTitle(null);
                        Long book_id = (Long) requestJSON.get("book_id");
                        if(book_id!=null) getBook.setBook_id(book_id.intValue());

                        soos.writeObject(getBook);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        List<Book> books = (List<Book>) sois.readObject();
                        for (int i=0; i<books.size(); i++) System.out.println(books.get(i).toString());

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("book_id",books.get(0).getBook_id());
                        answerJSON.put("title",books.get(0).getTitle());
                        answerJSON.put("author",books.get(0).getAuthor());
                        answerJSON.put("science_field",books.get(0).getScience_field());
                        answerJSON.put("key_words",books.get(0).getKey_words());
                        answerJSON.put("publication_year",books.get(0).getPublication_year());
                        answerJSON.put("edition",books.get(0).getEdition());
                        answerJSON.put("storage_id",books.get(0).getStorage_id());
                        answerJSON.put("status", 200);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "AddReader": {
                        AGCReader addReader = new AGCReader();
                        addReader.setMethod((String) requestJSON.get("method"));
                        addReader.setCard_id(0);
                        addReader.setPassport((String) requestJSON.get("passport"));
                        addReader.setFirst_name((String) requestJSON.get("first_name"));
                        addReader.setMiddle_name((String) requestJSON.get("middle_name"));
                        addReader.setLast_name((String) requestJSON.get("last_name"));
                        addReader.setBirthday( (String) requestJSON.get("birthday"));

                        soos.writeObject(addReader);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        boolean result = sois.readBoolean();
                        int new_id = sois.readInt();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("result", result);
                        answerJSON.put("card_id", new_id);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "GetReader": {
                        AGCReader getReader = new AGCReader();
                        getReader.setMethod((String) requestJSON.get("method"));
                        Long card_idSt = (Long) requestJSON.get("card_id");
                        if(card_idSt!=null) getReader.setCard_id(card_idSt.intValue());
                         getReader.setPassport((String) requestJSON.get("passport"));
                        getReader.setFirst_name((String) requestJSON.get("first_name"));
                        getReader.setMiddle_name((String) requestJSON.get("middle_name"));
                        getReader.setLast_name((String) requestJSON.get("last_name"));
                        getReader.setBirthday(null);

                        soos.writeObject(getReader);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        Reader reader = (Reader) sois.readObject();
                        List<BookStatus> bookStatusList = (List<BookStatus>) sois.readObject();

                        if (reader.getCard_id()!=0) {
                            JSONArray arrr = new JSONArray();
                            for (int i = 0; i < bookStatusList.size(); i++) {
                                JSONObject book = new JSONObject();
                                book.put("book_id", bookStatusList.get(i).getBook_id());
                                book.put("time", bookStatusList.get(i).getStatus_id().toString());
                                arrr.add(book);
                            }
                            JSONObject data = new JSONObject();
                            data.put("count", bookStatusList.size());
                            data.put("books", arrr);
                            JSONObject answerJSON = new JSONObject();
                            answerJSON.put("status", 200);
                            answerJSON.put("card_id", reader.getCard_id());
                            answerJSON.put("passport", reader.getPassport());
                            answerJSON.put("first_name", reader.getFirst_name());
                            answerJSON.put("middle_name", reader.getMiddle_name());
                            answerJSON.put("last_name", reader.getLast_name());
                            answerJSON.put("birthday", reader.getBirthday());
                            answerJSON.put("data", data);

                            jsonString = answerJSON.toJSONString();
                        }
                        else{
                            JSONObject answerJSON = new JSONObject();
                            answerJSON.put("status", 500);
                            answerJSON.put("result", false);
                            jsonString = answerJSON.toJSONString();
                        }
                        break;
                    }
                    case "IsBookInStock": {
                        IsBookInStock isBookInStock = new IsBookInStock();
                        Long book_idSt = (Long) requestJSON.get("book_id");
                        if(book_idSt!=null) isBookInStock.setBook_id(book_idSt.intValue());
                        isBookInStock.setMethod((String) requestJSON.get("method"));
                        soos.writeObject(isBookInStock);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        int count = sois.readInt();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("count", count);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "GetBookStatus": {
                        CGBookStatus getBookStatus = new CGBookStatus();

                        getBookStatus.setMethod((String) requestJSON.get("method"));
                        Long book_idSt = (Long) requestJSON.get("book_id");
                        if(book_idSt!=null) getBookStatus.setBook_id(book_idSt.intValue());
                        Long location_idST = (Long) requestJSON.get("location_id");
                        if(location_idST!=null) getBookStatus.setLocation_id(location_idST.intValue());


                        soos.writeObject(getBookStatus);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        List<BookStatus> bookStatusList = (List<BookStatus>) sois.readObject();

                        JSONArray arrr = new JSONArray();
                        for (int i=0; i<bookStatusList.size(); i++){
                            JSONObject book = new JSONObject();
                            book.put("book_id",bookStatusList.get(i).getBook_id());
                            book.put("in_stock",bookStatusList.get(i).getIn_stock());
                            book.put("status_id",bookStatusList.get(i).getStatus_id().toString());
                            book.put("location_id",bookStatusList.get(i).getLocation_id());
                            arrr.add(book);
                        }
                        JSONObject data = new JSONObject();
                        data.put("count", bookStatusList.size());
                        data.put("statuses", arrr);

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("data", data);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "ChangeBookStatusIn":
                    case "ChangeBookStatusOut": {
                        CGBookStatus changeBookStatus = new CGBookStatus();

                        changeBookStatus.setMethod((String) requestJSON.get("method"));
                        Long book_idSt = (Long) requestJSON.get("book_id");
                        if(book_idSt!=null) changeBookStatus.setBook_id(book_idSt.intValue());
                        Long location_idST = (Long) requestJSON.get("location_id");
                        if(location_idST!=null) changeBookStatus.setLocation_id(location_idST.intValue());


                        soos.writeObject(changeBookStatus);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");
                        boolean result = sois.readBoolean();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("result", result);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                }


                System.out.println(jsonString);

                //Отправляю json
               coos.writeUTF(jsonString);
                coos.flush();



                //Ожидание нового подключения клиента
                toClient = client.accept();
                sois.close();
                soos.close();
                toServer.close();
                toServer = new Socket(host, portMain);
                // сокет для записи данных на сервер
                soos = new ObjectOutputStream(toServer.getOutputStream());
                // сокет для чтения ответа с сервера
                sois = new ObjectInputStream(toServer.getInputStream());
                System.out.println("Новое подключение.");
              }

            } catch (IOException e) {
            System.out.println("I'm dum-dum");
            System.exit(-2);
        }
    }
}
