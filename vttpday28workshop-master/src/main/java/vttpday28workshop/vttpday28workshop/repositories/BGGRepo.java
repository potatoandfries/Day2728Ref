package vttpday28workshop.vttpday28workshop.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationPipeline;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Field;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import vttpday28workshop.vttpday28workshop.models.Game;
import vttpday28workshop.vttpday28workshop.models.GameWithRating;

@Repository
public class BGGRepo {
    @Autowired
    private MongoTemplate template;

    public Optional<Game> getGameWithReviews(Integer id){
        MatchOperation matchOps = Aggregation.match(Criteria.where("gid").is(id));
        LookupOperation lookupOps = Aggregation.lookup("reviews", "gid", "gid", "reviews");
        Aggregation pipeline = Aggregation.newAggregation(matchOps, lookupOps);
        AggregationResults<Document> results = template.aggregate(pipeline, "game", Document.class);
        List<Document> resultList = results.getMappedResults();
        if (resultList.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(Game.toGame(resultList.get(0)));
    }

    public List<GameWithRating> getGamesWithRating(String ratingType){
        ProjectionOperation lookupProjOps = Aggregation.project("_id", "user", "rating", "comment");
        SortOperation lookupSortOps;
        SortOperation sortOps;
        if (ratingType.equals("highest")){
            lookupSortOps = Aggregation.sort(Sort.by(Direction.DESC, "rating"));
            sortOps = Aggregation.sort(Sort.by(Direction.DESC, "rating"));
        }
        else {
            lookupSortOps = Aggregation.sort(Sort.by(Direction.ASC, "rating"));
            sortOps = Aggregation.sort(Sort.by(Direction.ASC, "rating"));
        }
        
        LimitOperation limitOps = Aggregation.limit(1);
        AggregationPipeline p = new AggregationPipeline().add(lookupSortOps).add(limitOps).add(lookupProjOps);
        Field gid = Fields.field("gid");
        Field output = Fields.field("review");
        LookupOperation lookUpOps = new LookupOperation("reviews", gid, gid, null, p, output);
        UnwindOperation unwindOps = Aggregation.unwind("review");
        ProjectionOperation projOps = Aggregation.project("name").and("gid").as("_id")
                                                .and("$review.rating").as("rating")
                                                .and("$review.user").as("user")
                                                .and("$review.comment").as("comment")
                                                .and("$review._id").as("review_id");
        
        Aggregation pipeline = Aggregation.newAggregation(lookUpOps, unwindOps, projOps, sortOps);
        List<Document> results = template.aggregate(pipeline, "game", Document.class).getMappedResults();
        return results.stream().map(doc -> GameWithRating.toGameWithRating(doc)).toList();
    }
}