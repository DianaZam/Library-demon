package casshelper.repositories;

import casshelper.library.Book;
import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import java.util.ArrayList;
import java.util.List;


public class BooksTable extends Table{

    /**
     * РОБИТ
     */

    public BooksTable(Session session) {
        super(session);
        TABLE_NAME="books";
    }

    /**
     * Creates the books table.
     *
     * LIKE works for author, title, key_words and science_field columns
     *
     */
    public void createTable() {
        StringBuilder sb1 = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(")
                .append("book_id int PRIMARY KEY, ").append("title text,").append("author text,")
                .append("science_field text,").append("key_words text,").append("publication_year int,")
                .append("edition int,").append("storage_id int);");

        String query = sb1.toString();
        session.execute(query);
        query = createCustomId("title");
        session.execute(query);
        query = createCustomId("author");
        session.execute(query);
        query = createCustomId("key_words");
        session.execute(query);
        query = createCustomId("science_field");
        session.execute(query);
    }







    /**
     *ВААААЩЕ НЕ ДОПИЛЕНО
     */
    public void insertBook(Book book){
        Statement st = new SimpleStatement("INSERT INTO "+TABLE_NAME+
                " (book_id, title, author, science_field, key_words, publication_year, edition,storage_id)  VALUES ("
                +book.getBook_id()
                +", '"+book.getTitle()+"', '"+book.getAuthor()+"', '"+book.getScience_field()+"', '"+book.getKey_words()
                +"', "+book.getPublication_year()+", "+book.getEdition()+", "+ book.getStorage_id() +");");
        session.execute(st);
    }
    public void deleteBook(Book book){
    }
    public void updateBook(Book book){

    }
    public List<Book> selectBooks(){
        Select select = QueryBuilder.select("book_id", "title").from(TABLE_NAME);
       ResultSet rs = session.execute(select.toString());
        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book s = new Book(r.getInt("book_id"), r.getString("title"), null, null, null, 0, 0, 0);
            books.add(s);
        }

        return books;
    }
    public List<Book> selectBooksWhere(String where, String what){
        Select select = QueryBuilder.select("book_id", "title").from(TABLE_NAME);
        select.where(QueryBuilder.like(where, "%"+what+"%"));
        ResultSet rs = session.execute(select.toString());
        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book s = new Book(r.getInt("book_id"), r.getString("title"), null, null, null, 0, 0, 0);
            books.add(s);
        }

        return books;
    }

}
