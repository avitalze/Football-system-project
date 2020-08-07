package DataAccess;

import java.sql.SQLException;
import java.util.List;
 /***    !!!!!!!!!!!!!!!!!!!!   not to do !!!!!!!!!!!! ************/
public class DaoNotificationsRfa implements Dao<String> {
    @Override
    public List<String> get(List<String> keys) {
        return null;
    }

    @Override
    public List<List<String>> getAll(String collName, String filter) {
        return null;
    }

    @Override
    public void save(List<String> strings) throws SQLException {

    }

    @Override
    public void update(List<String> keys, List<String> string) {
    }

    @Override
    public void delete(List<String> strings) {

    }
}
