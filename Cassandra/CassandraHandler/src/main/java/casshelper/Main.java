package casshelper;

import casshelper.repositories.BookStatusTable;
import casshelper.repositories.BooksTable;
import casshelper.repositories.KeyspaceRepository;
import casshelper.repositories.ReadersTable;
import org.apache.log4j.BasicConfigurator;
import com.datastax.driver.core.Session;




public class Main {

    public static void main(String args[]) throws InterruptedException {
        //Подключение к Кассандре
        BasicConfigurator.configure();
        CassandraConnector connector = new CassandraConnector();
        connector.connect("192.168.56.102", 9042);
        Session session = connector.getSession();
        System.out.println("Connected to Cassandra.");

        //Открытие/создание пространства ключей и таблиц
        KeyspaceRepository sr = new KeyspaceRepository(session);
        sr.createKeyspace("library", "SimpleStrategy", 1);
        sr.useKeyspace("library");
        BooksTable br = new BooksTable(session);
        br.createTable();
        BookStatusTable bst = new BookStatusTable(session);
        bst.createTable();
        ReadersTable rt = new ReadersTable(session);
        rt.createTable();

        //Запуск тредов для прослушивания потоков от подмодулей LilModules и ответа на запросы
        LilServerThread desk=new LilServerThread(br, bst, rt, 9090);
        LilServerThread site = new LilServerThread(br, bst, rt, 9191);
        LilServerThread filler = new LilServerThread(br, bst, rt, 9292);
        desk.start();
        site.start();
        filler.start();

        //Пока хотя бы один тред жив -  главный поток спит.
        while(desk.isAlive()||site.isAlive()||filler.isAlive()){
            Thread.sleep(3000);
        }
        connector.close();

    }

}