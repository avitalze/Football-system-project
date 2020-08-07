package Domain.LeagueManagment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;

public class Field {

    private String nameOfField;
    private HashSet<Match> matches;
    private Team team;
    private static final Logger LOG = LogManager.getLogger("Field");
    /******possible attribute*********/
    //private boolean isFree;

    public Field(String nameOfField, HashSet<Match> matches, Team team) {
        this.nameOfField = nameOfField;
        this.matches = matches;
        this.team = team;
    }

    public Field(String nameOfField) {
        this.nameOfField = nameOfField;
        matches = new HashSet<>();
    }


    //<editor-fold desc="getters and setters">
    public String getNameOfField() { return nameOfField; }

    public HashSet<Match> getMatches() { return matches; }

    public Team getTeam() { return team; }

    public void setNameOfField(String nameOfField) { this.nameOfField = nameOfField; }

    public void setMatches(HashSet<Match> matches) { this.matches = matches; }

    public void setTeam(Team team) { this.team = team; }

    public void addTeam(Team team){
        this.team = team;
    }
    public void removeTeam(Team team){
        if(this.team.equals(team)){
            this.team = null;
        }
    }
    //</editor-fold>

}
