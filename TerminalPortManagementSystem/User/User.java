package TerminalPortManagementSystem.User;

public interface User {
    public abstract String getUsername();
    public abstract String getPassword();
    public abstract UserType getUserType();
}
