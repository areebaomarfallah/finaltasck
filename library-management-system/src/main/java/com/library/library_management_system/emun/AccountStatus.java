package com.library.library_management_system.emun;

public enum AccountStatus {
    ACTIVE {
        @Override
        public boolean canTransitionTo(AccountStatus newStatus) {
            return newStatus == SUSPENDED || newStatus == DEACTIVATED;
        }
    },
    SUSPENDED {
        @Override
        public boolean canTransitionTo(AccountStatus newStatus) {
            return newStatus == ACTIVE || newStatus == DEACTIVATED;
        }
    },
    DEACTIVATED {
        @Override
        public boolean canTransitionTo(AccountStatus newStatus) {
            return false; // Deactivated accounts cannot be reactivated
        }
    };

    public abstract boolean canTransitionTo(AccountStatus newStatus);
}