package Stubs;

import Domain.MainSystem;
import Domain.Users.SystemManager;

import java.util.Date;

public class SystemManagerStub_A extends SystemManager {

    public SystemManagerStub_A(MainSystem ms, String name, String phoneNumber, String email, String userName, String password, Date date) {
        super(ms, name, phoneNumber, email, userName, password, date);
    }
}
