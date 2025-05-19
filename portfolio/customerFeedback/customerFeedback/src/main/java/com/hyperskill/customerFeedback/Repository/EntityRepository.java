package com.hyperskill.customerFeedback.Repository;

import com.hyperskill.customerFeedback.Documents.ReviewEntity;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends MongoRepository<ReviewEntity, ObjectId> {
}
