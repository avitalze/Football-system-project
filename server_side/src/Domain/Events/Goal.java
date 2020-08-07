package Domain.Events;

import Domain.LeagueManagment.Match;
import Domain.Users.Player;
import Domain.Users.Referee;

import java.text.ParseException;
import java.util.Date;

public class Goal extends Event {

    private Player player;

    public Goal(Referee referee, Match match, Player p) throws Exception {
        super(referee, match);
        if( p != null) {
            super.setName("Goal");
            this.player = p;
            /**add score to the relevant team**/
            //player at home team
            if (match.getHomeTeam().getPlayers().contains(p)) {
                match.setHomeScore(match.getHomeScore() + 2);
            } else {
                //player at away team
                if (match.getAwayTeam().getPlayers().contains(p)) {
                    match.setGuestScore(match.getGuestScore() + 2);
                }
                else{
                    LOG.error("This player isn't in one of the participating teams");
                    throw new Exception("This player isn't in one of the participating teams");
                }
            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    public Goal(int id, Referee referee, Match match, Player p, Date date, int time) throws Exception {
        super(id, referee, match, date, time);
        if( p != null) {
            super.setName("Goal");
            this.player = p;
//            /**add score to the relevant team**/
//            //player at home team
//            if (match.getHomeTeam().getPlayers().contains(p)) {
//                match.setHomeScore(match.getHomeScore() + 2);
//            } else {
//                //player at away team
//                if (match.getAwayTeam().getPlayers().contains(p)) {
//                    match.setGuestScore(match.getGuestScore() + 2);
//                }
//                else{
//                    LOG.error("This player isn't in one of the participating teams");
//                    throw new Exception("This player isn't in one of the participating teams");
//                }
//            }
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    public Player getPlayer(){
        return player;
    }

    @Override
    public String toString() {
        return super.getDateTime() +","+super.getMinuteOfMatch() +","+"Goal by "+player.getTeamRole().getName();
    }
}
