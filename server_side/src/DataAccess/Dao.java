package DataAccess;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

public interface Dao <T> {

    /**\
     * get specific row from DB by key
     * @param keys - list of keys of the table
     * @return list of all the vules of selected row
     * @throws ParseException if primary key or foreign key not exist
     */
    List<T> get(List<T> keys) throws ParseException;

    /**
     * select all the rows values that Satisfies the filter
     * getAll (null ,null) -> get all rows in table (no filter)
     * @param collName - name of coll we want to filter
     * @param filter - value of the coll
     * @return list of all the rows that Satisfies the filter
     */
    List<List<T>> getAll(T collName, T filter);

    /**
     * add new row to table
     * @param strings list of all the values of new row
     * @throws SQLException  if primary key or foreign key not exist
     */
    void save(List<T> strings) throws SQLException;

    /**
     * update exist row in table
     * @param t list of keys
     * @param string list of rest of values of new row (not contains keys!)
     */
    void update(List<T> t, List<T> string);

    /**
     * delete row from table
     * @param strings list of keys of the row
     */
    void delete(List<T> strings);
}

