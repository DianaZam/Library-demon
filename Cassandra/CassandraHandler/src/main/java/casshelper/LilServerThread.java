package casshelper;

import library.Book;
import casshelper.repositories.BookStatusTable;
import casshelper.repositories.BooksTable;
import casshelper.repositories.ReadersTable;
import library.BookStatus;
import library.Reader;
import requests.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LilServerThread extends Thread{
    private int lilServerPort;
    private ObjectOutputStream lsoos;
    private ObjectInputStream lsois;
    private BooksTable bt;
    private BookStatusTable bst;
    private ReadersTable rt;

    public LilServerThread(BooksTable bt, BookStatusTable bst, ReadersTable rt, int lilServerPort){
        this.bt=bt;
        this.bst=bst;
        this.rt=rt;
        this.lilServerPort=lilServerPort;
    }

    @Override
    public void run() {

        System.out.println("Я роблю. "+lilServerPort);

        try (ServerSocket server = new ServerSocket(lilServerPort)) {
            while (!server.isClosed()){

                System.out.print("Мам, я в цикле!");

                // становимся в ожидание подключения к сокету под именем - "lilserver" на серверной стороне
                Socket lilserver = server.accept();

                // после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением
                System.out.print("Соединение установлено");


                // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
                while (!lilserver.isClosed()) {

                    // инициируем каналы для  общения в сокете, для сервера
                    // канал записи в сокет
                    lsoos = new ObjectOutputStream(lilserver.getOutputStream());
                    System.out.println("канал записи создан");

                    // канал чтения из сокета
                    lsois = new ObjectInputStream(lilserver.getInputStream());
                    System.out.println("канал чтения создан");


                    System.out.println("Сервер готов читать из сокета");

                    //Читаем из сокета метод
                    String method = lsois.readUTF();
                    //Обработка в соответствии с методом
                    switch(method){
                        case "Info":{
                            List<Reader> readers = rt.selectAll();
                            List<Book> books = bt.selectAll();

                            //Отправка объектов на lilserver
                            lsoos.writeInt(readers.size());
                            lsoos.flush();
                            lsoos.writeInt(books.size());
                            lsoos.flush();
                            break;

                        }
                        case "GetBook":
                        case "GetBooks": {
                            //Чтение запроса
                            GetBooks getBooks=(GetBooks) lsois.readObject();
                            List<Book> books = null;

                            //Поиск в соответствии с полями запроса
                            if(getBooks.getBook_id()!=0) {
                                books = bt.selectBookFromID(getBooks.getBook_id());
                            } else if(getBooks.getTitle()==null && getBooks.getAuthor()==null
                                    && getBooks.getKey_words()==null && getBooks.getScience_field()==null){
                                books = bt.selectAll();
                            }else if(getBooks.getTitle()!=null && getBooks.getAuthor()!=null){
                                books = bt.selectBooksLikeTitleAuthor(getBooks.getTitle(), getBooks.getAuthor());
                            }else if(getBooks.getKey_words()!=null && getBooks.getScience_field()!=null){
                                books = bt.selectBooksLikeFieldWords(getBooks.getScience_field(), getBooks.getKey_words());
                            }else if(getBooks.getTitle()!=null){
                                books = bt.selectBooksLikeTitle(getBooks.getTitle());
                            }else if(getBooks.getAuthor()!=null){
                                books = bt.selectBooksLikeAuthor(getBooks.getAuthor());
                            }else if(getBooks.getScience_field()!=null){
                                books = bt.selectBooksLikeField(getBooks.getScience_field());
                            }else if (getBooks.getKey_words()!=null){
                                books = bt.selectBooksLikeWords(getBooks.getKey_words());
                            }
                            //Отправка объектов на lilserver
                            lsoos.writeObject(books);
                            lsoos.flush();
                            break;
                        }
                        case "AddBook": {
                            //Чтение запроса
                            ACBook addBook = (ACBook) lsois.readObject();

                            Book newBook = new Book();
                            List<Book> books = bt.selectAll();
                            int newId = books.size()+1;
                            newBook.setBook_id(newId);
                            newBook.setTitle(addBook.getTitle());
                            newBook.setAuthor(addBook.getAuthor());
                            newBook.setScience_field(addBook.getScience_field());
                            newBook.setKey_words(addBook.getKey_words());
                            newBook.setPublication_year(addBook.getPublication_year());
                            newBook.setEdition(addBook.getEdition());
                            newBook.setStorage_id(addBook.getStorage_id());


                            //Отправка результата операции на lilserver
                            //Запись не удалась
                            if (bt.selectBookFromID(newId).size()>0){
                                lsoos.writeBoolean(false);
                                lsoos.flush();
                                lsoos.writeInt(0);
                                lsoos.flush();
                            }//Запись удалась
                            else {
                                bt.insert(newBook);
                                BookStatus bookStatus = new BookStatus();
                                bookStatus.setIn_stock(true);
                                bookStatus.setBook_id(newId);
                                bookStatus.setLocation_id(newBook.getStorage_id());
                                for(int i=0; i<addBook.getCount(); i++) bst.insert(bookStatus);
                                lsoos.writeBoolean(true);
                                lsoos.flush();
                                lsoos.writeInt(newId);
                                lsoos.flush();
                            }
                            break;
                        }
                        case "ChangeBook": {
                            //Чтение запроса
                            ACBook changeBook = (ACBook) lsois.readObject();

                            Book book = new Book();
                            book.setBook_id(changeBook.getBook_id());
                            book.setTitle(changeBook.getTitle());
                            book.setAuthor(changeBook.getAuthor());
                            book.setScience_field(changeBook.getScience_field());
                            book.setKey_words(changeBook.getKey_words());
                            book.setPublication_year(changeBook.getPublication_year());
                            book.setEdition(changeBook.getEdition());
                            book.setStorage_id(changeBook.getStorage_id());

                            //Отправка результата операции на lilserver
                            bt.update(book);
                            lsoos.writeBoolean(true);
                            lsoos.flush();

                            break;
                        }
                        case "AddReader": {
                            //Чтение запроса
                            AGCReader addReader = (AGCReader) lsois.readObject();

                            Reader newReader = new Reader();
                            List<Reader> readers = rt.selectAll();
                            int newId = readers.size()+1;
                            newReader.setCard_id(newId);
                            newReader.setPassport(addReader.getPassport());
                            newReader.setFirst_name(addReader.getFirst_name());
                            newReader.setMiddle_name(addReader.getMiddle_name());
                            newReader.setLast_name(addReader.getLast_name());
                            newReader.setBirthday(addReader.getBirthday());

                            //Отправка результата операции на lilserver
                            //Запись не удалась
                            if (rt.selectReadersWhereID(newId).size()>0){
                                lsoos.writeBoolean(false);
                                lsoos.flush();
                                lsoos.writeInt(0);
                                lsoos.flush();
                            }//Запись удалась
                            else {
                                rt.insert(newReader);
                                lsoos.writeBoolean(true);
                                lsoos.flush();
                                lsoos.writeInt(newId);
                                lsoos.flush();
                            }
                            break;
                        }
                        case "ChangeReader": {
                            //Чтение запроса
                            AGCReader changeReader = (AGCReader) lsois.readObject();

                            Reader reader = new Reader();
                            reader.setCard_id(changeReader.getCard_id());
                            reader.setPassport(changeReader.getPassport());
                            reader.setFirst_name(changeReader.getFirst_name());
                            reader.setMiddle_name(changeReader.getMiddle_name());
                            reader.setLast_name(changeReader.getLast_name());
                            reader.setBirthday(changeReader.getBirthday());

                            //Отправка результата операции на lilserver
                            rt.update(reader);
                            lsoos.writeBoolean(true);
                            lsoos.flush();

                            break;
                        }
                        case "GetReader": {
                            //Чтение запроса
                            AGCReader getReader = (AGCReader) lsois.readObject();
                            List<Reader> readers;
                           Reader reader = null;

                            if(getReader.getCard_id()!=0){
                                readers = rt.selectReadersWhereID(getReader.getCard_id());
                                if (readers.size()>0)  reader = readers.get(0);
                            }else if(getReader.getPassport()!=null){
                                readers = rt.selectReadersWherePassport(getReader.getPassport());
                                if (readers.size()>0)  reader = readers.get(0);
                            }else if(getReader.getFirst_name()!=null && getReader.getMiddle_name()!=null && getReader.getLast_name()!=null){
                                readers =  rt.selectReadersLikeFIO(getReader.getFirst_name(), getReader.getMiddle_name(), getReader.getLast_name());
                                if (readers.size()>0)  reader = readers.get(0);
                            }else if(getReader.getFirst_name()!=null && getReader.getLast_name()!=null){
                                readers =  rt.selectReadersLikeFI(getReader.getFirst_name(), getReader.getLast_name());
                                if (readers.size()>0)  reader = readers.get(0);
                            }

                            //Отправка объектов на lilserver
                            //Пользователь найден
                            if (reader!=null){
                                lsoos.writeObject(reader);
                                lsoos.flush();

                                List<BookStatus> bookStatusList = bst.selectFromLocation(reader.getCard_id());
                                lsoos.writeObject(bookStatusList);
                                lsoos.flush();
                            }//Пользователь не найден
                            else{
                                lsoos.writeObject(new Reader());
                                lsoos.flush();

                                List<BookStatus> bookStatusList = new ArrayList<BookStatus>();
                                lsoos.writeObject(bookStatusList);
                                lsoos.flush();
                            }

                            break;
                        }
                        case "GetReaders": {
                            //Чтение запроса
                            AGCReader getReader = (AGCReader) lsois.readObject();
                            List<Reader> readers = null;

                            if(getReader.getCard_id()!=0){
                                readers = rt.selectReadersWhereID(getReader.getCard_id());
                            }else if(getReader.getPassport()!=null){
                                readers = rt.selectReadersWherePassport(getReader.getPassport());
                            }else if(getReader.getFirst_name()!=null && getReader.getMiddle_name()!=null && getReader.getLast_name()!=null){
                                readers =  rt.selectReadersLikeFIO(getReader.getFirst_name(), getReader.getMiddle_name(), getReader.getLast_name());
                            }else if(getReader.getFirst_name()!=null && getReader.getLast_name()!=null){
                                readers =  rt.selectReadersLikeFI(getReader.getFirst_name(), getReader.getLast_name());
                            }

                            //Отправка объектов на lilserver
                            lsoos.writeObject(readers);
                            lsoos.flush();
                            break;
                        }
                        case "IsBookInStock": {
                            //Чтение запроса
                            IsBookInStock isBookInStock = (IsBookInStock) lsois.readObject();
                            List <BookStatus> bookStatusList = bst.selectFromKey(isBookInStock.getBook_id(), true);
                            //Отправка количества книг в наличие на lilserver
                            lsoos.writeInt(bookStatusList.size());
                            lsoos.flush();
                            break;
                        }
                        case "GetBookStatus": {
                            //Чтение запроса
                            CGBookStatus getBookStatus = (CGBookStatus) lsois.readObject();
                            List <BookStatus> bookStatusList = null;
                            if(getBookStatus.getBook_id()!=0)  bookStatusList = bst.selectFromBookID(getBookStatus.getBook_id());
                            if(getBookStatus.getLocation_id()!=0) bookStatusList = bst.selectFromLocation(getBookStatus.getLocation_id());
                            //Отправка объектов на lilserver
                            lsoos.writeObject(bookStatusList);
                            lsoos.flush();
                            break;
                        }
                        case "ChangeBookStatusIn": {
                            //Чтение запроса
                            CGBookStatus changeBookStatus = (CGBookStatus) lsois.readObject();
                            BookStatus bookStatus = bst.selectFromKeyLoc(changeBookStatus.getBook_id(),
                                    false, changeBookStatus.getLocation_id()).get(0);
                            bst.changeStatusTrue(bookStatus, bt);

                            //Отправка результата lilserver
                            lsoos.writeBoolean(true);
                            lsoos.flush();
                            break;
                        }
                        case "ChangeBookStatusOut": {
                            //Чтение запроса
                            CGBookStatus changeBookStatus = (CGBookStatus) lsois.readObject();
                            BookStatus bookStatus = bst.selectFromKey(changeBookStatus.getBook_id(), true).get(0);
                            bst.changeStatusFalse(bookStatus, changeBookStatus.getLocation_id());

                            //Отправка результата lilserver
                            lsoos.writeBoolean(true);
                            lsoos.flush();
                            break;
                        }
                    }


                    lilserver = server.accept();

                }


                System.out.println("Клиент "+lilServerPort+" отключился");
                System.out.println("Закрываем соединения и сокеты");

                // закрываем сначала каналы
                lsois.close();
                lsoos.close();

                // закрываем сам сокет общения на стороне сервера
                lilserver.close();
            }
            System.out.println("Пока!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }






    }
}
