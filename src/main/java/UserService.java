

import static com.mongodb.client.model.Filters.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;


public class UserService {
    private static MongoCollection<Document> userColection;

    public UserService() {
        // open connection
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        // get ref to database
        MongoDatabase db = mongoClient.getDatabase("REST2");
        // get ref to collection
        userColection = db.getCollection("users");
        userColection.drop();
        userColection = db.getCollection("users");

    }

    public String createUser(String username, String password) {
//      create a new document
        Document doc = new Document("username", username).append("password", password);
        userColection.insertOne(doc);
        System.out.println(username + ", " + doc.getObjectId("_id"));
        return "okay";
    }

    public String login(String username, String password) {
        Document searchResult = userColection.find(eq("username", username)).first();
        if (searchResult == null || !password.equals(searchResult.getString("password"))) {
            return "login_failed";
        } else {
            String userID = searchResult.getObjectId("_id").toHexString();
            return userID;
        }
    }
    public boolean userExist(String userID) {
        try {
            ObjectId userObjId = new ObjectId(userID);
            Document searchResult = userColection.find(eq("_id", userObjId)).first();
            return searchResult != null;
        } catch (Exception e) {
            return false;
        }
    }
}