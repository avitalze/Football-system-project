package Domain.Events;

import Domain.LeagueManagment.Match;
import Domain.Users.Player;
import Domain.Users.Referee;

import java.text.ParseException;
import java.util.Date;

public class YellowCard extends Event {

    private Player player;
    public YellowCard(Referee referee, Match match, Player p) throws Exception {
        super(referee, match);
        if(p != null){
            this.player = p;
            super.setName("Yellow Card");
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    public YellowCard(int id, Referee referee, Match match, Player p, Date date, int time) throws Exception {
        super(id, referee, match, date, time);
        if(p != null){
            this.player = p;
            super.setName("Yellow Card");
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    @Override
    public String toString() {
        return super.getDateTime() +","+super.getMinuteOfMatch() +","+"Yellow card to "+player.getTeamRole().getName();
    }
}
