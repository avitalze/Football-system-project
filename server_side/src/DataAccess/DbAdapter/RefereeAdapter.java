package DataAccess.DbAdapter;

import Domain.Events.Event;
import Domain.Users.Fan;
import Domain.Users.Referee;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

public class RefereeAdapter implements DbObject<Referee>{
    @Override
    public Referee ToObj(List<String> fields) throws Exception {
        try {
            return new Referee(fields.get(1),fields.get(3),fields.get(4),fields.get(0),fields.get(2),FanAdapter.birthDateFormat.parse(fields.get(5)),fields.get(7));
        }
        catch (ParseException e){
            throw new Exception ("date format is wrong ");
        }
    }

    @Override
    public List<String> ToDB(Referee obj) {
        return null;
    }
}
