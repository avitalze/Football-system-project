package Stubs;

import Domain.BudgetControl.BudgetControl;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.Users.Coach;
import Domain.Users.Player;
import Domain.Users.TeamOwner;

import java.util.HashMap;
import java.util.HashSet;

public class TeamStub extends Team {

    public TeamStub(String name, HashSet<Player> players, Coach coach, Field field, TeamOwner founder) throws Exception {
        if(players.size() < 11){
            throw new Exception("The number of players are less than 11");
        }
        setLeaguePerSeason(new HashMap<>());
        setName(name);
        setPlayers(players);
        setCoach(coach);
        setTeamManager(null);
        HashSet<TeamOwner> teamOwners=new HashSet<>();
        teamOwners.add(founder);
        setTeamOwners(teamOwners);
        setField(field);
        setFounder(founder);
        this.budgetControl = new BudgetControl(this);
    }

    //ONLY FOR TEST
    public TeamStub (String name){
        setLeaguePerSeason(new HashMap<>());
        setName(name);
        setPlayers(new HashSet<>());
        setCoach(null);
        setTeamManager(null);
        HashSet<TeamOwner> teamOwners=new HashSet<>();
        teamOwners.add(null);
        setTeamOwners(teamOwners);
        setField(null);
        setFounder(null);
        setActive(true);
        this.budgetControl = new BudgetControl(this);
    }

}
