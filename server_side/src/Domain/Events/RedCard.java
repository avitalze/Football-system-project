package Domain.Events;

import Domain.LeagueManagment.Match;
import Domain.Users.Player;
import Domain.Users.Referee;

import java.text.ParseException;
import java.util.Date;

public class RedCard extends Event {

    private Player player;

    public RedCard(Referee referee, Match match, Player p) throws Exception {
        super(referee, match);
        if(p != null){
            super.setName("Red Card");
            this.player = p;
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    public RedCard(int id, Referee referee, Match match, Player p, Date date, int time) throws Exception {
        super(id, referee, match, date, time);
        if(p != null){
            super.setName("Red Card");
            this.player = p;
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    @Override
    public String toString() {
        return super.getDateTime() +","+super.getMinuteOfMatch() +","+"Red card to "+ player.getTeamRole().getName();
    }
}
