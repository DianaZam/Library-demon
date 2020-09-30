package casshelper;


import casshelper.library.Book;
import casshelper.repositories.BooksTable;
import casshelper.repositories.KeyspaceRepository;
import org.apache.log4j.BasicConfigurator;

import java.util.List;


import com.datastax.driver.core.Session;


public class Main {
    //private static final Logger LOG = LoggerFactory.getLogger(Main.class);

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
        Book book = new Book(1, "War or piece bitch", "Bob B.B.","litrachure",
                "War, Piece", 123, 45,9998 );
        br.insertBook(book);
        List<Book> books = br.selectBooksWhereTitle("title", "piece");
        System.out.println(books.get(0).getTitle()+"\n\n\n");

      //br.deleteTable();

      //sr.deleteKeyspace("library");
        connector.close();
    }





}