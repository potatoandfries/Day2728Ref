package vttpday28workshop.vttpday28workshop.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import vttpday28workshop.vttpday28workshop.models.Game;
import vttpday28workshop.vttpday28workshop.models.GameWithRating;
import vttpday28workshop.vttpday28workshop.repositories.BGGRepo;

@Service
public class BGGService {
    @Autowired
    private BGGRepo bggRepo;

    public Optional<JsonObject> getGameWithReviews(Integer id){
        Optional<Game> opt = bggRepo.getGameWithReviews(id);
        if (opt.isEmpty()){
            return Optional.empty();
        }
        return Optional.of(opt.get().toJson());
    }

    public JsonObject getGameWithRating(String ratingType){
        List<GameWithRating> games = bggRepo.getGamesWithRating(ratingType);
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("rating", games.get(0).getRating());
        games.forEach(game -> arrayBuilder.add(game.toJson()));
        objectBuilder.add("games", arrayBuilder.build());
        objectBuilder.add("timestamp", LocalDateTime.now().toString());
        return objectBuilder.build();
    }
}
