package vttpday28workshop.vttpday28workshop.models;

import java.time.LocalDateTime;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class Edit {
    private String comment;
    private Float rating;
    private String posted = LocalDateTime.now().toString();

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public Float getRating() {
        return rating;
    }
    public void setRating(Float rating) {
        this.rating = rating;
    }
    public String getPosted() {
        return posted;
    }
    public void setPosted(String posted) {
        this.posted = posted;
    }
    
    public JsonObject toJson(){
        JsonObject obj = Json.createObjectBuilder()
                            .add("comment", comment)
                            .add("rating", rating)
                            .add("posted", posted)
                            .build();
        return obj;
    }

    public static Edit toEdit(Document doc){
        Edit edit = new Edit();
        edit.setComment(doc.getString("comment"));
        edit.setPosted(doc.getString("posted"));
        edit.setRating(Float.parseFloat(doc.get("rating").toString()));

        return edit;
    }
}
