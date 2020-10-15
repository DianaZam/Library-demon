package casshelper.library;

import java.io.Serializable;

public class Book implements Serializable {
    private int book_id;
    private String title;
    private String author;
    private String science_field;
    private String key_words;
    private int publication_year;
    private int edition;
    private int storage_id;

    public Book() {
    }

    public Book(int book_id, String title, String author, String science_field,
         String key_words, int publication_year, int edition, int storage_id){
        this.book_id=book_id;
        this.title=title;
        this.author=author;
        this.science_field=science_field;
        this.key_words=key_words;
        this.publication_year=publication_year;
        this.edition=edition;
        this.storage_id=storage_id;
    }

    @Override
    public String toString() {
        return "Book: "+book_id+", "+title+", "+author+", "+science_field+", "+key_words+", "+publication_year
                +", "+edition+", "+storage_id;
    }

    public int getBook_id() {
        return book_id;
    }
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getScience_field() {
        return science_field;
    }
    public void setScience_field(String science_field) {
        this.science_field = science_field;
    }

    public String getKey_words() {
        return key_words;
    }
    public void setKey_words(String key_words) {
        this.key_words = key_words;
    }

    public int getPublication_year() {
        return publication_year;
    }
    public void setPublication_year(int publication_year) {
        this.publication_year = publication_year;
    }

    public int getEdition() {
        return edition;
    }
    public void setEdition(int edition) {
        this.edition = edition;
    }

    public int getStorage_id() {
        return storage_id;
    }
    public void setStorage_id(int storage_id) {
        this.storage_id = storage_id;
    }
}
