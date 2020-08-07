package Domain;

import Domain.LeagueManagment.Team;
import Domain.Users.TeamRole;

public class TeamSubscription {

    public Object role;
    public Team team;
    public TeamRole user;

    public TeamSubscription(Object role, Team team, TeamRole user) {
        this.role = role;
        this.team = team;
        this.user = user;
    }

}

