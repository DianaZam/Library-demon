package casshelper;

import casshelper.repositories.BookStatusTable;
import casshelper.repositories.BooksTable;
import casshelper.repositories.KeyspaceRepository;
import casshelper.repositories.ReadersTable;
import library.Reader;
import org.apache.log4j.BasicConfigurator;
import com.datastax.driver.core.Session;

import java.util.List;


public class Main {

    public static void main(String args[]) throws InterruptedException {
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
        BookStatusTable bst = new BookStatusTable(session);
        bst.createTable();
        ReadersTable rt = new ReadersTable(session);
        rt.createTable();


        LilServerThread desk=new LilServerThread(br, bst, rt, 9090);
        LilServerThread site = new LilServerThread(br, bst, rt, 9191);
        LilServerThread filler = new LilServerThread(br, bst, rt, 9292);

        desk.start();
        site.start();
        filler.start();

        while(desk.isAlive()||site.isAlive()||filler.isAlive()){
            Thread.sleep(3000);
        }
        connector.close();


       /*

        //br.deleteTable();
        // bst.deleteTable();
        //rt.deleteTable();


        //sr.deleteKeyspace("library");

        */
    }

}