package API;

import Controller.Server;
import Entity.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.websocket.WsContext;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        try {
            Server server = Server.getInstance();
            ConcurrentHashMap<String, WsContext> users = new ConcurrentHashMap<>();
            Javalin app = Javalin.create(config -> {
                config.plugins.enableCors(cors -> {
                    cors.add(it -> {
                        it.anyHost();
                    });
                });
                config.staticFiles.add("/Webbpage", Location.CLASSPATH);
            }).start(5500);

            app.get("/", ctx->{
                System.out.println("noice");
            }).get("/testPage", ctx->{
                System.out.println("testar");
                File file = new File("Webbpage/images/webpic.jpg");
                ImageIcontester image = new ImageIcontester();
                image.s = file;
                ctx.json(image);

            }).post("/addProduct", ctx->{

            }).post("/signup",ctx->{
                System.out.println(ctx.body());
                Response response = new Response();
                response.message = "NICE DONE";
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(new Gson().toJson(response));
                ctx.json(jsonNode);
            }).ws("/inbox", ws -> {
                //offline meddelanden + ta emot userId
                ws.onConnect(ctx -> {
                    String userid = ctx.queryParam("userid");
                    assert userid != null;
                    users.put(userid, ctx);
                });

                ws.onClose(ctx -> {

                });

                //Skicka ut rå data så kan javascript hantera detta
                ws.onMessage(ctx -> {

                });
            }).post("/login", ctx ->{
                Gson gson = new Gson();
                Login login = gson.fromJson(ctx.body().toString(), Login.class);
                if(server.login(login.username, login.password)){
                    ctx.json(gson.toJson(server.getUser(login.username)));
                } else {
                    ctx.json(gson.toJson(new LoginError()));
                }
            }).post("/register", ctx -> {
                Gson gson = new Gson();
                User user = gson.fromJson(ctx.body().toString(), User.class);
                Response response = new Response();
                response.message = server.registerNewUser(user);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(new Gson().toJson(response));
                ctx.json(jsonNode);
            }).post("/addProduct", ctx->{
                //TODO: can we reuse Gson, Response and Objectmapper instead of creating a new one every time?
                Gson gson = new Gson();
                Product product = gson.fromJson(ctx.body().toString(), Product.class);
                Response response = new Response();
                response.message = server.addProduct(product);
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(new Gson().toJson(response));
                ctx.json(jsonNode);
            }).post("/removeProduct", ctx -> {
                //TODO: Testa om dessa fungerar, addProduct, remove, sell och buy. Inga av dessa har testats, vilka ska ha response/inte ha response, har bara utgått från metoderna i server
               /*
                Gson gson = new Gson();
                int productId = gson.fromJson(ctx.body().toString(), int.class);
                Response response = new Response();
                response.message = server.removeProduct(productId).toString();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(new Gson().toJson(response));
                ctx.json(jsonNode);
                */
                Gson gson = new Gson();
                ObjectMapper objectMapper = new ObjectMapper();
                Response response = new Response();

                int productId = gson.fromJson(ctx.body(), int.class);
                response.message = server.removeProduct(productId).toString();
                String jsonResponse = objectMapper.writeValueAsString(response);
                ctx.json(jsonResponse);

            }).post("/sellProduct", ctx -> {
                int productId = Integer.parseInt(ctx.queryParam("productId"));
                String buyerName = ctx.queryParam("buyerName");

                server.sellProduct(productId, buyerName);



            }).post("/buyRequest", ctx -> {
                int[] productIds = ctx.bodyAsClass(int[].class);
                String buyerName = ctx.queryParam("buyerName");

                server.buyRequest(productIds, buyerName);

            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

