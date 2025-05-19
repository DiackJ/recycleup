package com.hyperskill.customerFeedback;

import com.hyperskill.customerFeedback.DTO.EntityDTO;
import com.hyperskill.customerFeedback.DTO.PageDTO;
import com.hyperskill.customerFeedback.Documents.ReviewEntity;
import com.hyperskill.customerFeedback.Service.FeedbackService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

//    @Autowired
//    public FeedbackController(FeedbackService feedbackService){
//        this.feedbackService = feedbackService;
//    }

    @PostMapping("/feedback")
    public ResponseEntity<?> feedback(@RequestBody EntityDTO entityDTO){
        ReviewEntity entity = feedbackService.entityFeedback(entityDTO);
        //setting 201 created response with location header /feedback/[generatedId]
        String locationUri = "/feedback/" + entity.getId();
        HttpHeaders header = new HttpHeaders();
        header.set("Location", locationUri);
        return new ResponseEntity<>(entity, header, HttpStatus.CREATED);
    }

    @GetMapping("/feedback/{id}")
    public ResponseEntity<ReviewEntity> retrieveFeedback(@PathVariable ObjectId id){
        //System.out.println("true");
        return feedbackService.getFeedback(id);

    }


    @GetMapping("/feedback")
    public PageDTO<ReviewEntity> retrievePagedDocs(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int perPage, @RequestParam(required = false) Double rating, @RequestParam(required = false) String customer, @RequestParam(required = false) String product, @RequestParam(required = false) String vendor){
        return feedbackService.pagingFeedback(page, perPage, rating, customer, product, vendor);
    }
}
