package Stubs;
import Domain.BudgetControl.BudgetControl;
import Domain.LeagueManagment.Field;
import Domain.LeagueManagment.Team;
import Domain.Users.Coach;
import Domain.Users.Player;
import Domain.Users.TeamManager;
import Domain.Users.TeamOwner;

import java.util.HashMap;
import java.util.HashSet;

public class TeamStubOr extends Team {

    public TeamStubOr(String name, HashSet<Player> players, Coach coach, Field field, TeamOwner founder) throws Exception {
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
    public TeamStubOr (String name,boolean isActive){
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
        setActive(isActive);
        this.budgetControl = new BudgetControl(this);
    }

    @Override
    public void becomeActive(HashSet<Player> players, Coach coach, Field field) {
        return;
    }

    @Override
    public void deleteTeamByTeamOwner() {
        return;
    }

    @Override
    public void reopenTeam(HashSet<Player> players, Coach coach, Field field, TeamOwner newFounder) {
        return;
    }

    @Override
    public void addTeamOwner(TeamOwner tO)  {
        return;
    }

    @Override
    public void removeTeamOwner(TeamOwner tO) throws Exception {
        return;
    }

    @Override
    public void removeTeamManager(TeamManager tM) throws Exception {
        return;
    }

    @Override
    public void addPlayer(Player p) throws Exception {
        return;
    }

    @Override
    public void removePlayer(Player p) throws Exception {
        return;
    }

    @Override
    public void addExpense(String typeOfExpense, long amount) throws Exception {
        return;
    }

    @Override
    public void addIncome(String typeOfIncome, long amount) throws Exception {
        return;
    }

    @Override
    public void removeField(Field f) throws Exception {
        return;
    }

    @Override
    public void setField(Field field) {
        return;
    }
}
