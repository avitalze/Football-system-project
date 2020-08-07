package Domain.LeagueManagment.Calculation;

import Domain.LeagueManagment.Calculation.CalculationPolicy;
import Domain.LeagueManagment.League;
import Domain.LeagueManagment.Match;
import Domain.LeagueManagment.Team;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class CalculateOption1 implements CalculationPolicy {
    //TODO test - V
    @Override
    public String getNameOfCalculationPolicy(){
        return "CalculateOption1";
    }


    /**
     * This function gets hash of each league and all the teams inside her
     * and calculate the score of each team in the leagues table
     * give 2 points for winning, 1 for tiko for both teams and 0 for loosing
     * @param teamsInSeason
     * @CodeBy yarden
     */
    @Override
    public void calculate(HashMap<League, HashSet<Team >> teamsInSeason) {

        /**For each League**/
        for(League l: teamsInSeason.keySet()){

            HashSet<Team> teamsPerLeague = teamsInSeason.get(l);
            /**For each team in league**/
            for (Team team: teamsPerLeague) {
                /**check each home match the team won and add +2 to her score if tiko, add +1**/
                HashSet<Match> homeMatchesPerTeam = team.getHome();
                for (Match m: homeMatchesPerTeam) {
                    if(m.getStartDate().before(new Date())) {
                        if (m.getHomeScore() > m.getGuestScore()) {//team is won
                            team.setScore(team.getScore() + 2);
                        } else {
                            if (m.getHomeScore() == m.getGuestScore()) {//there is tiko
                                team.setScore(team.getScore() + 1);
                            }
                        }
                        //about lose do not add nothing
                    }
                }

                HashSet<Match> awayMatchesPerTeam = team.getAway();
                for (Match m: awayMatchesPerTeam) {
                    if(m.getStartDate().before(new Date())) {
                        if (m.getHomeScore() < m.getGuestScore()) {//team is won
                            team.setScore(team.getScore() + 2);
                        } else {
                            if (m.getHomeScore() == m.getGuestScore()) {//there is tiko
                                team.setScore(team.getScore() + 1);
                            }
                        }
                        //about lose do not add nothing
                    }
                }

            }// team

        }//league

    }


}
