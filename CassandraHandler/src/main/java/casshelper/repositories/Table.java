package casshelper.repositories;

import com.datastax.driver.core.*;


public abstract class Table {
    protected String TABLE_NAME;
    protected Session session;

    public Table(Session session) {
        this.session = session;
    }

    /**
     * Creates the table.
     */
    public abstract void createTable();

    /**
     * Alters the table  and adds an extra column.
     */
    public void alterTable(String columnName, String columnType) {
        StringBuilder sb = new StringBuilder("ALTER TABLE ").append(TABLE_NAME).append(" ADD ").append(columnName).append(" ").append(columnType).append(";");
        final String query = sb.toString();
        session.execute(query);
    }

    /**
     * Delete table.
     **/
    public void deleteTable() {
        StringBuilder sb = new StringBuilder("DROP TABLE IF EXISTS ").append(TABLE_NAME+";");
        final String query = sb.toString();
        session.execute(query);
    }

    protected String createCustomId(String column){
        StringBuilder sb = new StringBuilder("CREATE CUSTOM INDEX  IF NOT EXISTS ").append(column+"_id ON library."+TABLE_NAME+"("+ column+")" )
                .append(" USING 'org.apache.cassandra.index.sasi.SASIIndex' ")
                .append("WITH OPTIONS = {" +
                        "'mode': 'CONTAINS', " +
                        "'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.NonTokenizingAnalyzer', " +
                        "'case_sensitive': 'false'};");

        return sb.toString();
    }


}
