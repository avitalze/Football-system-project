package Domain.Events;

import Domain.LeagueManagment.Match;
import Domain.Users.Player;
import Domain.Users.Referee;

import java.util.Date;

public class Replacement extends Event {
    Player firstPlayer;
    Player secondPlayer;

    public Replacement(Referee referee, Match match, Player p1, Player p2) throws Exception {
        super(referee, match);

        if(p1 != null && p2 != null) {
            /**implement replacement**/
            this.firstPlayer = p1;
            this.secondPlayer = p2;
            //p1 is player in away team
            if (match.getAwayTeam().getPlayers().contains(p1)) {
                if(match.getAwayTeam().getPlayers().contains(p2)) {
                    super.setName("Replacement");
                }
                else{
                    throw new Exception("Both players must to be from the same team");
                }
            } else {
                if (match.getHomeTeam().getPlayers().contains(p1)) {
                    if(match.getHomeTeam().getPlayers().contains(p2)) {
                        super.setName("Replacement");
                    }
                    else{
                        throw new Exception("Both players must to be from the same team");
                    }
                }
                else{
                    throw new Exception("The player to replace isn't in one of the participating teams");
                }
            }
        }
        else{
            throw new Exception("Please insert valid players");
        }

    }

    public Replacement(int id, Referee referee, Match match, Player p1, Player p2, Date date, int time) throws Exception {
        super(id, referee, match, date, time);

        if(p1 != null && p2 != null) {
            /**implement replacement**/
            this.firstPlayer = p1;
            this.secondPlayer = p2;
            super.setName("Replacement");

        }
        else{
            throw new Exception("Please insert valid players");
        }

    }

    @Override
    public String toString() {
        return super.getDateTime() +","+super.getMinuteOfMatch() +", replace "+ firstPlayer.getTeamRole().getName()+" by " + secondPlayer.getTeamRole().getName();
    }
}
