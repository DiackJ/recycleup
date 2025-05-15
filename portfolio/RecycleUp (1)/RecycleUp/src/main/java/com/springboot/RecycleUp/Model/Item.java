package com.springboot.RecycleUp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String material;
    private int amount;

    @JsonIgnore
    private long accountId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="profile_id")
    private Profile profile;

    public Item(){}
    public Item(String material, int amount){
        this.material = material;
        this.amount = amount;
    }

    public String getMaterial(){return this.material;}
    public void setAmount(int amount){this.amount = amount;}
    public int getAmount(){return this.amount;}
    public void setAccountId(long accountId){this.accountId = accountId;}
    public void setProfileId(Profile profileId){this.profile = profileId;}

    public String toString(){
        return material;
    }

}
