package Domain.LeagueManagment.Scheduling;

import Domain.Events.Event;
import Domain.LeagueManagment.*;
import Domain.LeagueManagment.Scheduling.SchedulingPolicy;
import Domain.MainSystem;
import Domain.Users.Referee;
import org.apache.commons.lang3.time.DateUtils;

import java.sql.Ref;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SchedualeOption1 implements SchedulingPolicy {


    //TODO test

    @Override
    public String getNameOfSchedulingPolicy(){
        return "SchedualeOption1";
    }


    /**
     * This function gets hash of each league and all the teams inside her, hash of referees and main referee
     * and create matches for teams according to this policy -
     * Each pair of teams will play against each other only once in a season.
     * start date of match - a week from today at 8PM and all the other matches after the first create
     * will be two-day differentials.
     * the referees refereeing at all the matches in the season
     * @param teamsInSeason
//     * @param referees
//     * @param mainRef
     * @throws Exception
     */
    @Override
    public void assign(HashMap<League, HashSet<Team>> teamsInSeason, Season season) throws Exception {

        Date date = new Date(System.currentTimeMillis());
        Date afterAweek = DateUtils.addDays(date,7);
        SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy 20:00:00");
        String dateString;

        /**For each League**/
        for (League l : teamsInSeason.keySet()) {

            /**devide the referees to mainRef and other**/
            LinkedHashSet<Referee> refereeLinkedHashSet = l.getRefereesInLeague().get(season);

            ArrayList <Referee> mainRefArray = new ArrayList();//hold one main ref
            HashSet<Referee> allReffToPutInMatch = new HashSet<>();//hold 2 referees to put in match
            Iterator<Referee> refItr = refereeLinkedHashSet.iterator();
            int index=0;
            while(refItr.hasNext()){
                if(index==0){
                    Referee mainRef = (Referee) refItr.next();
                    mainRefArray.add(0,mainRef);
                }
                else{
                    allReffToPutInMatch.add(refItr.next());
                }
                index++;
            }
            /*********************************************/

            HashSet<Team> teamsPerLeague = teamsInSeason.get(l);
            /**For each team in league**/
            ArrayList<Team> arrayList = new ArrayList<>();
            for (Team currentTeam : teamsPerLeague) {
                arrayList.add(currentTeam);
            }

            for(int i=0;i<arrayList.size();i++){

                Team currentHomeTeam = arrayList.get(i);

                for(int j=i+1;j<arrayList.size();j++){
                    Team currentAwayTeam = arrayList.get(j);
                    //create new match
                    dateString = dt.format(afterAweek);
                    Match match = new Match(0,0,currentAwayTeam, currentHomeTeam, currentHomeTeam.getField()
                            ,new HashSet<Event>(),allReffToPutInMatch,mainRefArray.get(0),dateString);
                    //insert the match to teams list
                    currentHomeTeam.addMatchToHomeMatches(match);
                    currentAwayTeam.addMatchToAwayMatches(match);

                    //update the date 2 days after, at the same hour
                    afterAweek = DateUtils.addDays(afterAweek,2);

                }
            }
        }
    }
}
