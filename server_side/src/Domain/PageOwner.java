package Domain;

public interface PageOwner {

    default void addRecordToPage(String record) throws Exception{
        if(record==null || record.length()==0){
            throw new Exception("record not valid");
        }
        if(this.getPage()!=null) {
            this.getPage().addRecords(record);
        }
        else{
            throw new Exception("The team hasn't private page");
        }

    }

    default void removeRecordFromPage(String record) throws Exception{
        if(record==null || record.length()==0){
            throw new Exception("record not valid");
        }
        if(this.getPage()!=null) {
            this.getPage().removeRecord(record);
        }
        else{
            throw new Exception("The team hasn't private page");
        }
    }

    default boolean createPrivatePage(){
        PrivatePage p = new PrivatePage();
        if(this.getPage()==null){// you can have only one page
            this.setPage(p);
            p.setPageOwner(this);
            return true;
        }
        return false;
    }

    default boolean deletePrivatePage(){
        if(getPage() == null){
            return false;
        }
        this.getPage().setPageOwner(null);
        this.setPage(null);
        return true;
    }


    public PrivatePage getPage();

    public void setPage(PrivatePage p);

    public String getOwnerName();
}

