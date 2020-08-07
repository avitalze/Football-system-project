package Domain.Events;

import Domain.LeagueManagment.Match;
import Domain.Users.Player;
import Domain.Users.Referee;

import java.text.ParseException;
import java.util.Date;

public class OffSide extends Event {

    private Player player;

    public OffSide(Referee referee, Match match, Player p) throws Exception {
        super(referee, match);
        if(p != null){
            super.setName("Off Side");
            this.player = p;
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }

    public OffSide(int id, Referee referee, Match match, Player p, Date date, int time) throws Exception {
        super(id, referee, match, date, time);
        if(p != null){
            super.setName("Off Side");
            this.player = p;
        }
        else{
            LOG.error("one of parameters null");
            throw new Exception("Please insert valid player");
        }
    }
    @Override
    public String toString() {
        return super.getDateTime() +","+super.getMinuteOfMatch() +", offSide by "+ player.getTeamRole().getName();
    }
}
