package TerminalPortManagementSystem.User;

public interface User {
    String getUsername();
    String getPassword();
    // Determine the type of user
    boolean isManager();
    boolean isAdmin();
}
