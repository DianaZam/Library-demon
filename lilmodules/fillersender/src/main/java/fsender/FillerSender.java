package fsender;

import library.Book;
import library.Reader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import requests.ACBook;
import requests.AGCReader;
import requests.GetBooks;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class FillerSender {
    private final String HOST = "localhost";
    private final int PORT_MAIN =9292;
    private final int PORT_CLIENT =8282;


    public FillerSender() {}

    public void start() throws IOException, InterruptedException, ClassNotFoundException {
        ServerSocket client;
        Socket toServer;
        Socket toClient;
        String method;
        String jsonString;


        try {
            client = new ServerSocket(PORT_CLIENT);
            toClient = client.accept();

            System.out.println("Клиент подключился к сокету");

            toServer = new Socket(HOST, PORT_MAIN);
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
                    case  "Info":{
                        System.out.println("Запрос "+method+" отправлен.");
                        int reader_count = sois.readInt();
                        int book_count = sois.readInt();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("reader_count", reader_count);
                        answerJSON.put("book_count", book_count);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "AddBook": {
                        ACBook addBook = new ACBook();
                        addBook.setMethod((String) requestJSON.get("method"));
                        addBook.setBook_id(0);
                        addBook.setTitle((String) requestJSON.get("title"));
                        addBook.setAuthor((String) requestJSON.get("author"));
                        addBook.setKey_words((String) requestJSON.get("key_words"));
                        addBook.setScience_field((String) requestJSON.get("science_field"));
                        Long edition =(Long) requestJSON.get("edition");
                        addBook.setEdition(edition.intValue());
                        Long publ =(Long) requestJSON.get("publication_year");
                        addBook.setPublication_year(publ.intValue());
                        Long storage_id =(Long) requestJSON.get("storage_id");
                        addBook.setStorage_id(storage_id.intValue());
                        Long count =(Long) requestJSON.get("count");
                        addBook.setCount(count.intValue());


                        soos.writeObject(addBook);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        boolean result = sois.readBoolean();
                        int new_id = sois.readInt();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("result", result);
                        answerJSON.put("book_id", new_id);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "ChangeBook": {
                        ACBook changeBook = new ACBook();
                        changeBook.setMethod((String) requestJSON.get("method"));
                        Long book_id =(Long) requestJSON.get("book_id");
                        changeBook.setBook_id(book_id.intValue());
                        changeBook.setTitle((String) requestJSON.get("title"));
                        changeBook.setAuthor((String) requestJSON.get("author"));
                        changeBook.setKey_words((String) requestJSON.get("key_words"));
                        changeBook.setScience_field((String) requestJSON.get("science_field"));
                        Long edition =(Long) requestJSON.get("edition");
                        changeBook.setEdition(edition.intValue());
                        Long publ =(Long) requestJSON.get("publication_year");
                        changeBook.setPublication_year(publ.intValue());
                        Long storage_id =(Long) requestJSON.get("storage_id");
                        changeBook.setStorage_id(storage_id.intValue());


                        soos.writeObject(changeBook);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        boolean result = sois.readBoolean();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("result", result);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "GetBooks": {
                        GetBooks getBooks = new GetBooks();
                        getBooks.setAuthor((String) requestJSON.get("author"));
                        getBooks.setKey_words((String) requestJSON.get("key_words"));
                        getBooks.setMethod((String) requestJSON.get("method"));
                        getBooks.setScience_field((String) requestJSON.get("science_field"));
                        getBooks.setTitle((String) requestJSON.get("title"));
                       if(requestJSON.containsKey("book_id")){
                           Long book_id = (Long) requestJSON.get("book_id");
                           if(book_id!=null) getBooks.setBook_id(book_id.intValue());
                       }


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

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("count", books.size());
                        answerJSON.put("books", arrr);

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
                    case "ChangeReader": {
                        AGCReader changeReader = new AGCReader();
                        changeReader.setMethod((String) requestJSON.get("method"));
                        Long card_id =(Long) requestJSON.get("card_id");
                        changeReader.setCard_id(card_id.intValue());
                        changeReader.setPassport((String) requestJSON.get("passport"));
                        changeReader.setFirst_name((String) requestJSON.get("first_name"));
                        changeReader.setMiddle_name((String) requestJSON.get("middle_name"));
                        changeReader.setLast_name((String) requestJSON.get("last_name"));
                        changeReader.setBirthday( (String) requestJSON.get("birthday"));

                        soos.writeObject(changeReader);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        boolean result = sois.readBoolean();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("result", result);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "GetReaders": {
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

                        List<Reader> readers = (List<Reader>) sois.readObject();


                        JSONArray arrr = new JSONArray();
                        for (int i = 0; i < readers.size(); i++) {
                            JSONObject reader = new JSONObject();
                            reader.put("card_id", readers.get(i).getCard_id());
                            reader.put("passport", readers.get(i).getPassport());
                            reader.put("first_name", readers.get(i).getFirst_name());
                            reader.put("middle_name", readers.get(i).getMiddle_name());
                            reader.put("last_name", readers.get(i).getLast_name());
                            reader.put("birthday", readers.get(i).getBirthday());
                            arrr.add(reader);
                        }

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("count", readers.size());
                        answerJSON.put("readers", arrr);

                        jsonString = answerJSON.toJSONString();

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
                toServer = new Socket(HOST, PORT_MAIN);
                // сокет для записи данных на сервер
                soos = new ObjectOutputStream(toServer.getOutputStream());
                // сокет для чтения ответа с сервера
                sois = new ObjectInputStream(toServer.getInputStream());
                System.out.println("Новое подключение.");
            }

        } catch (IOException e) {
            System.out.println("I'm dum-dum-dum");
            System.exit(-3);
        }
    }
}
