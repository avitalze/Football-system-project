package Domain.Events;

import Domain.LeagueManagment.Match;
import Domain.Users.Referee;

import java.text.ParseException;
import java.util.Date;

public class ExtraTime extends Event {

    private int extraMinute;

    public ExtraTime(Referee referee, Match match, int minutesToAdd) throws Exception {
        super(referee, match);
        if (minutesToAdd > 0) {
            super.setName("Extra Time");
            this.extraMinute = minutesToAdd;
            match.setNumOfMinutes(match.getNumOfMinutes()+minutesToAdd);
        }
        else{
            LOG.error("You can not give a negative extra time");
            throw new Exception("You can not give a negative extra time");
        }

    }

    public ExtraTime(int id, Referee referee, Match match, int minutesToAdd, Date date, int time) throws Exception {
        super(id, referee, match, date, time);
        if (minutesToAdd > 0) {
            super.setName("Extra Time");
            this.extraMinute = minutesToAdd;
            match.setNumOfMinutes(match.getNumOfMinutes()+minutesToAdd);
        }
        else{
            LOG.error("You can not give a negative extra time");
            throw new Exception("You can not give a negative extra time");
        }

    }
    @Override
    public String toString() {
        return super.getDateTime() +","+super.getMinuteOfMatch() +","+"extra time of "+extraMinute+" minutes";
    }
}
