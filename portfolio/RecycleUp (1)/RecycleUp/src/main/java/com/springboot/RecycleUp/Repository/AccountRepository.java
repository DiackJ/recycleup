package com.springboot.RecycleUp.Repository;

import com.springboot.RecycleUp.Model.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.id= :accId")
    void deleteAccountById(@Param("accId") long accId);
}
