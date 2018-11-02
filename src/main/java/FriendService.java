package main.java;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

public class FriendService {
    private static MongoCollection<Document> friendColection;
    public FriendService() {
        // open connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        // get ref to database
        MongoDatabase db = mongoClient.getDatabase("REST2");
        // get ref to collection
        friendColection = db.getCollection("friends");
        friendColection.drop();
        friendColection = db.getCollection("friends");
    }

    public String addFriend(String userID, String friendID) {
        Document newFriendship = new Document().append("userID", userID).append("friendsID", friendID);
        friendColection.insertOne(newFriendship);
        return "okay";
    }

    public Set<String> getAllFriend(String userID) {
        FindIterable<Document> allFriends = friendColection.find(eq("userID", userID));
        Set<String> allFriendsID = new HashSet<>();
        for (Document friend : allFriends) {
            allFriendsID.add(friend.get("friendsID").toString());
        }
        return allFriendsID;
    }
}
