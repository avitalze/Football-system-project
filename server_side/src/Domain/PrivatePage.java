package Domain;

import Domain.Users.Fan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PrivatePage {
    private List<String> records;
    private PageOwner pageOwner;
    private HashSet <Fan> fans;
    private static final Logger LOG = LogManager.getLogger("PrivatePage");

    public PrivatePage( PageOwner pageOwner) {
        this.pageOwner = pageOwner;
        this.fans=new HashSet<>();
        this.records= new ArrayList<>();
    }

    public PrivatePage() {
        this.pageOwner=null;
        this.fans=new HashSet<>();
        this.records= new ArrayList<>();
    }


    public List<String> getRecords() {
        return records;
    }

    public PageOwner getPageOwner() {
        return pageOwner;
    }

    public void setPageOwner(PageOwner pageOwner) {
        this.pageOwner = pageOwner;
    }

    public HashSet<Fan> getFans() {
        return fans;
    }


    /**Or**/
    //TODO test-V
    public String getRecordsAsString(){
        String ans="";
        for (String s:records) {
            ans=ans+ s +",";
        }
        return ans;
    }

    /**Or**/
    //TODO test-V
    public void addRecords(String record) throws Exception {
        if(record==null || record.length()==0){
            LOG.error("record not valid");
            throw new Exception("record not valid");
        }
        records.add(record);
        LOG.info(String.format("%s - %s", pageOwner.getOwnerName(), "added record to page"));
    }

    /**Or**/
    //TODO test-V
    public void removeRecord(String record) throws Exception {
        if(record==null){
            LOG.error("record in null");
            throw new Exception("record in null");
        }
        if(record.length()==0){
            LOG.error("record empty");
            throw new Exception("record empty");
        }
        if(!records.contains(record)){
            LOG.error("this page doesn't contain this record");
            throw new Exception("this page doesn't contain this record");
        }
        records.remove(record);
        LOG.info(String.format("%s - %s", pageOwner.getOwnerName(), "added record to page"));
    }

    /**Or**/
    //TODO test-V
    public void addFan(Fan fan) throws Exception {
        if(fan==null){
            LOG.error("fan null");
            throw new Exception("fan null");
        }
        if(fans.contains(fan)){
            LOG.error("fan is already in fan list");
            throw new Exception("this fan is already in fan list");
        }
        fans.add(fan);
        LOG.info(String.format("%s - %s", pageOwner.getOwnerName(), "add fan to page, fane username: "+fan.getUserName()));
    }



}
