package TerminalPortManagementSystem.User;

public interface User {
    String getUsername();
    String getPassword();
    boolean isManager();
    boolean isAdmin();
}
