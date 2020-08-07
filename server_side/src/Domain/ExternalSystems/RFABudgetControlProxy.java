package Domain.ExternalSystems;

public class RFABudgetControlProxy implements RFABudgetControlInterface {

    private RFABudgetControlInterface rfaBudgetControl;


    public RFABudgetControlProxy(RFABudgetControlInterface rfaBudgetControl) {
        this.rfaBudgetControl= rfaBudgetControl;
    }


    @Override
    public boolean addPayment(String teamName, String date, double amount) throws Exception {
        if(rfaBudgetControl==null || rfaBudgetControl==this){
            return false;
        }
        else{
            return rfaBudgetControl.addPayment(teamName,date,amount);
        }
    }

    public boolean switchBudgetControls(RFABudgetControlInterface budgetControlInterface){
        if(budgetControlInterface== this){
            return false;
        }
        else{
            rfaBudgetControl=budgetControlInterface;
            return true;
        }
    }
}
