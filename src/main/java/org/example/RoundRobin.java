package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStreamReader;
import java.util.Random;

import static spark.Spark.port;
import static spark.Spark.staticFiles;
import static spark.Spark.*;

public class RoundRobin {

    public static String url = "http://";
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String... args){

        staticFiles.location("/public");

        port(getPort());

        get("/app", (req,res) -> getData());

        post("/app", (req,res) -> getPost(req.body()));

    }

    public static String getData() throws IOException {
        URL obj = new URL(url + getRoundRobin() + ":4567/service");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader( con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            return "GET request not worked";
        }
    }

    public static String getPost(String text) throws IOException {
        URL obj = new URL(url + getRoundRobin() + ":4567/service");
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "text/plain");
        con.setDoOutput(true);

        OutputStream os = con.getOutputStream();
        os.write(text.getBytes());
        os.flush();
        os.close();
        System.out.println("INSERTO " + con.getResponseCode());

        return getData();
    }

    private static String getRoundRobin() {
        String[] ips = {"3.85.188.145", "52.91.150.107", "34.205.65.158"};
        Random r = new Random();
        return ips[r.nextInt(3)];
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4566;
    }
}
