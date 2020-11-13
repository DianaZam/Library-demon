package requests;

import java.io.Serializable;

public class CGBookStatus  implements Serializable {
    private String method;
    private int book_id;
    private int location_id;

    public CGBookStatus() {
    }

    public CGBookStatus(int book_id, int location_id, String method){
        this.book_id=book_id;
        this.location_id=location_id;
        this.method=method;
    }

    public int getBook_id() {
        return book_id;
    }
    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getLocation_id() {
        return location_id;
    }
    public void setLocation_id(int location_id) {
        this.location_id = location_id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
