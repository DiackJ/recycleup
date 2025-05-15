package com.springboot.RecycleUp.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private long id;

    private String name;
    private int goal;
    private int itemsRecycled;
    private double progress;
    private int points;
    @JsonIgnore
    private boolean isPrimary;

    private boolean bronzeReward;
    private boolean silverReward;
    private boolean goldReward;
    private boolean diamondReward;

    @ManyToOne
    @JoinColumn(name="account_id", nullable = false)
    @JsonBackReference
    private Account account;

    @Transient
    @JsonProperty
    @OneToMany(mappedBy = "profile", orphanRemoval = false)
    private Map<String, Integer> topItems = new HashMap<>();

    @Transient
    @JsonProperty
    private Map<String, Integer> leaderboard = new HashMap<>();

    public Profile(){}
    public Profile(String name, int goal){this.name = name; this.goal = goal; }

    public String getName(){return this.name;}
    public void setGoal(int goal){this.goal = goal;}
    public int getGoal(){return this.goal;}
    public void setItemsRecycled(int items){this.itemsRecycled = items;}
    public int getItemsRecycled(){return this.itemsRecycled;}
    public void setProgress(double progress){this.progress = progress;}
    public double getProgress(){return this.progress;}
    public void setPoints(int points){this.points = points;}
    public int getPoints(){return this.points;}
    public long getProfileId(){return this.id;}
//    public void setPrimary(boolean isPrimary){this.isPrimary = isPrimary;}

    public void setAccount(Account account){this.account = account;}
    public Account getAccount(){return this.account;}

    public void setTopItems(Map<String, Integer> topItems){this.topItems = topItems;}
    public Map<String, Integer> getTopItems(){return this.topItems;}

    public void setLeaderboardList(Map<String, Integer> leaderboard){this.leaderboard = leaderboard;}
    public Map<String, Integer> getLeaderboard(){return this.leaderboard;}

    public void setBronzeReward(boolean bronzeReward){this.bronzeReward = bronzeReward;}
    public boolean getBronzeReward(){return this.bronzeReward;}
    public void setSilverReward(boolean silverReward){this.silverReward = silverReward;}
    public boolean getSilverReward(){return this.silverReward;}
    public void setGoldReward(boolean goldReward){this.goldReward = goldReward;}
    public boolean getGoldReward(){return this.goldReward;}
    public void setDiamondReward(boolean diamondReward){this.diamondReward = diamondReward;}
    public boolean getDiamondReward(){return this.diamondReward;}

    public String toString(){
        return name + " " + points;
    }

}
