package vttpday28workshop.vttpday28workshop.models;

import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Game {
    private Integer gameId;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer usersRated;
    private String url;
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
    public Integer getGameId() {
        return gameId;
    }
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getRanking() {
        return ranking;
    }
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    public Integer getUsersRated() {
        return usersRated;
    }
    public void setUsersRated(Integer usersRated) {
        this.usersRated = usersRated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

    public static Game toGame(Document doc){
        Game game = new Game();
        game.setGameId(doc.getInteger("gid"));
        game.setName(doc.getString("name"));
        game.setYear(doc.getInteger("year"));
        game.setRanking(doc.getInteger("ranking"));
        game.setUsersRated(doc.getInteger("users_rated"));
        game.setUrl(doc.getString("url"));
        if (doc.getList("reviews", Document.class) != null){
            List<Document> reviewDocs = doc.getList("reviews", Document.class);
            List<Review> reviewList = reviewDocs.stream().map(document -> Review.toReview(document)).toList();
            game.setReviews(reviewList);
        }
        return game;
    }
    
    public JsonObject toSimpleJson(){
        JsonObject obj = Json.createObjectBuilder()
                            .add("game_id", gameId)
                            .add("name", name)
                            .build();
        return obj;
    }

    public JsonObject toJson(){
        JsonObjectBuilder objBuilder = Json.createObjectBuilder()
                            .add("game_id", gameId)
                            .add("name", name)
                            .add("year", year)
                            .add("ranking", ranking)
                            .add("users_rated", usersRated)
                            .add("url", url);
        if (reviews != null){
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            reviews.forEach(review -> arrayBuilder.add(review.toLatestJson()));
            objBuilder.add("reviews", arrayBuilder.build());
        }
        objBuilder.add("timestamp", LocalDateTime.now().toString());
        return objBuilder.build();
    }
}