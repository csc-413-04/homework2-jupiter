package main.java;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.security.MessageDigest;
import java.util.*;
import java.lang.String;

import static com.mongodb.client.model.Filters.eq;

public class AuthService {
    private final MongoDatabase db;
    private final MongoCollection<Document> authCollection;

    public AuthService(){
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDatabase("REST2");

        authCollection = db.getCollection("auth");
    }


 public String creatSession(String userID) {
        Date timeStamp = new Date();
        Document existingSession = authCollection.find(eq("_id", userID)).first();
        if (existingSession == null) {
            Document newSession = new Document("_id", userID).append("timestamp", timeStamp);
            authCollection.insertOne(newSession);
        } else {
            Document updateSession = new Document("$set", new Document("_id", userID).append("timestamp", timeStamp));
            authCollection.updateOne(existingSession, updateSession);
        }
        return userID;
    }

    public Document findOwner(String token) {
        Document tokenOwner = authCollection.find(eq("token", token)).first();
        return tokenOwner;
    }

}