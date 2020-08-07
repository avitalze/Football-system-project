package Domain.LeagueManagment.Scheduling;

import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Season;
import Domain.LeagueManagment.Team;
import Domain.Users.Referee;

import java.util.HashMap;
import java.util.HashSet;

public interface SchedulingPolicy {

    void assign(HashMap<League, HashSet<Team>> teamsInCurrentSeasonLeagues, Season season) throws Exception;
    String getNameOfSchedulingPolicy();
}
