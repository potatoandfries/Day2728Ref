package vttpday27workshop.vttpday27workshop.repos;

import java.util.Optional;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import vttpday27workshop.vttpday27workshop.models.Edit;
import vttpday27workshop.vttpday27workshop.models.Review;

@Repository
public class BGGRepo {

    @Autowired
    private MongoTemplate template;

    public boolean gameExists(Integer gid){
        Criteria criteria = Criteria.where("gid").is(gid);
        Query query = Query.query(criteria);
        Document doc = template.findOne(query, Document.class, "game");
        return doc == null ? false : true;
    }

    public String insertReview(Review review){
        Document reviewDoc = Document.parse(review.toJson().toString());
        Document doc = template.insert(reviewDoc, "reviews");
        return doc.getObjectId("_id").toHexString();
    }
    
    public boolean updateReview(String id, Edit edit){
        ObjectId objId = new ObjectId(id);
        Criteria criteria = Criteria.where("_id").is(objId);
        Query query = Query.query(criteria);
        Document doc = Document.parse(edit.toJson().toString());
        Update updateOps = new Update().set("rating", edit.getRating()).set("comment", edit.getComment()).push("editted", doc);
        UpdateResult result = template.updateMulti(query, updateOps, Document.class, "reviews");
        return result.getModifiedCount() == 1;
    }

    public Optional<Review> getReviewById(String id){
        ObjectId objId = new ObjectId(id);
        Document doc = template.findById(objId, Document.class, "reviews");
        if (doc == null){
            return Optional.empty();
        }
        return Optional.of(Review.toReview(doc));
    }
}
