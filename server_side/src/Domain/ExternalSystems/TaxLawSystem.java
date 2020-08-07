package Domain.ExternalSystems;

public class TaxLawSystem implements TaxLawInterface {


    @Override
    public double getTaxRate(double revenueAmount) {
        return 0.17*revenueAmount;
    }
}
