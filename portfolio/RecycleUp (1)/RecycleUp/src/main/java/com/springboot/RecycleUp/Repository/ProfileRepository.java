package com.springboot.RecycleUp.Repository;

import com.springboot.RecycleUp.Model.Profile;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByName(String name);

    @Query("SELECT p FROM Profile p WHERE p.account.id = :accountId")
    List<Profile> profileList(@Param("accountId") Long accountId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Profile p WHERE p.name = :name AND p.account.id = :accId")
    void deleteByName(@Param("name") String pname, @Param("accId") long accountId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Profile p WHERE p.account.id= :accId")
    void deleteByAccount_Id(@Param("accId") long accId);
}
