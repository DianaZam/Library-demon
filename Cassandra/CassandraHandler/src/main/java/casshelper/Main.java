package casshelper;


import casshelper.library.Book;
import casshelper.library.BookStatus;
import casshelper.repositories.BookStatusTable;
import casshelper.repositories.BooksTable;
import casshelper.repositories.KeyspaceRepository;
import com.datastax.driver.core.LocalDate;
import org.apache.log4j.BasicConfigurator;

import java.util.List;


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
        br.createTable();


        br.insertBook(new Book(1, "War or piece", "Black B.B., Green G.G.","Politics",
                "War, Piece, Politic", 2001, 3,9998));
        br.insertBook(new Book(2, "Matrix", "Red R.R.","Math",
                "Algebra, Matrix", 2020, 1,9244));
        br.insertBook(new Book(3, "Data Base", "Blue B.B., Green G.G.","Informatics",
                "Data Base, Information, IT", 2001, 3,9377));
        br.insertBook(new Book(4, "Biology pour dummies", "White W.W., Green G.G.","Biology",
                "Biology", 1997, 8,9621));
        br.insertBook(new Book(5, "Ubuntu", "Orange O.O.","Informatics, OS",
                "Ubuntu, OS, Informatics, Linux", 2004, 7,9174));


        //List<Book> books = br.selectBooks();
        //System.out.println(books.get(0).getTitle()+"\n\n\n");

        BookStatusTable bst = new BookStatusTable(session);
        bst.createTable();


        bst.insertBookStatus(new BookStatus(1 , true, 99981));
        bst.insertBookStatus(new BookStatus(2 , true, 92441));
        bst.insertBookStatus(new BookStatus(3 , true, 93771));
        bst.insertBookStatus(new BookStatus(4 , true, 96211));
        bst.insertBookStatus(new BookStatus(5 , false, 121234));
        bst.insertBookStatus(new BookStatus(1 , false, 123478));
        bst.insertBookStatus(new BookStatus(2 , false, 123478));
        bst.insertBookStatus(new BookStatus(5 , false, 123478));



       // List<BookStatus> bookStatuses = bst.selectBookStatus();
        //System.out.println(bookStatuses.get(0).getStatus_id().toString());


        //br.deleteTable();
        // bst.deleteTable();
        //sr.deleteKeyspace("library");


        connector.close();
    }





}