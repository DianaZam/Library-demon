package casshelper;

import com.datastax.driver.core.LocalDate;
import library.Book;
import casshelper.repositories.BookStatusTable;
import casshelper.repositories.BooksTable;
import casshelper.repositories.ReadersTable;
import library.BookStatus;
import library.Reader;
import library.ReaderR;
import requests.AGReader;
import requests.CGBookStatus;
import requests.GetBooks;
import requests.IsBookInStock;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
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

                // становимся в ожидание подключения к сокету под именем - "client" на серверной стороне
                Socket lilserver = server.accept();

                // после хэндшейкинга сервер ассоциирует подключающегося клиента с этим сокетом-соединением
                System.out.print("Соединение установлено");

                // инициируем каналы для  общения в сокете, для сервера
                // канал записи в сокет
                lsoos = new ObjectOutputStream(lilserver.getOutputStream());
                System.out.println("канал записи создан");

                // канал чтения из сокета
                lsois = new ObjectInputStream(lilserver.getInputStream());
                System.out.println("канал чтения создан");

                // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
                while (!lilserver.isClosed()) {
                    System.out.println("Сервер готов читать из сокета");
                    String method = lsois.readUTF();
                    switch(method){
                        case "GetBooks": {
                            GetBooks getBooks=(GetBooks) lsois.readObject();
                            List<Book> books = null;
                            if(getBooks.getTitle()==null && getBooks.getAuthor()==null
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
                            lsoos.writeObject(books);
                            lsoos.flush();
                            break;
                        }
                        case "AddReader": {
                            AGReader addReader = (AGReader) lsois.readObject();
                            Reader newReader = new Reader();
                            newReader.setCard_id(addReader.getCard_id());
                            newReader.setPassport(addReader.getPassport());
                            newReader.setFirst_name(addReader.getFirst_name());
                            newReader.setMiddle_name(addReader.getMiddle_name());
                            newReader.setLast_name(addReader.getLast_name());
                            newReader.setBirthday(LocalDate.fromYearMonthDay(addReader.getBirthday().getYear(),
                                    addReader.getBirthday().getMonth(), addReader.getBirthday().getDay()));
                            rt.insert(newReader);
                            lsoos.writeBoolean(true);
                            lsoos.flush();
                            break;
                        }
                        case "GetReader": {
                            AGReader getReader = (AGReader) lsois.readObject();
                           Reader reader = null;

                            if(getReader.getCard_id()!=0){
                                reader = rt.selectReadersWhereID(getReader.getCard_id()).get(0);
                            }else if(getReader.getPassport()!=0){
                                reader = rt.selectReadersWherePassport(getReader.getPassport()).get(0);
                            }else if(getReader.getFirst_name()!=null && getReader.getMiddle_name()!=null && getReader.getLast_name()!=null){
                                reader = rt.selectReadersLikeFIO(getReader.getFirst_name(), getReader.getMiddle_name(), getReader.getLast_name()).get(0);
                            }else if(getReader.getFirst_name()!=null && getReader.getLast_name()!=null){
                                reader = rt.selectReadersLikeFI(getReader.getFirst_name(), getReader.getLast_name()).get(0);
                            }

                            System.out.println(reader.getBirthday().getMillisSinceEpoch());

                            ReaderR readerR = new ReaderR(reader.getCard_id(), reader.getPassport(), reader.getFirst_name(), reader.getMiddle_name(),
                                    reader.getLast_name(), reader.getBirthday());

                            lsoos.writeObject(readerR);
                            lsoos.flush();

                            List<BookStatus> bookStatusList = bst.selectFromLocation(reader.getCard_id());
                            lsoos.writeObject(bookStatusList);
                            lsoos.flush();

                            break;
                        }
                        case "IsBookInStock": {
                            IsBookInStock isBookInStock = (IsBookInStock) lsois.readObject();
                            List <BookStatus> bookStatusList = bst.selectFromKey(isBookInStock.getBook_id(), true);
                            lsoos.writeInt(bookStatusList.size());
                            lsoos.flush();
                            break;
                        }
                        case "GetBookStatus": {
                            CGBookStatus getBookStatus = (CGBookStatus) lsois.readObject();
                            List <BookStatus> bookStatusList = null;
                            if(getBookStatus.getBook_id()!=0)  bookStatusList = bst.selectFromBookID(getBookStatus.getBook_id());
                            if(getBookStatus.getLocation_id()!=0) bookStatusList = bst.selectFromLocation(getBookStatus.getLocation_id());
                            lsoos.writeObject(bookStatusList);
                            lsoos.flush();
                            break;
                        }
                        case "ChangeBookStatusIn": {
                            CGBookStatus changeBookStatus = (CGBookStatus) lsois.readObject();
                            BookStatus bookStatus = bst.selectFromKeyLoc(changeBookStatus.getBook_id(),
                                    false, changeBookStatus.getLocation_id()).get(0);
                            bst.changeStatusTrue(bookStatus, bt);

                            lsoos.writeBoolean(true);
                            lsoos.flush();
                            break;
                        }
                        case "ChangeBookStatusOut": {
                            CGBookStatus changeBookStatus = (CGBookStatus) lsois.readObject();
                            BookStatus bookStatus = bst.selectFromKey(changeBookStatus.getBook_id(), true).get(0);
                            bst.changeStatusFalse(bookStatus, changeBookStatus.getLocation_id());

                            lsoos.writeBoolean(true);
                            lsoos.flush();
                            break;
                        }
                    }

                }

                // если условие выхода - верно выключаем соединения
                System.out.println("Клиент "+lilServerPort+" отключился");
                System.out.println("Закрываем соединения и сокеты");

                // закрываем сначала каналы сокета !
                lsois.close();
                lsoos.close();

                // потом закрываем сам сокет общения на стороне сервера!
                lilserver.close();
            }
            System.out.println("Пока!");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();

        }






    }
}
