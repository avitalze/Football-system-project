package DataAccess.DbAdapter;

import Domain.Main;
import Domain.MainSystem;
import Domain.Users.Fan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

public class FanAdapter implements DbObject<Fan> {
    public static final SimpleDateFormat birthDateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public Fan ToObj(List<String> fields) throws Exception {
        try {

            Fan f=new Fan(fields.get(1),fields.get(3),fields.get(4),fields.get(0),fields.get(2),birthDateFormat.parse(fields.get(5)));
            boolean isEmail=true;
            if(fields.get(5).equals(0)){
                isEmail=false;
            }
            f.setSendByEmail(isEmail);
            return f;

        }
        catch (ParseException e){
            throw new Exception ("date format is wrong ");
        }
    }

    @Override
    public List<String> ToDB(Fan obj) {
        LinkedList<String> res=new LinkedList<>();
        res.add(obj.getUserName());
        res.add(obj.getName());
        res.add(obj.getPassword());
        res.add(obj.getPhoneNumber());
        res.add(obj.getEmail());
        res.add(obj.getDateOfBirth().toString());
        return res;
    }
}
