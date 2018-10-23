
import org.bson.Document;
import static spark.Spark.*;

import java.util.Set;


public class Main {
    private static UserService userService = new UserService();
    private static AuthService authService = new AuthService();
    private static FriendService friendService = new FriendService();
    
    public static void main(String[] args) {
        port(1236);
        
        get("/newuser", (req, res) -> {
            Set<String> params = req.queryParams();
            String username = null;
            String password = null;
            try {
                username = req.queryParamsValues("username")[0];
                password = req.queryParamsValues("password")[0];
            } catch (Exception e) {
                return "invalid input";
            }
            if (username == null || password == null || params.size() > 2) {
                return "Invalid input";
            }
            String result = userService.createUser(username, password);
            return result;
        });
        
        get("login", (req, res) -> {
            Set<String> params = req.queryParams();
            String username = null;
            String password = null;
            try {
                username = req.queryParamsValues("username")[0];
                password = req.queryParamsValues("password")[0];
            } catch (Exception e) {
                return "invalid input";
            }
            if (username == null || password == null || params.size() > 2) {
                return "invalid input";
            }
            String userID = userService.login(username, password);
            if (userID.equals("login_failed")) {
                return userID;
            } else {
                String token = authService.creatSession(userID);
                return token;
            }
        });
        
        get("addfriend", (req, res) -> {
            Set<String> params = req.queryParams();
            String token = null;
            String friend = null;
            try {
                token = req.queryParamsValues("token")[0];
                friend = req.queryParamsValues("friend")[0];
            } catch (Exception e) {
                return "invalid input";
            }
            if (token == null || friend == null || params.size() > 2) {
                return "invalid input";
            }
            Document userDoc = authService.findOwner(token);
            if (userDoc == null) {
                return "failed_authentication";
            }
            if (!userService.userExist(friend)) {
                return "invalid input: friend id not found";
            }
            String userID = userDoc.get("_id").toString();
            String result = friendService.addFriend(userID, friend);
            return result;
        });
        
        get("friends", (req, res) -> {
            Set<String> params = req.queryParams();
            String token = null;
            try {
                token = req.queryParamsValues("token")[0];
            } catch (Exception e) {
                return "invalid input";
            }
            if (token == null || params.size() > 1) {
                return "Invalid input";
            }
            Document tokenOwner = authService.findOwner(token);
            if (tokenOwner == null) {
                return "failed_authentication";
            }
            String userID = tokenOwner.get("_id").toString();
            Set<String> allFriends = friendService.getAllFriend(userID);
            return allFriends;
        });
    }
}
