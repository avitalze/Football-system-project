package Domain.ExternalSystems;

public class TaxSystemProxy implements TaxLawInterface {


    private TaxLawInterface taxLawSystem;

    public TaxSystemProxy(TaxLawInterface taxLawInterface) {
        taxLawSystem=taxLawInterface;

    }

    @Override
    public double getTaxRate(double revenueAmount) {
        if(taxLawSystem == null || taxLawSystem== this){
            return -1;
        }else{
            return taxLawSystem.getTaxRate(revenueAmount);
        }
    }

    public boolean switchTaxSystems(TaxLawInterface taxLawInterface){
        if(taxLawInterface== this){
            return false;
        }
        else{
            taxLawSystem=taxLawInterface;
            return true;
        }
    }
}
