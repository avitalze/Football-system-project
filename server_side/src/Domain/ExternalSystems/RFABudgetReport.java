package Domain.ExternalSystems;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RFABudgetReport {

    private String teamName;
    private double amount;
    private String date ;

    public RFABudgetReport(String teamName, double amount, String date) {
        this.teamName = teamName;
        this.amount = amount;
        this.date = date;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
