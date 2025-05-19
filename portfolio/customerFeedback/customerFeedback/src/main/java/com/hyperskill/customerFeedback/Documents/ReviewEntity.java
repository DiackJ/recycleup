package com.hyperskill.customerFeedback.Documents;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class ReviewEntity {
    @Id
    private ObjectId id;
    private String customer;
    private String vendor;
    private String product;
    private double rating;
    private String feedback;

    public ReviewEntity(){}

    public ObjectId getId(){
        return this.id;
    }
    public void setId(ObjectId id){
        this.id = id;
    }

    public Double getRating(){
        return this.rating;
    }
    public void setRating(Double rating){
        this.rating=rating;
    }

    public String getFeedback(){
        return this.feedback;
    }
    public void setFeedback(String feedback){
        this.feedback = feedback;
    }

    public String getCustomer(){
        return this.customer;
    }
    public void setCustomer(String customer){
        this.customer=customer;
    }

    public String getProduct(){
        return this.product;
    }
    public void setProduct(String product){
        this.product=product;
    }

    public String getVendor(){
        return this.vendor;
    }
    public void setVendor(String vendor){
        this.vendor = vendor;
    }

    @Override
    public String toString(){
        return this.vendor;
    }
}
