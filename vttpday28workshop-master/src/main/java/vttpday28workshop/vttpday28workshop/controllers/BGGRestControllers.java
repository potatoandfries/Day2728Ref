package vttpday28workshop.vttpday28workshop.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttpday28workshop.vttpday28workshop.services.BGGService;

@RestController
@RequestMapping
public class BGGRestControllers {
    @Autowired
    private BGGService bggSvc;

    @GetMapping(path = "/game/{gameId}/reviews", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameWithReviews(@PathVariable Integer gameId){
        Optional<JsonObject> opt = bggSvc.getGameWithReviews(gameId);
        if (opt.isEmpty()){
            JsonObject resp = Json.createObjectBuilder()
                                .add("error", "Game not found.")
                                .build();
            return ResponseEntity.status(404).body(resp.toString());
        }
        return ResponseEntity.status(200).body(opt.get().toString());
    }

    @GetMapping(path = "/games/highest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameWithHighestRating(){
        JsonObject resp = bggSvc.getGameWithRating("highest");
        return ResponseEntity.status(200).body(resp.toString());
    }

    @GetMapping(path = "/games/lowest", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameWithLowestRating(){
        JsonObject resp = bggSvc.getGameWithRating("lowest");
        return ResponseEntity.status(200).body(resp.toString());
    }
}
