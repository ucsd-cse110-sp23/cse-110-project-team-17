package project.handler;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.JSONObject;

import com.mongodb.client.*;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import java.util.*;

public class DBCreate {

    private static String regex = ";;;";

    public static void createUser(String username, String password) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");
            Document historyList = new Document();

            Document userAccount = new Document("account_id", new ObjectId());
            userAccount.append("username_id", username)
                        .append("password_id", password)
                        .append("historyList", historyList);


            accountCollection.insertOne(userAccount);
        }
    }

    public static Map<String,String> readUserInformation() {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            MongoCursor<Document> cursor = accountCollection.find().cursor();
            Map<String, String> userInformation = new HashMap<>();
            
            while (cursor.hasNext()) {
                String jsonString = cursor.next().toJson();
                JSONObject obj = new JSONObject(jsonString);
                
                String usernameInfoString = obj.get("username_id").toString();
                String passwordInfoString = obj.get("password_id").toString();

                userInformation.put(usernameInfoString, passwordInfoString);
            }
            return userInformation;
        }
    }

    // Method to return historyList
    public static ArrayList<String> getHistoryListDB(String username) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            Bson filter = eq("username_id", username);
            Document user = accountCollection.find(filter).first();

            Document historyDocument = (Document) user.get("historyList");
            ArrayList<String> historyList = new ArrayList<String>();
            if (historyDocument != null) {          
                for (String key : historyDocument.keySet()) {
                    String questionData = (String) historyDocument.get(key);
                    historyList.add(questionData);
                }
            }

            return historyList;
        }
    }

    // Method to add a question to history list
    public static void addPromptDB(String username, String questionText) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            Bson filter = eq("username_id", username);
            Document user = accountCollection.find(filter).first();

            Document historyDocument = (Document) user.get("historyList");

            String index = questionText.split(regex)[0];
            historyDocument.put(index, questionText);
            Bson updateOperation = set("historyList", historyDocument);
            accountCollection.updateOne(filter, updateOperation);
            
        }
    }

    // Method to delete a specific question from history list
    public static void deleteQuestionDB(String username, String questionText) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            Bson filter = eq("username_id", username);
            Document user = accountCollection.find(filter).first();

            Document historyDocument = (Document) user.get("historyList");

            String index = questionText.split(regex)[0];
            historyDocument.remove(index);
            Bson updateOperation = set("historyList", historyDocument);
            accountCollection.updateOne(filter, updateOperation);
            
        }
    }

    // Method to clear all history from history list
    public static void clearAllDB(String username) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            Bson filter = eq("username_id", username);
            Document newHistoryDoc = new Document();
            Bson updateOperation = set("historyList", newHistoryDoc);
            accountCollection.updateOne(filter, updateOperation);
        }
    }

    // Method to remove all current users from account
    public static void wipeDB() {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");
            String username_field = "username_id";
            for (Document doc : accountCollection.find()) {
                String username = (String) doc.get(username_field);
                Bson filter = eq(username_field, username);
                accountCollection.deleteOne(filter);
            }
        }
    }

    // Method to setup user's email information
    public static void addEmailInformation(String firstName, String lastName, String userName,
                                            String emailAddress, String SMTPHost, String TLSPort,
                                            String emailPassword) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> emailCollection = accountDB.getCollection("emailAccounts");
            //MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            //Bson filter = eq("username_id", userName);
            //Document usernameDoc = accountCollection.find(filter).first();
            Document emailAccount = new Document("emailAccount_id", new ObjectId());
            emailAccount.append("firstname_id", firstName)
                        .append("lastName_id", lastName)
                        .append("username_id", userName)
                        .append("emailAddress_id", emailAddress)
                        .append("SMTPHost_id", SMTPHost)
                        .append("TLSPort_id", TLSPort)
                        .append("emailPassword_id", emailPassword);

            emailCollection.insertOne(emailAccount);
        }
    }
}