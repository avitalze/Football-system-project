package Domain.LeagueManagment.Calculation;

import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Team;

import java.util.HashMap;
import java.util.HashSet;

public interface CalculationPolicy {

    void calculate(HashMap<League, HashSet<Team>> teamsInSeason);
    String getNameOfCalculationPolicy();
}
