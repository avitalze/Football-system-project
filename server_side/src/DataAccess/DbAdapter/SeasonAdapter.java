package DataAccess.DbAdapter;

import Domain.LeagueManagment.Season;
import Domain.MainSystem;

import java.util.List;

public class SeasonAdapter implements DbObject<Season>{
    @Override
    public Season ToObj(List<String> fields) throws Exception {

        Season s = new Season(MainSystem.getInstance(), null, null, Integer.parseInt(fields.get(0)));
        if(fields.get(3).equals("1")){
            MainSystem.getInstance().setCurrSeason(s);
        }

        return s;
    }

    @Override
    public List<String> ToDB(Season obj) {
        return null;
    }
}
