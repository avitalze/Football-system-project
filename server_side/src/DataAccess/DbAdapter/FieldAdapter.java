package DataAccess.DbAdapter;

import DataAccess.DbAdapter.DbObject;
import Domain.LeagueManagment.Field;

import java.util.List;

public class FieldAdapter implements DbObject<Field> {
    @Override
    public Field ToObj(List<String> fields) throws Exception {
        return new Field(fields.get(0));
    }

    @Override
    public List<String> ToDB(Field obj) {
        return null;
    }
}
