package org.example;

import java.util.List;

import static spark.Spark.*;

public class LogServiceApp {

    public static void main(String... args){

        MongoDBConnection mongoConnection = new MongoDBConnection();
        mongoConnection.createConnection();
        mongoConnection.closeConnection();
        port(getPort());

        get("/hello", (req, res) -> "Hello! test");

        get("/service", (req,res) -> {
            mongoConnection.createConnection();
            List<String> colecctions = mongoConnection.getDocumentsColecction();
            mongoConnection.closeConnection();
            return colecctions;
        } );

        post("/service", (req,res) -> {
            mongoConnection.createConnection();
            if(req.body()!=null){
                mongoConnection.addDocument(req.body());
            }
            List<String> colecctions = mongoConnection.getDocumentsColecction();
            mongoConnection.closeConnection();
            return colecctions;
        });

    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567;
    }

}
