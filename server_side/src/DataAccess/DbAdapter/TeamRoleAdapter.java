package DataAccess.DbAdapter;

import Domain.MainSystem;
import Domain.Users.*;

import java.util.List;

public class TeamRoleAdapter implements DbObject<TeamRole> {
    @Override
    public TeamRole ToObj(List<String> fields) throws Exception {

        TeamRole teamRole = new TeamRole(MainSystem.getInstance(), fields.get(1),fields.get(3),fields.get(4),fields.get(0),fields.get(2),
                MainSystem.birthDateFormat.parse(fields.get(5)));

        //player
        if(fields.get(7).equals("1")){
//            Player player = new Player(teamRole);
            teamRole.becomePlayer();
        }

        //coach
        if(fields.get(8).equals("1")){
//            Coach coach = new Coach(teamRole);
            teamRole.becomeCoach();
        }

        //team owner
        if(fields.get(9).equals("1")){
//            TeamOwner teamOwner = new TeamOwner(teamRole);
            teamRole.becomeTeamOwner();
        }

        //team manager
        if(fields.get(10).equals("1")){
//            TeamManager teamManager = new TeamManager(teamRole, null, null);
            teamRole.becomeTeamManager();
        }

        return teamRole;
    }

    @Override
    public List<String> ToDB(TeamRole obj) {
        return null;
    }
}
