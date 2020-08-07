package DataAccess.DbAdapter;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public interface DbObject<T> {
    
    T ToObj(List<String> fields) throws Exception;
    List<String> ToDB(T obj);
}
