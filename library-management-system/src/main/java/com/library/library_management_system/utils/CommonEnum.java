package com.library.library_management_system.utils;


public class CommonEnum {
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

    public enum Category {
        FICTION,
        NON_FICTION,
        SCIENCE,
        HISTORY
    }

    public enum Status {
        SUCCESS,
        FAILED
    }

    public enum TransactionStatus {
        BORROWED,
        RETURNED,
        OVERDUE
    }

    public enum TransactionType {
        DEBIT,
        CREDIT
    }
}