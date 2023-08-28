package TerminalPortManagementSystem.User;

public enum UserType {
    Admin {
        @Override
        public boolean isAdmin() {
            return true;
        }

        @Override
        public boolean isManager() {
            return false;
        }
    },
    Manager {
        @Override
        public boolean isAdmin() {
            return false;
        }

        @Override
        public boolean isManager() {
            return true;
        }
    };

    public abstract boolean isAdmin();
    public abstract boolean isManager();
}
