package com.springboot.RecycleUp.DTO;

public class ItemDTO {
    private String material;
    private int amount;

    public ItemDTO(){}

    public ItemDTO(String material, int amount){
        this.material = material;
        this.amount = amount;
    }

    public String getMaterial(){return this.material;}
    public int getAmount(){return this.amount;}

}
