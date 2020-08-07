package Stubs;

import Domain.MainSystem;
import Domain.Users.Fan;
import Domain.Users.TeamRole;

import java.util.Date;

public class TeamRoleStub_A extends TeamRole {

    public TeamRoleStub_A(Fan fan) {
        super(fan);
    }

    public TeamRoleStub_A(MainSystem ms, String name, String phoneNumber, String email, String userName, String password, Date date) {
        super(ms, name, phoneNumber, email, userName, password, date);
    }
}
