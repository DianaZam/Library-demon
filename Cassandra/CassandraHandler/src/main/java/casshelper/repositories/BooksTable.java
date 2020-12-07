package casshelper.repositories;

import library.Book;
import com.datastax.driver.core.*;
import java.util.ArrayList;
import java.util.List;


public class BooksTable extends Table{

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
    @Override
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

    public void insert(Book book){
        Statement st = new SimpleStatement("INSERT INTO "+TABLE_NAME+
                " (book_id, title, author, science_field, key_words, publication_year, edition,storage_id)  VALUES ("
                +book.getBook_id()
                +", '"+book.getTitle()+"', '"+book.getAuthor()+"', '"+book.getScience_field()+"', '"+book.getKey_words()
                +"', "+book.getPublication_year()+", "+book.getEdition()+", "+ book.getStorage_id() +");");
        session.execute(st);
    }

    public void update(Book book){
        Statement st = new SimpleStatement("UPDATE  "+TABLE_NAME+
                " SET title = '"+book.getTitle()+"', author = '"+book.getAuthor()+"', science_field ='"+book.getScience_field()+
                "',  key_words='"+book.getKey_words()+"', publication_year ="+book.getPublication_year()+", edition ="+book.getEdition()+
                ", storage_id = "+book.getStorage_id()+"  WHERE book_id=" +book.getBook_id()+";");
        session.execute(st);
    }

    public void delete(Book book){
        Statement st = new SimpleStatement("DELETE FROM " + TABLE_NAME +
                " WHERE book_id = " + book.getBook_id() + ";");
        session.execute(st);
    }

    public List<Book> selectBookFromID(int book_id){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                "  where book_id = "+ book_id+";");
        ResultSet rs = session.execute(st);
        List<Book> books = new ArrayList<Book>();

        for (Row r : rs) {
            Book s = new Book(r.getInt("book_id"), r.getString("title"),
                    r.getString("author"), r.getString("science_field"), r.getString("key_words"),
                    r.getInt("publication_year"), r.getInt("edition"), r.getInt("storage_id"));
            books.add(s);
        }

        return books;
    }

    public List<Book> selectAll(){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+";");
        ResultSet rs = session.execute(st);
        List<Book> books = new ArrayList<Book>();
        for (Row r : rs) {
            Book s = new Book(r.getInt("book_id"), r.getString("title"),
                    r.getString("author"), r.getString("science_field"), r.getString("key_words"),
                    r.getInt("publication_year"), r.getInt("edition"), r.getInt("storage_id"));
            books.add(s);
        }
        return books;
    }

    public List<Book> selectBooksLike(String where, String what){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                "  where "+ where +" like '%"+ what+"%';");
        ResultSet rs = session.execute(st);
        List<Book> books = new ArrayList<Book>();
        for (Row r : rs) {
            Book s = new Book(r.getInt("book_id"), r.getString("title"),
                    r.getString("author"), r.getString("science_field"), r.getString("key_words"),
                    r.getInt("publication_year"), r.getInt("edition"), r.getInt("storage_id"));
            books.add(s);
        }
        return books;
    }

    public List<Book> selectBooksLikeDouble(String where1, String what1,String where2, String what2){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                "  where "+ where1 +" like '%"+ what1+"%' and "+ where2 +" like '%"+ what2+"%' allow filtering;");
        ResultSet rs = session.execute(st);
        List<Book> books = new ArrayList<Book>();
        for (Row r : rs) {
            Book s = new Book(r.getInt("book_id"), r.getString("title"),
                    r.getString("author"), r.getString("science_field"), r.getString("key_words"),
                    r.getInt("publication_year"), r.getInt("edition"), r.getInt("storage_id"));
            books.add(s);
        }
        return books;
    }

    public List<Book> selectBooksLikeTitle(String what){return selectBooksLike("title", what);}
    public List<Book> selectBooksLikeAuthor(String what){return selectBooksLike("author", what);}
    public List<Book> selectBooksLikeField(String what){return selectBooksLike("science_field", what);}
    public List<Book> selectBooksLikeWords(String what){return selectBooksLike("key_words", what);}
    public List<Book> selectBooksLikeTitleAuthor(String what1, String what2){return selectBooksLikeDouble("title", what1, "author", what2);}
    public List<Book> selectBooksLikeFieldWords(String what1, String what2){return selectBooksLikeDouble("science_field", what1, "key_words", what2);}
}
