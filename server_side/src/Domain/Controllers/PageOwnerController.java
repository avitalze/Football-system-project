package Domain.Controllers;

import Domain.PageOwner;
import Domain.PrivatePage;

public class PageOwnerController {
    /**
     * add record to page
     * @param pO
     * @param record
     * @throws Exception when page not exist or record not exist
     * @codeBy Eden
     */
    public void addRecordToPage(PageOwner pO,String record) throws Exception{
        if(pO.getPage()!=null){
            pO.addRecordToPage(record);
        }
        else{
            throw new Exception("first open page");
        }
    }

    /**
     * remove record from page, if page not exist or record not exist throws exception ,
     * @param pO
     * @param record
     * @throws Exception when page not exist or record not exist
     * @codeBy Eden
     */
    public void removeRecordFromPage(PageOwner pO,String record) throws Exception{
        if(pO.getPage()!=null){
            pO.removeRecordFromPage(record);
        }
        else{
            throw new Exception("first open page");
        }
    }

    /**
     * create private page (can be only 1)
     * @param pO
     * @throws Exception if page already exist
     * @codeBy Eden
     */
    public void createPrivatePage(PageOwner pO) throws Exception {
        if(!pO.createPrivatePage()){
            throw new Exception("you already have page");
        }

    }

    /**
     * delete private page (can be only 1)
     * @param pO
     * @throws Exception if page already exist
     * @codeBy Eden
     */
    public void deletePrivatePage(PageOwner pO) throws Exception {
        if(!pO.deletePrivatePage()){
            throw new Exception("you don't have page to delete");
        }
    }

    public PrivatePage getPage(PageOwner pO){
        return pO.getPage();
    }
}

