package com.springboot.RecycleUp.Util;

import com.springboot.RecycleUp.Model.Account;
import com.springboot.RecycleUp.Repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Account user = accountRepository.findByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("Account not found");
        }
        return new CustomUserDetails(user);
    }

}
