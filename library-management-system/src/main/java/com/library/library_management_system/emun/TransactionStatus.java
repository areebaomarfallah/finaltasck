// In BMS (Book Management System)
package com.library.library_management_system.emun;

public enum TransactionStatus {
    BORROWED,
    RETURNED,
    OVERDUE;

    public enum TransactionType {
        DEBIT,
        CREDIT
    }
}