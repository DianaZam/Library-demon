package casshelper.repositories;


import library.Reader;
import com.datastax.driver.core.*;
import java.util.ArrayList;
import java.util.List;

public class ReadersTable extends Table{

    public ReadersTable(Session session) {
        super(session);
        TABLE_NAME="readers";
    }

    /**
     * Creates the books table.
     *
     * LIKE works for first_name, middle_name, last_name
     *
     */
    @Override
    public void createTable() {
        StringBuilder sb1 = new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(TABLE_NAME).append("(")
                .append("card_id int PRIMARY KEY, ").append("passport int,").append("first_name text,")
                .append("middle_name text,").append("last_name text,").append("birthday date").append(");");

        String query = sb1.toString();
        session.execute(query);
        query = createCustomId("first_name");
        session.execute(query);
        query = createCustomId("middle_name");
        session.execute(query);
        query = createCustomId("last_name");
        session.execute(query);
    }

    public void insert(Reader reader){
        Statement st = new SimpleStatement("INSERT INTO "+TABLE_NAME+
                " (card_id, passport, first_name, middle_name, last_name, birthday)  VALUES ("
                +reader.getCard_id() +", "+reader.getPassport()+", '"+reader.getFirst_name()
                +"', '"+reader.getMiddle_name()+"', '"+reader.getLast_name()
                +"', '"+reader.getBirthday() .toString()+"');");
        session.execute(st);
    }

    public void delete(Reader reader){
        Statement st = new SimpleStatement("DELETE FROM " + TABLE_NAME +
                " WHERE card_id = " + reader.getCard_id() + ";");
        session.execute(st);
    }

    public List<Reader> selectAll(){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+";");
        ResultSet rs = session.execute(st);
        List<Reader> readers = new ArrayList<Reader>();
        for (Row r : rs) {
            Reader s = new Reader(r.getInt("card_id"), r.getInt("passport"),  r.getString("first_name"),
                    r.getString("middle_name"), r.getString("last_name"), r.getDate("birthday"));
            readers.add(s);
        }
        return readers;
    }

    public List<Reader> selectReadersLikeFIO(String first, String middle, String last){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                "  where first_name like '%"+ first+"%' and middle_name like '%"+ middle+"%' and"+
                "  last_name like '%"+ last+"%' allow filtering;");
        ResultSet rs = session.execute(st);
        List<Reader> readers = new ArrayList<Reader>();
        for (Row r : rs) {
            Reader s = new Reader(r.getInt("card_id"), r.getInt("passport"),  r.getString("first_name"),
                    r.getString("middle_name"), r.getString("last_name"), r.getDate("birthday"));
            readers.add(s);
        }
        return readers;
    }

    public List<Reader> selectReadersLikeFI(String first, String last){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                "  where first_name like '%"+ first+"%' and"+
                "  last_name like '%"+ last+"%' allow filtering;");
        ResultSet rs = session.execute(st);
        List<Reader> readers = new ArrayList<Reader>();
        for (Row r : rs) {
            Reader s = new Reader(r.getInt("card_id"), r.getInt("passport"),  r.getString("first_name"),
                    r.getString("middle_name"), r.getString("last_name"), r.getDate("birthday"));
            readers.add(s);
        }
        return readers;
    }

    public List<Reader> selectReadersWhereID(int card_id){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                " where card_id="+card_id+";");
        ResultSet rs = session.execute(st);
        List<Reader> readers = new ArrayList<Reader>();
        for (Row r : rs) {
            Reader s = new Reader(r.getInt("card_id"), r.getInt("passport"),  r.getString("first_name"),
                    r.getString("middle_name"), r.getString("last_name"), r.getDate("birthday"));
            readers.add(s);
        }
        return readers;
    }

    public List<Reader> selectReadersWherePassport(int passport){
        Statement st = new SimpleStatement("select * from "+TABLE_NAME+
                " where passport="+passport+" allow filtering;");
        ResultSet rs = session.execute(st);
        List<Reader> readers = new ArrayList<Reader>();
        for (Row r : rs) {
            Reader s = new Reader(r.getInt("card_id"), r.getInt("passport"),  r.getString("first_name"),
                    r.getString("middle_name"), r.getString("last_name"), r.getDate("birthday"));
            readers.add(s);
        }
        return readers;
    }

}
