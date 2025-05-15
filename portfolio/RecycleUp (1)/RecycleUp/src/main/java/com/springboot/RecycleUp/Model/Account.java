package com.springboot.RecycleUp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String email;
    @JsonIgnore
    private String password;

    @Transient
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Profile profiles;

    public Account(){}
    public Account(String email, String password){
        this.email = email;
        this.password = password;
    }

    //constructor to use in profile service test
    public Account(String email, String password, long id){
        this.email = email;
        this.password = password;
        this.id = id;
    }

    public long getAccountId(){return this.id;}
    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}

}
