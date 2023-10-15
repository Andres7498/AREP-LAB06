package org.example;

import com.mongodb.MongoException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MongoDBConnection {

    private String url = "54.167.84.206:27017";
    private MongoClient mongoClient = null;
    private MongoDatabase mongoDatabase = null;
    private MongoCollection<Document> mongoCollection;

    public void createConnection() {
        try {
            mongoClient = new MongoClient(url);
            mongoDatabase = mongoClient.getDatabase("admin");
            mongoCollection = mongoDatabase.getCollection("colection-arep");
        } catch (MongoException ex) {
            System.out.println(ex);
        }
    }

    public List<String> getDocumentsColecction() {
        ArrayList<String> data = new ArrayList<>();
        for (Document d: mongoCollection.find()) {
            System.out.println(d.toJson());
            data.add(d.toJson());
        }
        return data.subList(Math.max(data.size() - 10, 0), data.size());
    }

    public void addDocument(String text) {
        Document myDocument = new Document();
        myDocument.put("text", text);
        Date date = new Date();
        SimpleDateFormat formateadorFecha = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formateadorHora = new SimpleDateFormat("HH:mm:ss");
        myDocument.put("date", formateadorFecha.format(date) + " " + formateadorHora.format(date));
        mongoCollection.insertOne(myDocument);
    }

    public void closeConnection() {
        this.mongoClient.close();
    }
}
