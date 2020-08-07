package DataAccess.DbAdapter;

import Domain.LeagueManagment.League;
import Domain.MainSystem;

import java.util.List;

public class LeagueAdapter implements DbObject<League> {
    @Override
    public League ToObj(List<String> fields) throws Exception {
        return new League(fields.get(0), MainSystem.getInstance());
    }

    @Override
    public List<String> ToDB(League obj) {
        return null;
    }
}
