package API;

import Controller.Server;
import Entity.ImageIcontester;
import Entity.Login;
import Entity.LoginError;
import Entity.User;
import Entity.Product;
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
                config.staticFiles.add("/public", Location.CLASSPATH);
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
                Product product = ctx.bodyAsClass(Product.class);
                String msg = server.addProduct(product);
                ctx.result(msg);

            }).post("/removeProduct", ctx -> {
                int productId = Integer.parseInt(ctx.body());
                Product removedProduct = server.removeProduct(productId);
                if(removedProduct != null){
                    ctx.json(removedProduct);
                } else{
                    ctx.result("Product not found");
                }

            }).post("/sellProduct", ctx -> {
                int productId = Integer.parseInt(ctx.queryParam("productId"));
                String buyerName = ctx.queryParam("buyerName");

                server.sellProduct(productId, buyerName);

            }).post("/buyRequest", ctx -> {
                int[] productIds = ctx.bodyAsClass(int[].class);
                String buyerName = ctx.queryParam("buyerName");

                server.buyRequest(productIds, buyerName);

            }).get("/getItemsWithOffer", ctx -> {


            }).get("/getProducts", ctx -> {


            }).get("/getPurchaseHistory", ctx -> {


            }).post("/searchProduct", ctx -> {


            }).post("/registerUser", ctx -> {


            }).post("/login", ctx -> {


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
            }).get("/login", ctx ->{
                Gson gson = new Gson();
                Login login = gson.fromJson(ctx.body().toString(), Login.class);
                if(server.login(login.username, login.password)){
                    ctx.json(server.getUser(login.username));
                } else {
                    ctx.json(new LoginError());
                }
            }).get("/register", ctx -> {
                Gson gson = new Gson();
                User user = gson.fromJson(ctx.body().toString(), User.class);
                ctx.json(server.registerNewUser(user));
            });

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}

