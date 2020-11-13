package Site;

import com.datastax.driver.core.LocalDate;
import library.Book;
import library.BookStatus;
import library.ReaderR;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import requests.AGReader;
import requests.GetBooks;
import requests.IsBookInStock;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class SiteSender {
    private final String host = "localhost";
    private final int portMain=9191;
    private final int portClient=9099;

    public  SiteSender(){}

    //Протестить всё

    public void start() throws IOException, InterruptedException, ClassNotFoundException {
        ServerSocket client;
        Socket toServer;
        Socket toClient;
        String method = null;
        String jsonString= null;


        try {
            client = new ServerSocket(portClient);
            toClient = client.accept();
            // канал записи в сокет к клиенту

            System.out.println("Клиент подключился к сокету");

            toServer = new Socket(host, portMain);
            // сокет для записи данных на сервер
            ObjectOutputStream soos = new ObjectOutputStream(toServer.getOutputStream());
            // сокет для чтения ответа с сервера
            ObjectInputStream sois = new ObjectInputStream(toServer.getInputStream());
            System.out.println("Главный сервер подключился к сокету");

            while (!toClient.isClosed() &&
                    !toServer.isClosed()) {

                PrintWriter coos =  new PrintWriter(toClient.getOutputStream());
                BufferedOutputStream bos =new BufferedOutputStream(toClient.getOutputStream());
                // канал чтения из сокета от клиенты
                BufferedReader cois = new BufferedReader(new InputStreamReader(toClient.getInputStream()));


                // Читаю запрос
                jsonString=cois.readLine();
                StringTokenizer st =new StringTokenizer(jsonString);
                method = st.nextToken();
                method = st.nextToken().substring(1);
                System.out.println(method);

                for(int i=0; i<6; i++) {
                    jsonString = cois.readLine();
                }

                System.out.println(jsonString);

                String cleaner;
                for(int i=0; i<2; i++) {
                    cleaner = cois.readLine();
                }


                //Чисто проверка
               /* Scanner scanner = new Scanner(System.in);
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
                        jsonString="{ \"method\" : \"GetReader\", \"card_id\" : 123, \"passport\" : null, "+
                                "\"first_name\" : null, \"middle_name\" : null, \"last_name\" : null, \"birthday\" : null}";
                        break;
                    }
                    case 4: {
                        jsonString="{ \"method\" : \"IsBookInStock\", \"book_id\" : \"2340\"}";
                        break;
                    }
                }*/




                //парсить json
                JSONObject requestJSON = (JSONObject) JSONValue.parse(jsonString);


                //отправить method
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
                        getBooks.setMethod(method);
                        getBooks.setScience_field((String) requestJSON.get("science_field"));
                        getBooks.setTitle((String) requestJSON.get("title"));

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

                        ///ТУТ ТОК МАССИВ
                        jsonString=arrr.toJSONString();
                        break;
                    }
                    case "AddReader": {
                        AGReader addReader = new AGReader();
                        addReader.setMethod(method);
                        addReader.setCard_id(((Long) requestJSON.get("card_id")).intValue());
                        addReader.setPassport(((Long) requestJSON.get("passport")).intValue());
                        addReader.setFirst_name((String) requestJSON.get("first_name"));
                        addReader.setMiddle_name((String) requestJSON.get("middle_name"));
                        addReader.setLast_name((String) requestJSON.get("last_name"));
                        String b =(String) requestJSON.get("birthday");
                        int year = Integer.parseInt(b.substring(6,10));
                        int month = Integer.parseInt(b.substring(3,5));
                        int day = Integer.parseInt(b.substring(0,2));

                        addReader.setBirthday( new Date(year,month,day));

                        soos.writeObject(addReader);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        boolean result = sois.readBoolean();

                        JSONObject answerJSON = new JSONObject();
                        answerJSON.put("status", 200);
                        answerJSON.put("result", result);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "GetReader": {
                        AGReader getReader = new AGReader();
                        getReader.setMethod(method);
                        Long card_idSt = (Long) requestJSON.get("card_id");
                        if(card_idSt==null) getReader.setCard_id(0);
                        else getReader.setCard_id(card_idSt.intValue());
                        Long pass = (Long) requestJSON.get("passport");
                        if(pass==null) getReader.setPassport(0);
                        else getReader.setPassport(pass.intValue());
                        getReader.setFirst_name((String) requestJSON.get("first_name"));
                        getReader.setMiddle_name((String) requestJSON.get("middle_name"));
                        getReader.setLast_name((String) requestJSON.get("last_name"));
                        getReader.setBirthday(null);

                        soos.writeObject(getReader);
                        soos.flush();
                        System.out.println("Запрос "+method+" отправлен.");

                        ReaderR reader = (ReaderR) sois.readObject();
                        List<BookStatus> bookStatusList = (List<BookStatus>) sois.readObject();

                        JSONArray arrr = new JSONArray();
                        for (int i=0; i<bookStatusList.size(); i++){
                            JSONObject book = new JSONObject();
                            book.put("book_id",bookStatusList.get(i).getBook_id());
                            book.put("time",bookStatusList.get(i).getStatus_id().toString());
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
                        answerJSON.put("birthday", reader.getBirthday().getDay()+"."+reader.getBirthday().getMonth()+"."+reader.getBirthday().getYear());
                        answerJSON.put("data", data);

                        jsonString=answerJSON.toJSONString();
                        break;
                    }
                    case "IsBookInStock": {
                        IsBookInStock isBookInStock = new IsBookInStock();
                        isBookInStock.setBook_id(Integer.valueOf((String) requestJSON.get("book_id")));
                        isBookInStock.setMethod(method);
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
                }


                System.out.println(jsonString);
                byte[] data = jsonString.getBytes();



                // шлем HTTP Headers
                 coos.println("HTTP/1.1 200 OK");
               coos.println("Server: Java HTTP Server : 1.0");
                coos.println("Date: " + new Date());
                coos.println("Content-type:  application/json");
               coos.println("Content-length: " + data.length);
                //Длина ответа - эхо запроса без первого "/"
                coos.println(); // Пустая строка между headers и содержимым!



                //Отправляю json
                //coos.print(jsonString);
                coos.flush();


                bos.write(data, 0, data.length);
                bos.flush();

                System.out.println("Ответ отослан");


                toClient = client.accept();
            }

        } catch (IOException e) {
            System.out.println("I'm dum");
            System.exit(-1);
        }
    }

}
