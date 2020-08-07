package Domain.BudgetControl;

import Domain.LeagueManagment.Team;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

public class BudgetControl {
    private static final Logger LOG = LogManager.getLogger("BudgetControl");
    private long balance;
    private LinkedList<BudgetReport> incomeAndExpenses;
    private Team team;

    public BudgetControl(Team team){
        incomeAndExpenses = new LinkedList<>();
        balance = 0;
        this.team=team;
    }

    /**OR**/
    //TODO test - V
    public void addIncome(String typeOfIncome, long amount) throws Exception {
        if(typeOfIncome ==null || typeOfIncome.length()==0){
            LOG.error("one of parameters null");
            throw new Exception("type of income not valid");
        }
        if(amount<=0){
            LOG.error("amount of income is negative");
            throw new Exception("amount of income is negative");
        }
        incomeAndExpenses.add(new BudgetReport(typeOfIncome,amount));
        balance += amount;
        LOG.info(String.format("%s - %s", this.team.getName(), "add income to team budget, type: "+typeOfIncome+" amount: "+amount));
    }

    /**OR**/
    //TODO test - V
    public void addExpense(String typeOfExpense, long amount) throws Exception{
        if(typeOfExpense ==null || typeOfExpense.length()==0){
            LOG.error("one of parameters null");
            throw new Exception("type of expense not valid");
        }
        if(amount<=0){
            LOG.error("amount of expense is negative");
            throw new Exception("amount of expense is negative");
        }
        if (balance - amount >= 0) {
            amount = amount - amount * 2;// make the amount negative
            incomeAndExpenses.add(new BudgetReport(typeOfExpense, amount));
            balance = balance + amount;// the amount in negative
        }
        else{
            LOG.error("Budget cannot be less than 0");
            throw new Exception("Budget cannot be less than 0");
        }
        LOG.info(String.format("%s - %s", this.team.getName(), "add expense to team budget, type: "+typeOfExpense+" amount: "+amount));
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public LinkedList<BudgetReport> getIncomeAndExpenses() {
        return incomeAndExpenses;
    }

    public void setIncomeAndExpenses(LinkedList<BudgetReport> incomeAndExpenses) {
        this.incomeAndExpenses = incomeAndExpenses;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
