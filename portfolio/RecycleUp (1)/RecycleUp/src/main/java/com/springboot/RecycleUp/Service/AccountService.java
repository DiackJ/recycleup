package com.springboot.RecycleUp.Service;

import com.springboot.RecycleUp.DTO.AccountProfileDTO;
import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Model.Profile;
import com.springboot.RecycleUp.Repository.AccountRepository;
import com.springboot.RecycleUp.Repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {
    @Autowired
    public final AccountRepository accountRepository;

    @Autowired
    public ProfileRepository profileRepository;

    @Autowired
    public final PasswordEncoder encoder;

    @Autowired
    public ProfileService profileService;

    public AccountService(AccountRepository accountRepository, PasswordEncoder encoder){
        this.accountRepository = accountRepository;
        this.encoder = encoder;
    }

    @Transactional
    public void createAccountAndProfile(AccountProfileDTO dto){
        Account account = new Account(dto.getEmail(), encoder.encode(dto.getPassword()));

        Profile profile = new Profile(dto.getName(), dto.getGoal());
        accountRepository.save(account);
        profile.setAccount(account);
        profileRepository.save(profile);
    }

    @Transactional
    public void deleteAccountAndAllProfiles(long accountId){
        profileRepository.deleteByAccount_Id(accountId); //first to prevent foreign key constraints
        accountRepository.deleteAccountById(accountId);
    }
}
