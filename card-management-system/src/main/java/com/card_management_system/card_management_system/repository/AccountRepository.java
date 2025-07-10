package com.card_management_system.card_management_system.repository;

import com.card_management_system.card_management_system.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}
