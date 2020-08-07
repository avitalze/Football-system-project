package Domain.ExternalSystems;

public interface RFABudgetControlInterface {

    public boolean addPayment(String teamName, String date, double amount) throws Exception;
}
