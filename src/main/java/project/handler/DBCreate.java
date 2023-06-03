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

            emailInfo.put("firstname_id", "");
            emailInfo.put("lastName_id", "");
            emailInfo.put("username_id", "");
            emailInfo.put("displayName_id", "");
            emailInfo.put("emailAddress_id", "");
            emailInfo.put("SMTPHost_id", "");
            emailInfo.put("TLSPort_id", "");
            emailInfo.put("emailPassword_id", "");

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
            MongoCollection<Document> emailCollection = accountDB.getCollection("emailAccounts");
            MongoCollection<Document> accountCollection = accountDB.getCollection("Accounts");
            
            //Document emailAccount = new Document("emailInfo", new ObjectId());
            // emailAccount.append("firstname_id", firstName)
            //             .append("lastName_id", lastName)
            //             .append("username_id", userName)
            //             .append("emailAddress_id", emailAddress)
            //             .append("SMTPHost_id", SMTPHost)
            //             .append("TLSPort_id", TLSPort)
            //             .append("emailPassword_id", emailPassword);

            Bson filter = eq("username_id", userName);
            Document user = accountCollection.find(filter).first();
            Document emailDocument = (Document) user.get("emailInfo");

            emailDocument.put("firstname_id", firstName);
            emailDocument.put("lastName_id", lastName);
            emailDocument.put("username_id", userName);
            emailDocument.put("displayName_id", displayName);
            emailDocument.put("emailAddress_id", emailAddress);
            emailDocument.put("SMTPHost_id", SMTPHost);
            emailDocument.put("TLSPort_id", TLSPort);
            emailDocument.put("emailPassword_id", emailPassword);

            emailCollection.insertOne(emailDocument);
        }
    }

    public static ArrayList<String[]> readEmailInformation() {
        String uri = "mongodb+srv://josephyeh0903:josephycxyeh0903@cluster0.ytb32ia.mongodb.net/?retryWrites=true&w=majority";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase accountDB = mongoClient.getDatabase("UserAccounts");
            MongoCollection<Document> emailCollection = accountDB.getCollection("emailAccounts");

            MongoCursor<Document> cursor = emailCollection.find().cursor();
            String[] emailInformation = new String[7];
            ArrayList<String[]> allEmails = new ArrayList<>();
            
            while (cursor.hasNext()) {
                String jsonString = cursor.next().toJson();
                JSONObject obj = new JSONObject(jsonString);
                
                String firstNameString = obj.get("firstname_id").toString();
                String lastNameString = obj.get("lastName_id").toString();
                String userNameString = obj.get("username_id").toString();
                String emailAddressString = obj.get("emailAddress_id").toString();
                String emailPasswordString = obj.get("emailPassword_id").toString();
                String SMTPHostString = obj.get("SMTPHost_id").toString();
                String TLSPortString = obj.get("TLSPort_id").toString();

                emailInformation[0] = firstNameString;
                emailInformation[1] = lastNameString;
                emailInformation[2] = userNameString;
                emailInformation[3] = emailAddressString;
                emailInformation[6] = emailPasswordString;
                emailInformation[4] = SMTPHostString;
                emailInformation[5] = TLSPortString;
                allEmails.add(emailInformation);
            }
            return allEmails;
        }
    }
}