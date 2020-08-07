package Domain.LeagueManagment;

import Domain.MainSystem;
import Domain.Users.Referee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.font.GlyphLayout;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

public class League {

    private String name;
    private MainSystem mainSystem; //1

    private static final Logger LOG = LogManager.getLogger("League");
    /**
     * hold the teams in this league and in specific Seasons
     */
    private HashMap<Season, HashSet<Team>> teamsInSeason;

    private HashMap<Season, LinkedHashSet<Referee>> refereesInLeague;

    public League(String name, MainSystem mainSystem, Season currSeason) throws Exception {
        this.name = name;
        teamsInSeason=new HashMap<>();
        refereesInLeague = new HashMap<>();
        this.mainSystem = mainSystem;
        if(! mainSystem.addLeague(this)){
            LOG.error("There is already league with the same name");
            throw new Exception("There is already league with the same name");
        }
    }

    public League(String name, MainSystem mainSystem) throws Exception {
        if (name != null && mainSystem != null) {
            this.name = name;
            this.mainSystem = mainSystem;
            teamsInSeason = new HashMap<>();
            refereesInLeague = new HashMap<>();
            //if cannot ad the league, there is league with the same name
            if(! mainSystem.addLeague(this)){
                LOG.error("There is already league with the same name");
                throw new Exception("There is already league with the same name");
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Invalid parameters");
        }
    }

    //<editor-fold desc="getter ans setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MainSystem getMainSystem() {
        return mainSystem;
    }

    public void setMainSystem(MainSystem mainSystem) {
        this.mainSystem = mainSystem;
    }

    public void setTeamsInSeason(HashMap<Season, HashSet<Team>> teamsInSeason) {
        this.teamsInSeason = teamsInSeason;
    }

    public HashMap<Season, LinkedHashSet<Referee>> getRefereesInLeague() {
        return refereesInLeague;
    }

    public void setRefereesInLeague(HashMap<Season,LinkedHashSet<Referee>> refereesInLeague) {
        this.refereesInLeague = refereesInLeague;
    }

    //</editor-fold>

    public void setRefereePerSeasonToHash(Season s, Referee referee){
        refereesInLeague.get(s).add(referee);
    }

    /**
     * Add teams by Season to this league.
     * also add to input season the input league with the input teams.
     * @param season
     * @param teams teams to add to the current league with the league
     * @codeBy Eden
     */
    //TODO test - V
    public void addSeasonWithTeams(Season season, HashSet<Team> teams) {

        if(season==null || teams==null){
            LOG.error("one of parameters null");
            throw new NullPointerException();
        }
        /**check if season already exist**/
        if(!teamsInSeason.containsKey(season)) {
            teamsInSeason.put(season, teams);
            season.addLeagueWithTeams(this,teams);
        }
        else{
            /**if season already exist- check is it already hold the input teams*/
            if(!teamsInSeason.get(season).equals(teams)) {
                teams.addAll(teamsInSeason.get(season));
                teamsInSeason.put(season, teams);
                season.addLeagueWithTeams(this,teams);
            }
        }
    }

    public HashMap<Season, HashSet<Team>> getTeamsInSeason() {
        return teamsInSeason;
    }

    @Override
    public boolean equals(Object o){

        if (!(o instanceof League)) {
            return false;
        }
        League newLeague = (League)o;
        if(this.getName().equals(newLeague.getName())){
            return true;
        }
        else{
            return false;
        }
    }

}