package com.springboot.RecycleUp.DTO;

public class AccountProfileDTO {
    private String email;
    private String password;

    private String name;
    private int goal;

    public AccountProfileDTO(String email, String password, String name, int goal){
        this.email =email;
        this.password = password;
        this.name = name;
        this.goal = goal;
    }
    public AccountProfileDTO(){}
    public AccountProfileDTO(String name, int goal){this.name = name; this.goal = goal;}

    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}
    public String getName(){return this.name;}
    public int getGoal(){return this.goal;}

}
