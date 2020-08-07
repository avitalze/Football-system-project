package Domain;

import Domain.Users.Fan;
import Domain.Users.SystemManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Observable;
public class Complaint extends Observable {
    private int id;
    /****WE NEED?*******/
    private Fan fan;
/**********/
    private MainSystem system;
    private String  content;
    private String answer;
    private List<SystemManager> systemManagers;
    private static final Logger LOG = LogManager.getLogger();

/**Eden*/
    public Complaint(Fan fan, MainSystem system) throws Exception {
        if(!system.getUsers().contains(fan)){
            LOG.error("fan not exist at system");
            throw new Exception("fan not exist at system");
        }
        this.fan = fan;
        this.system = system;
        answer=null;
        systemManagers= this.system.getSystemManagers();
        for (SystemManager s:systemManagers) {
            s.addComplaint(this);
        }
    }

    /**Eden*/
    public void send(String s){
        setChanged();
        notifyObservers(s);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fan getFan() {
        return fan;
    }

    public void setFan(Fan fan) {
        this.fan = fan;
    }

    public MainSystem getSystem() {
        return system;
    }

    public void setSystem(MainSystem system) {
        this.system = system;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public List<SystemManager> getSystemManagers() {
        return systemManagers;
    }
}

