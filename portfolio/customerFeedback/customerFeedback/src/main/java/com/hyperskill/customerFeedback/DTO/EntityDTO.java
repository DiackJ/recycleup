package com.hyperskill.customerFeedback.DTO;


public class EntityDTO {
    private String customer;
    private String vendor;
    private String product;
    private double rating;
    private String feedback;

    public EntityDTO(){}

    public double getRating(){
        return this.rating;
    }

    public String getFeedback(){
        return this.feedback;
    }

    public String getCustomer(){
        return this.customer;
    }

    public String getProduct(){
        return this.product;
    }

    public String getVendor(){
        return this.vendor;
    }

}
