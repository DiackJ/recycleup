package com.hyperskill.customerFeedback.Service;

import com.hyperskill.customerFeedback.DTO.PageDTO;
import com.hyperskill.customerFeedback.Documents.ReviewEntity;
import com.hyperskill.customerFeedback.DTO.EntityDTO;
import com.hyperskill.customerFeedback.Repository.EntityRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackService {
    @Autowired
    private final EntityRepository entityRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public FeedbackService(EntityRepository entityRepository, MongoTemplate mongoTemplate){
        this.entityRepository = entityRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public ReviewEntity entityFeedback(EntityDTO entityDTO){
        ReviewEntity entity = new ReviewEntity();

        entity.setId(new ObjectId());
        entity.setRating(entityDTO.getRating());
        entity.setCustomer(entityDTO.getCustomer());
        entity.setFeedback(entityDTO.getFeedback());
        entity.setProduct(entityDTO.getProduct());
        entity.setVendor(entityDTO.getVendor());

        return entityRepository.save(entity);
    }

    //method that returns an entity object by id, if no id is found then return a 404 not found http response with a null body
    public ResponseEntity<ReviewEntity> getFeedback(ObjectId id){
        return entityRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    public PageDTO<ReviewEntity> pagingFeedback(int page, int perPage, Double rating, String customer, String product, String vendor){
        PageDTO<ReviewEntity> pageDTO = new PageDTO<>();

        List<ReviewEntity> entity = entityRepository.findAll();

        //validating that page number is >= 1 and that items per page are minimum 5 and maximum 20 else use default values
        if(page <= 0){
            page = 1;
        }

        if(perPage < 5 || perPage > 20){
            perPage = 10;
        }

      //create a page object of entities that are (possibly) filtered and paged
       Page<ReviewEntity> entityPage = filteredResults(rating, customer, product, vendor, getPageable(page -1, perPage));

     //set the vales on the page based on above filtering and pagination
       pageDTO.setPages(entityPage);

       System.out.println(entityPage.getContent());


        //set the current page
        pageDTO.setCurrentPage(page);
        //set the total number of pages by getting the size of the page object
        pageDTO.setTotalPages(entityPage.getSize());

        //debug statement to see the current page vs total number of pages
        System.out.println(pageDTO.getCurrentPage() + " -- " + pageDTO.getTotalPages());

        //get the total number of documents per current page
        pageDTO.setTotalDocuments(entityPage.getNumberOfElements());

        //setting the number of documents equal to all the documents in the repository unless the page number
        //is greater than the total number of documents in the repository, in which case return an empty array
        if(page <= pageDTO.getTotalDocuments()){
            pageDTO.setDocuments(entity);
        }else if(page > pageDTO.getTotalDocuments()){
            pageDTO.setDocuments(new ArrayList<>());
        }

        //if the current page and last page match and are both 0 (first page) then isFirstPage and isLastPage get marked as true since there is only 1 page
        //else, if the current page only is 0, then only isFirstPage gets marked as true because that means it's the first page
        //else, if the last page is equal to the total number of pages then only isLastPage gets marked as true because that means it's the last page
        //otherwise if it is not the first or last page, then both get marked as false as it's some page in between
        if(pageDTO.getCurrentPage() == pageDTO.getTotalPages() && pageDTO.getTotalPages() == 0){
            pageDTO.setFirstPage(true);
            pageDTO.setLastPage(true);
        }else if(pageDTO.getCurrentPage() == 0){
            pageDTO.setFirstPage(true);
        }else if(pageDTO.getCurrentPage() == pageDTO.getTotalPages()){
            pageDTO.setLastPage(true);
        }else{
            pageDTO.setFirstPage(false);
            pageDTO.setLastPage(false);
        }

        return pageDTO;
    }

    //method to return a pageable instance to be used in main pagingFeedback method to apply paging to entity results
    public Pageable getPageable(int page, int perPage){
        return PageRequest.of(page, perPage);
    }

    //method to return a page object of entities that may or may not be filtered depending on input provided by user to be used
    //in main pagingFeedback method to return content based on filters
    public Page<ReviewEntity> filteredResults(Double rating, String customer, String product, String vendor, Pageable page){
        Query query = new Query();

        // Add filters dynamically
        //filters will only be applied if they are not null, otherwise they remain empty and therefore ignored
        if (rating != null) {
            //use rang-based filtering where lowerBound represents the lowest possible value and upperBound represents the highest possible value to better filter number based values
            double lowerBound = rating;
            double upperBound = rating + 0.9;
            query.addCriteria(Criteria.where("rating").gte(lowerBound).lte(upperBound));
        }
        if (customer != null && !customer.isEmpty()) {
            //for example here: as long as the customer filter is not null or empty, a criteria will be added to the query telling it to look for
            //any documents where the customer field contains the inputted filter and the i makes it case-insensitive so Karen, KAREN, karen, and KaReN all work the same
            query.addCriteria(Criteria.where("customer").regex(customer, "i")); 
        }
        if (product != null && !product.isEmpty()) {
            query.addCriteria(Criteria.where("product").regex(product, "i"));
        }
        if (vendor != null && !vendor.isEmpty()) {
            query.addCriteria(Criteria.where("vendor").regex(vendor, "i"));
        }
        //else, if all filter values are null or empty, then the query is empty and gets ignored returning all documents by default according to paging

        // Add pagination so documents still comply with paging
        query.with(page);

        //gets the results of the filtering
        List<ReviewEntity> results = mongoTemplate.find(query, ReviewEntity.class);
        //calculates the count of documents to apply pagination
        long total = mongoTemplate.count(query.skip(-1).limit(-1), ReviewEntity.class);

        return new PageImpl<>(results, page, total);
    }

    /* note on using pageable instead of dynamically setting skip and limit variables
    *
       pageable takes the provided page number and calculates the offset to manually skip objects that have already appeared on previous pages
       as well as taking the provided object limit to specify how many objects appear on each page
       implemented manually may look something like this:
                int skip = (currentPage -1) * perPage -- we minus one from the current page so the offset starts at 0 because 1 - 1 = 0 and 0 * 5 (for example) is 0
                int limit = currentPage * perPage -- here we don't need to minus one because this is to determine to what index it should stop
                and then this can be implemented using .skip() and .limit() provided by mongoDB repository
        pageable does all of this behind the scenes
    *
    */

}
