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
            Document emailInfo = new Document();

            Document userAccount = new Document("account_id", new ObjectId());

            emailInfo.put("firstName_id", "");
            emailInfo.put("lastName_id", "");
            emailInfo.put("emailAddress_id", "");
            emailInfo.put("emailPassword_id", "");
            emailInfo.put("SMTPHost_id", "");
            emailInfo.put("TLSPort_id", "");
            emailInfo.put("displayName_id", "");

            userAccount.append("username_id", username)
                        .append("password_id", password)
                        .append("historyList", historyList)
                        .append("emailInfo", emailInfo);

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
                                            String emailPassword, String displayName) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");
            
            //Document emailAccount = new Document("emailInfo", new ObjectId());
            // emailAccount.append("firstname_id", firstName)
            //             .append("lastName_id", lastName)
            //             .append("username_id", userName)
            //             .append("emailAddress_id", emailAddress)
            //             .append("SMTPHost_id", SMTPHost)
            //             .append("TLSPort_id", TLSPort)
            //             .append("emailPassword_id", emailPassword);


            System.out.println(userName);
            Bson filter = eq("username_id", userName);
            Document user = accountCollection.find(filter).first();
            Document emailDocument = (Document) user.get("emailInfo");

            emailDocument.put("firstName_id", firstName);
            emailDocument.put("lastName_id", lastName);
            emailDocument.put("emailAddress_id", emailAddress);
            emailDocument.put("emailPassword_id", emailPassword);
            emailDocument.put("SMTPHost_id", SMTPHost);
            emailDocument.put("TLSPort_id", TLSPort);
            emailDocument.put("displayName_id", displayName);

            Bson updateOperation = set("emailInfo", emailDocument);
            accountCollection.updateOne(filter, updateOperation);
        }
    }

    public static Map<String, String> readEmailInformation(String username) {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");

            Bson filter = eq("username_id", username);
            Document user = accountCollection.find(filter).first();

            Document emailDocument = (Document) user.get("emailInfo");
            Map<String, String> emailInformation = new HashMap<String, String>();
            
            String firstNameString = emailDocument.get("firstName_id").toString();
            String lastNameString = emailDocument.get("lastName_id").toString();
            String emailAddressString = emailDocument.get("emailAddress_id").toString();
            String emailPasswordString = emailDocument.get("emailPassword_id").toString();
            String SMTPHostString = emailDocument.get("SMTPHost_id").toString();
            String TLSPortString = emailDocument.get("TLSPort_id").toString();
            String displayName = emailDocument.get("displayName_id").toString();

            emailInformation.put("firstName_id", firstNameString);
            emailInformation.put("lastName_id", lastNameString);
            emailInformation.put("emailAddress_id", emailAddressString);
            emailInformation.put("emailPassword_id", emailPasswordString);
            emailInformation.put("SMTPHost_id", SMTPHostString);
            emailInformation.put("TLSPort_id", TLSPortString);
            emailInformation.put("displayName_id", displayName);

            return emailInformation;
        }
    }
}