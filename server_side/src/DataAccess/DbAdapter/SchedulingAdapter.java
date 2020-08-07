package DataAccess.DbAdapter;

import DataAccess.Dao;
import Domain.LeagueManagment.Scheduling.SchedualeOption1;
import Domain.LeagueManagment.Scheduling.SchedualeOption2;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;

import java.util.List;

public class SchedulingAdapter implements DbObject<SchedulingPolicy> {
    @Override
    public SchedulingPolicy ToObj(List<String> fields) throws Exception {

        if(fields.get(0).equals("SchedualeOption1")){
            return new SchedualeOption1();
        }
        else{
            if(fields.get(0).equals("SchedualeOption2")){
                return new SchedualeOption2();
            }
        }

        return null;
    }

    @Override
    public List<String> ToDB(SchedulingPolicy obj) {
        return null;
    }
}
