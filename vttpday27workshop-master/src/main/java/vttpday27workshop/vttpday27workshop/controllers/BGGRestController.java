package vttpday27workshop.vttpday27workshop.controllers;

import java.io.StringReader;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttpday27workshop.vttpday27workshop.models.Edit;
import vttpday27workshop.vttpday27workshop.models.Review;
import vttpday27workshop.vttpday27workshop.services.BGGService;

@RestController
@RequestMapping
public class BGGRestController {
    
    @Autowired
    private BGGService bggSvc;

    @PostMapping(path = "/review", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createReview(@RequestBody MultiValueMap<String, String> form){
        Review review = new Review();
        review.setUser(form.getFirst("user"));
        review.setRating(Float.parseFloat(form.getFirst("rating")));
        review.setComment(form.getFirst("comment"));
        review.setGameId(Integer.parseInt(form.getFirst("gid")));
        Optional<String> opt = bggSvc.createReview(review);
        if (opt.isEmpty()){
            JsonObject resp = Json.createObjectBuilder()
                                    .add("error", "Game not found")
                                    .build();
            return ResponseEntity.status(404).body(resp.toString());
        }
        JsonObject resp = Json.createObjectBuilder()
                                    .add("review_id", opt.get())
                                    .build();
        return ResponseEntity.status(201).body(resp.toString());
    }

    @PutMapping(path = "/review/{reviewId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateReview(@PathVariable String reviewId, @RequestBody String payload){
        JsonObject data = Json.createReader(new StringReader(payload)).readObject();
        Edit edit = new Edit();
        edit.setComment(data.getString("comment", ""));
        edit.setRating(Float.parseFloat(data.get("rating").toString()));
        if (!bggSvc.updateReview(reviewId, edit)){
            JsonObject resp = Json.createObjectBuilder()
                                .add("error", "Review not found.")
                                .build();
            return ResponseEntity.status(404).body(resp.toString());
        }
        return ResponseEntity.status(200).body(null);
    }
    
    @GetMapping(path = "/review/{reviewId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getLatestReview(@PathVariable String reviewId){
        Optional<JsonObject> opt = bggSvc.getLatestReviewById(reviewId);
        if (opt.isEmpty()){
            JsonObject resp = Json.createObjectBuilder()
                                .add("error", "Review not found.")
                                .build();
            return ResponseEntity.status(404).body(resp.toString());
        }
        return ResponseEntity.status(200).body(opt.get().toString());
    }

    @GetMapping(path = "/review/{reviewId}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getFullReview(@PathVariable String reviewId){
        Optional<JsonObject> opt = bggSvc.getFullReviewById(reviewId);
        if (opt.isEmpty()){
            JsonObject resp = Json.createObjectBuilder()
                                .add("error", "Review not found.")
                                .build();
            return ResponseEntity.status(404).body(resp.toString());
        }
        return ResponseEntity.status(200).body(opt.get().toString());
    }
}
