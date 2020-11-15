package library;

import java.io.Serializable;
import java.util.Date;

public class BookStatus implements Serializable {
    private int book_id;
    private boolean in_stock;
    private Date status_id;
    private int location_id;

    public BookStatus() {
    }

    public BookStatus(int book_id, boolean in_stock, int location_id){
        this.book_id=book_id;
        this.in_stock=in_stock;
        this.location_id=location_id;
    }

    public BookStatus(int book_id, boolean in_stock, Date status_id, int location_id){
        this.book_id=book_id;
        this.in_stock=in_stock;
        this.status_id = status_id;
        this.location_id=location_id;
    }

    @Override
    public String toString() {
        return "BookStatus: "+book_id+", "+in_stock+", "+status_id.toString()+", "+location_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public boolean  getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(boolean in_stock) {
        this.in_stock = in_stock;
    }

    public int getLocation_id() {
        return location_id;
    }

    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public Date getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Date status_id) {
        this.status_id = status_id;
    }

}
