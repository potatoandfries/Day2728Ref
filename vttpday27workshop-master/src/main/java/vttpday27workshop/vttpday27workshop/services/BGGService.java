package vttpday27workshop.vttpday27workshop.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.JsonObject;
import vttpday27workshop.vttpday27workshop.models.Edit;
import vttpday27workshop.vttpday27workshop.models.Review;
import vttpday27workshop.vttpday27workshop.repos.BGGRepo;

@Service
public class BGGService {
    
    @Autowired
    private BGGRepo bggRepo;

    public Optional<String> createReview(Review review){
        if (!bggRepo.gameExists(review.getGameId())){
            return Optional.empty();
        }
        String objId = bggRepo.insertReview(review);
        return Optional.of(objId);
    }

    public boolean updateReview(String id, Edit edit){
        return bggRepo.updateReview(id, edit);
    }

    public Optional<JsonObject> getLatestReviewById(String id){
        Optional<Review> opt = bggRepo.getReviewById(id);
        if (opt.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(opt.get().toLatestJson());
    }

    public Optional<JsonObject> getFullReviewById(String id){
        Optional<Review> opt = bggRepo.getReviewById(id);
        if (opt.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(opt.get().toJson());
    }
}
