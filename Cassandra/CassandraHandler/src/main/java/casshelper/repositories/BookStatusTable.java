package casshelper.repositories;

import casshelper.library.BookStatus;
import com.datastax.driver.core.*;
import java.util.ArrayList;
import java.util.List;

public class BookStatusTable extends Table {
    public BookStatusTable(Session session) {
        super(session);
        TABLE_NAME = "book_status";
    }

    @Override
    public void createTable() {
        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(")
                .append("book_id int, ").append("in_stock boolean,").append("status_id timestamp,")
                .append("location_id int,").append("PRIMARY KEY((book_id, in_stock), status_id)").append(");");

        String query = sb.toString();
        session.execute(query);
    }

    public void insertBookStatus(BookStatus bookStatus){
        Statement st = new SimpleStatement("INSERT INTO "+TABLE_NAME+
                " (book_id, in_stock, status_id, location_id)  VALUES ("+bookStatus.getBook_id()+", "
                +bookStatus.getIn_stock()+", toTimeStamp(now()) ,"+bookStatus.getLocation_id()+");");
        session.execute(st);
    }

    public void deleteBookStatusOnId(BookStatus bookStatus){
        Statement st = new SimpleStatement("DELETE FROM "+TABLE_NAME+
                " WHERE status_id = "+bookStatus.getStatus_id().getTime()+");");
        session.execute(st);
    }


    public List<BookStatus> selectBookStatus(){
        Statement st = new SimpleStatement("SELECT * FROM "+TABLE_NAME+";");
        ResultSet rs = session.execute(st);
        List<BookStatus> bookStatuses = new ArrayList<BookStatus>();

        for (Row r : rs) {
            BookStatus s = new BookStatus(r.getInt("book_id"), r.getBool("in_stock"),
                    r.getTimestamp("status_id"), r.getInt("location_id"));
            bookStatuses.add(s);
        }

        return bookStatuses;
    }


}
