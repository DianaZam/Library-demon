package requests;

import java.io.Serializable;

public class IsBookInStock implements Serializable {
    private String method;
    private int book_id =0;

    public IsBookInStock() {
    }

    public IsBookInStock(String method, int book_id){
        this.book_id=book_id;
        this.method=method;
    }

    @Override
    public String toString() {
        return "book_id: "+book_id+" method: "+method;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

}
