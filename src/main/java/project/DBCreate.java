package project;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.client.*;

import java.util.*;

public class DBCreate {

    public static void createUser(String username, String password) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {


            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");


            //Random rand = new Random();
            Document userAccount = new Document("account_id", new ObjectId());
            userAccount.append("username_id", username)
                        .append("password_id", password);


            accountCollection.insertOne(userAccount);
        }
    }

    public static Map<String,String> readUserInformation() {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");


            // find one document with new Document
            // Document userAccount = (Document) accountCollection.find(new Document("account_id", username));
            
            // find a list of documents and iterate throw it using an iterator.
            // FindIterable<Document> iterable = accountCollection.find(gte("username_id", username));
            MongoCursor<Document> cursor = accountCollection.find().cursor();
            Map<String, String> userInformation = new HashMap<>();
            
            while (cursor.hasNext()) {
                String jsonString = cursor.next().toJson();
                //[username: , password:],
                JSONObject obj = new JSONObject(jsonString);
                //String usernameJSON = obj.getJSONObject("username_id").getString("pageName");

                //JSONArray arr = obj.getJSONArray("posts"); // notice that `"posts": [...]`
                // String post_id = arr.getJSONObject(i).getString("post_id");
                //String usernameT = obj.get("username_id").toString();
                //JSONObject jsonObjectUsername = obj.getJSONObject("username_id");
                //String usernameInfoString = jsonObjectUsername.get(username).toString();
                //JSONObject jsonObjectPassword = obj.getJSONObject("password_id");
                //String passwordInfoString = jsonObjectPassword.get(password).toString();
                
                String usernameInfoString = obj.get("username_id").toString();
                String passwordInfoString = obj.get("password_id").toString();

                userInformation.put(usernameInfoString, passwordInfoString);
            }
            return userInformation;
        }
    }
}