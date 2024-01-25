package vttpday28workshop.vttpday28workshop.models;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class GameWithRating {
    private Integer gid;
    private String name;
    private Float rating;
    private String user;
    private String comment;
    private String review_id;

    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Float getRating() {
        return rating;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getReview_id() {
        return review_id;
    }
    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public static GameWithRating toGameWithRating(Document doc){
        GameWithRating game = new GameWithRating();
        game.setGid(doc.getInteger("_id"));
        game.setName(doc.getString("name"));
        game.setComment(doc.getString("comment"));
        game.setRating(Float.parseFloat(doc.get("rating").toString()));
        game.setReview_id(doc.getObjectId("review_id").toHexString());
        game.setUser(doc.getString("user"));
        return game;
    }

    public JsonObject toJson(){
        JsonObject obj = Json.createObjectBuilder()
                            .add("_id", gid)
                            .add("name", name)
                            .add("rating", rating)
                            .add("user", user)
                            .add("comment", comment)
                            .add("review_id", review_id)
                            .build();
        return obj;
    }
}
