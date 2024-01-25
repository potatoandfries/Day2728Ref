package vttpday27workshop.vttpday27workshop.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonValue;

public class Review {
    private String user;
    private Float rating;
    private String comment;
    private Integer gameId;
    private String posted = LocalDate.now().toString();
    private List<Edit> editted;

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public Float getRating() {
        return rating;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Integer getGameId() {
        return gameId;
    }
    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }
    public String getPosted() {
        return posted;
    }
    public void setPosted(String posted) {
        this.posted = posted;
    }
    public List<Edit> getEditted() {
        return editted;
    }
    public void setEditted(List<Edit> editted) {
        this.editted = editted;
    }

    public JsonObject toJson(){
        JsonObjectBuilder builder = Json.createObjectBuilder()
                            .add("user", user)
                            .add("rating", rating)
                            .add("comment", comment)
                            .add("gid", gameId)
                            .add("posted", posted);
        if (editted != null){
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            editted.forEach(edit -> arrBuilder.add(edit.toJson()));
            builder.add("editted", arrBuilder.build());
        }
        builder.add("timestamp", LocalDateTime.now().toString());
        return builder.build();
    }

    public static Review toReview(Document doc){
        Review review = new Review();
        List<Document> edits = doc.getList("editted", Document.class);
        if (edits != null){
            List<Edit> editsList = edits.stream().map(document -> Edit.toEdit(document)).toList();
            review.setEditted(editsList);
            review.setComment(editsList.get(editsList.size() - 1).getComment());
            review.setRating(editsList.get(editsList.size() - 1).getRating());
        }
        else {
            review.setComment(doc.getString("comment"));
            review.setRating(Float.parseFloat(doc.get("rating").toString()));
        }
        review.setGameId(doc.getInteger("gid"));
        review.setUser(doc.getString("user"));
        review.setPosted(doc.getString("posted"));
        return review;
    }

    public JsonObject toLatestJson(){
        JsonObject obj = Json.createObjectBuilder()
                            .add("user", user)
                            .add("rating", rating)
                            .add("comment", comment)
                            .add("gid", gameId)
                            .add("posted", posted)
                            .add("editted", editted == null ? JsonValue.FALSE : JsonValue.TRUE)
                            .add("timestamp", LocalDateTime.now().toString())
                            .build();
        return obj;
    }
}
