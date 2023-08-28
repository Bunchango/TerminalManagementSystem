package TerminalPortManagementSystem.User;

import java.io.Serializable;

public class Admin implements Serializable, User {
    private final String username = "admin";
    private final String password = "admin12345";
    public final UserType userType = UserType.Admin;
    private static final Admin instance = new Admin();

    public static Admin getInstance() {
        return instance;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public UserType getUserType() {
        return userType;
    }
}
