package DataAccess.DbAdapter;

import Domain.LeagueManagment.Calculation.CalculateOption1;
import Domain.LeagueManagment.Calculation.CalculationPolicy;

import java.util.List;

public class CalculationAdapter implements DbObject<CalculationPolicy> {
    @Override
    public CalculationPolicy ToObj(List<String> fields) throws Exception {

        if(fields.get(0).equals("CalculateOption1")){
            return new CalculateOption1();
        }

        return null;

    }

    @Override
    public List<String> ToDB(CalculationPolicy obj) {
        return null;
    }
}
