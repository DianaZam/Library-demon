package casshelper;

import casshelper.repositories.BookStatusTable;
import casshelper.repositories.BooksTable;
import casshelper.repositories.KeyspaceRepository;
import casshelper.repositories.ReadersTable;
import org.apache.log4j.BasicConfigurator;
import com.datastax.driver.core.Session;

public class Main {

    public static void main(String args[]) {
        BasicConfigurator.configure();
        CassandraConnector connector = new CassandraConnector();
        connector.connect("192.168.56.102", 9042);
        Session session = connector.getSession();
        System.out.println("Connected to Cassandra.");

        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("library");

        BooksTable br = new BooksTable(session);
        //Тут создание книг
       /* br.createTable();


        br.insert(new Book(1, "War or piece", "Black B.B., Green G.G.","Politics",
                "War, Piece, Politic", 2001, 3,9998));
        br.insert(new Book(2, "Matrix", "Red R.R.","Math",
                "Algebra, Matrix", 2020, 1,9244));
        br.insert(new Book(3, "Data Base", "Blue B.B., Green G.G.","Informatics",
                "Data Base, Information, IT", 2001, 3,9377));
        br.insert(new Book(4, "Biology pour dummies", "White W.W., Green G.G.","Biology",
                "Biology", 1997, 8,9621));
        br.insert(new Book(5, "Ubuntu", "Orange O.O.","Informatics, OS",
                "Ubuntu, OS, Informatics, Linux", 2004, 7,9174));
*/
        //тут вывод книг
       /* List<Book> books = br.selectAll();
        for (int i=0; i<books.size(); i++) System.out.println(books.get(i).toString());
*/

       BookStatusTable bst = new BookStatusTable(session);
       //создание статусов
        /*bst.createTable();


        bst.insert(new BookStatus(1 , true, 99981));
        bst.insert(new BookStatus(2 , true, 92441));
        bst.insert(new BookStatus(3 , true, 93771));
        bst.insert(new BookStatus(4 , true, 96211));
        bst.insert(new BookStatus(5 , false, 121234));
        bst.insert(new BookStatus(1 , false, 123478));
        bst.insert(new BookStatus(2 , false, 123478));
        bst.insert(new BookStatus(5 , false, 123478));
*/
        //вывод статусов
/*
        List<BookStatus> bookStatuses = bst.selectAll();
        for(int i=0; i<bookStatuses.size(); i++) System.out.println(bookStatuses.get(i).toString());
*/

        ReadersTable rt = new ReadersTable(session);
        //создание читателей
      /*  rt.createTable();
        rt.insert(new Reader(99981, 54568, "Shady", "Shadow",
                "Dark", LocalDate.fromYearMonthDay(2001,2,21)));
        rt.insert(new Reader(121234, 54268, "Danya", "Shadow",
                "Dark", LocalDate.fromYearMonthDay(2001,3,21)));
        rt.insert(new Reader(123478, 51168, "Naruto", "Shadow",
                "Dark", LocalDate.fromYearMonthDay(1999,2,21)));
*/
        //вывод статусов
/*
        List<Reader> readers = rt.selectReadersWherePassport(54568);
        for(int i=0; i<readers.size(); i++) System.out.println(readers.get(i).toString());
*/


        //br.deleteTable();
        // bst.deleteTable();
        //rt.deleteTable();


        //sr.deleteKeyspace("library");


        connector.close();
    }





}