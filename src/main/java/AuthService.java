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


    public String creatSession(String userID){
        Date timeStamp = new Date();
        System.out.println(timeStamp);
        String token = generateHash(timeStamp.toString());
        System.out.println(token);
        Document newSession = new Document("_id",userID).append("token",token).append("timestamp",timeStamp);
        return token;
    }

    private String generateHash(String time){
        try{
            MessageDigest mDigest = MessageDigest.getInstance("SHA-1");
            byte[] result = mDigest.digest(time.getBytes());
            return Base64.getEncoder().encodeToString(result);
        }catch(Exception e){
            return "Fail on generating Hash";
        }
    }

    public Document findOwner(String token) {
        Document tokenOwner = authCollection.find(eq("token", token)).first();
        return tokenOwner;
    }

}