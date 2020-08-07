package Domain.ExternalSystems;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

public class RFABudgetControl implements RFABudgetControlInterface {

    private static final Logger LOG = LogManager.getLogger("RFABudgetControl");
    private double balance;
    private LinkedList<RFABudgetReport> payments;

    public RFABudgetControl() {
        this.balance = 0;
        payments=new LinkedList<>();
    }

    @Override
    public boolean addPayment(String teamName, String date, double amount) {
        if( teamName==null || teamName.length()==0 || date==null || date.length()==0){
            LOG.error("one of parameters null");
            return  false;
        }
        if(amount<=0){
            LOG.error("amount of income is negative");
            return false;
        }
        payments.add(new RFABudgetReport(teamName,amount,date));
        balance += amount;
        LOG.info(String.format("%s - %s", teamName, "add income to RFA payment systemr budget, date: "+date+" amount: "+amount));
        return true;
    }


}
