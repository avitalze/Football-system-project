package Domain.BudgetControl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BudgetReport {
    private String reason;
    private long amount;
    private Date now;
    private DateFormat dateFormat= new SimpleDateFormat("dd/mm/yyyy");

    public BudgetReport(String reason, long amount) {
        this.reason = reason;
        this.amount = amount;
        this.now = new Date();
        dateFormat.format(this.now);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getNow() {
        return now;
    }

    public void setNow(Date now) {
        this.now = now;
    }
}
